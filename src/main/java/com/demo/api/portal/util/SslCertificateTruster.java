package com.demo.api.portal.util;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 
 * Utility for getting the JVM to trust an untrusted certificate
 * 
 * @author wtran@pivotal.io
 *
 */
public class SslCertificateTruster {
	public static final String JAVAX_NET_SSL_TRUST_STORE_PR_NAME = "javax.net.ssl.trustStorePassword";
	public static final String JAVAX_NET_SSL_TRUST_STORE = "javax.net.ssl.trustStore";
	private final ExecutorService executor;

	private SslCertificateTruster() {
		executor = Executors.newFixedThreadPool(1, new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "SSLCertificateTruster:downloader");
			}
		});
	}

	static final SslCertificateTruster instance = new SslCertificateTruster();

	/**
	 * Performs an SSL handshake with the given host and port, and if the JVM
	 * does not trust the certificate presented during SSL handshake, return the
	 * certificate chain, otherwise return null.
	 * 
	 * @param host
	 * @param port
	 * @param timeout
	 *            socket timeout in milliseconds
	 * @return
	 * @throws Exception
	 *             if the certificate chain could not be obtained during SSL
	 *             handshake
	 */
	public static X509Certificate[] getUntrustedCertificate(String host, int port, int timeout) throws Exception {
		return instance.getUntrustedCertificateInternal(host, port, timeout);
	}

	X509Certificate[] getUntrustedCertificateInternal(final String host, final int port, int timeout) throws Exception {
		SSLContext context = SSLContext.getInstance("TLS");
		X509TrustManager defaultTrustManager = getDefaultTrustManager();
		CertificateCollectingTrustManager collector = new CertificateCollectingTrustManager(defaultTrustManager);
		context.init(null, new TrustManager[] { collector }, null);
		final SSLSocketFactory factory = context.getSocketFactory();

		Future<?> task = executor.submit(new Runnable() {
			@Override
			public void run() {
				SSLSocket socket = null;
				try {
					socket = (SSLSocket) factory.createSocket(host, port);
					socket.startHandshake();
					socket.close();
				} catch (Exception e) {
					System.err.println("Error downloading certificate " + host + ":" + port + "," + e);
					e.printStackTrace();
				} finally {
					if (socket != null) {
						try {
							socket.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		try {
			task.get(timeout, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			task.cancel(true);
			throw e;
		}

		X509Certificate[] chain = collector.getCollectedCertificateChain();
		if (chain == null) {
			throw new CertificateException("Could not obtain server certificate chain");
		} else if (collector.isTrusted()) {
			return null;
		}
		return chain;
	}

	/**
	 * If the certificate at the specified host and port is untrusted, append
	 * the untrusted certificate to the JVM's currently used truststore. The
	 * change is made to a copy of the truststore, and the JVM can use the new
	 * copy via system property "javax.net.ssl.trustStore".
	 * 
	 * @param host
	 * @param port
	 * @param timeout
	 * @throws Exception
	 */
	public static void trustCertificate(String host, int port, int timeout) throws Exception {
		instance.trustCertificateInternal(host, port, timeout);
	}

	void trustCertificateInternal(String host, int port, int timeout) throws Exception {
		X509Certificate[] untrusted = getUntrustedCertificate(host, port, timeout);
		if (untrusted != null) {
			appendToTruststore(untrusted);
		}
	}

	/**
	 * Append the certificate to the JVM's currently used truststore. The change
	 * is made to a copy of the truststore, and the JVM can use the new copy via
	 * system property "javax.net.ssl.trustStore".
	 * 
	 * @param chain
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 */
	public static void appendToTruststore(X509Certificate[] chain) throws NoSuchAlgorithmException, KeyStoreException,
			IOException, CertificateException, FileNotFoundException {
		instance.appendToTruststoreInternal(chain);
	}

	void appendToTruststoreInternal(X509Certificate[] chain) throws NoSuchAlgorithmException, KeyStoreException,
			IOException, CertificateException, FileNotFoundException {
		X509TrustManager defaultTrustManager = getDefaultTrustManager();
		X509Certificate[] cacerts = defaultTrustManager.getAcceptedIssuers();

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(null); // init empty keystore
		int count = 0;
		for (X509Certificate cert : cacerts) {
			trustStore.setCertificateEntry("" + count++, cert);
		}
		for (X509Certificate cert : chain) {
			trustStore.setCertificateEntry("" + count++, cert);
		}

		String password = UUID.randomUUID().toString();
		OutputStream stream = null;
		try {
			File trustStoreOutputFile = File.createTempFile("truststore", null);
			trustStoreOutputFile.deleteOnExit();
			stream = new FileOutputStream(trustStoreOutputFile);
			trustStore.store(stream, password.toCharArray());
			System.setProperty(JAVAX_NET_SSL_TRUST_STORE, trustStoreOutputFile.getAbsolutePath());
			System.setProperty(JAVAX_NET_SSL_TRUST_STORE_PR_NAME, password);
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	private static X509TrustManager getDefaultTrustManager() throws NoSuchAlgorithmException, KeyStoreException {
		TrustManagerFactory trustManagerFactory =
				TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

		// will initialize with the first valid keystore
		// 1. javax.net.ssl.trustStore
		// 2. jssecerts
		// 3. cacerts
		// see https://github.com/openjdk-mirror/jdk7u-jdk/blob/master/src/share/classes/sun/security/ssl/TrustManagerFactoryImpl.java#L130
		trustManagerFactory.init((KeyStore) null);

		X509TrustManager defaultTrustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
		return defaultTrustManager;
	}

	private static class CertificateCollectingTrustManager implements X509TrustManager {

		private final X509TrustManager delegate;
		private X509Certificate[] collected;
		private Boolean trusted;

		CertificateCollectingTrustManager(X509TrustManager delegate) {
			this.delegate = delegate;
		}

		public X509Certificate[] getAcceptedIssuers() {
			return delegate.getAcceptedIssuers();
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			if (collected != null) {
				throw new IllegalStateException("A certificate chain has already been collected.");
			} else {
				collected = chain;
			}
			try {
				delegate.checkClientTrusted(chain, authType);
				trusted = true;
			} catch (CertificateException e) {
				trusted = false;
			}
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			if (collected != null) {
				throw new IllegalStateException("A certificate chain has already been collected.");
			} else {
				collected = chain;
			}
			try {
				delegate.checkServerTrusted(chain, authType);
				trusted = true;
			} catch (CertificateException e) {
				trusted = false;
			}
		}

		private boolean isTrusted() {
			if (trusted == null) {
				throw new IllegalStateException("No certificates have been collected yet");
			}
			return trusted;
		}

		private X509Certificate[] getCollectedCertificateChain() {
			return collected;
		}
	}

}