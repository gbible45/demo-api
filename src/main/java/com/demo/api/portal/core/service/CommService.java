package com.demo.api.portal.core.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CommService {
    private static final Log log = LogFactory.getLog(CommService.class);

	public StandardPBEStringEncryptor getEncryptor(String encKey) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setAlgorithm("PBEWithMD5AndDES");
		encryptor.setPassword(encKey);
		encryptor.setKeyObtentionIterations(1000);
		encryptor.setProviderName("SunJCE");
		encryptor.setSaltGenerator(new org.jasypt.salt.RandomSaltGenerator());
		encryptor.setStringOutputType("base64");
		return encryptor;
	}

	public String getEncrypt(String encKey, String encValue) {
		return getEncryptor(encKey).encrypt(encValue);
	}

	public String getDecrypt(String encKey, String encValue) {
		return getEncryptor(encKey).decrypt(encValue);
	}

}