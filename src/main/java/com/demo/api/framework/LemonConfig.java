package com.demo.api.framework;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Although most of the configurations are
 * inside various sub-packages, some didn't fit
 * anywhere, which are here, inside the root package. 
 * 
 * @author Sanjay Patel
 */
@Configuration
@EnableEncryptableProperties
@EnableSpringDataWebSupport
@EnableTransactionManagement
@EnableJpaAuditing
@EnableAsync
@Slf4j
public class LemonConfig {
	
	/**
	 * For handling JSON vulnerability,
	 * JSON response bodies would be prefixed with
	 * this string.
	 */
	public final static String JSON_PREFIX = ")]}',\n";

	/**
	 * Prefixes JSON responses for JSON vulnerability. See for more details:
	 * 
	 * https://docs.angularjs.org/api/ng/service/$http
	 * http://stackoverflow.com/questions/26384930/how-to-add-n-before-each-spring-json-response-to-prevent-common-vulnerab
	 * 
	 * To disable this, in your application.properties, use
	 * lemon.enabled.json-prefix: false
	 */
	@Bean
	@ConditionalOnProperty(name="lemon.enabled.json-prefix", matchIfMissing=true)
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setJsonPrefix(JSON_PREFIX);

        log.info("Configuring JSON vulnerability prefix ...");


		ObjectMapper mapper = new ObjectMapper();
		Hibernate4Module hm = new Hibernate4Module();
		hm.disable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION);  // @Transient 변환을 허용
		mapper.registerModule(hm);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		converter.setObjectMapper(mapper);

		log.info("Configuring JSON hibernate lazy objects ...");

        return converter;
	}
	
	/**
	 * Needed in CaptchaValidator.
	 * 
	 * ConditionalOnMissingBean will ensure that this bean will be processed
	 * in the REGISTER_BEAN ConfigurationPhase. For more details see:
	 * ConditionEvaluator.shouldSkip, ConfigurationPhase.REGISTER_BEAN
	 *  
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(RestTemplate.class)
	public RestTemplate restTemplate() {
		
		log.info("Configuring RestTemplate ...");
		
		return new RestTemplate();
	}

	@Value("${spring.profiles.active:}")
	private String profilesActive;
	@Value("${jasypt.encryptor.password:}")
	private String encryptorPassword;

	@Bean(name = "jasyptStringEncryptor")
	public StringEncryptor stringEncryptor() {
		log.info("Bean jasyptStringEncryptor stringEncryptor ...");
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		if (StringUtils.isEmpty(encryptorPassword)) {
			if (StringUtils.isEmpty(profilesActive)) {
				profilesActive = "local";
			}
			encryptorPassword = "jasypt_" + profilesActive + "_pass";
		}
		config.setPassword(encryptorPassword);
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		return encryptor;
	}
}
