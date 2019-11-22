package com.demo.api.framework.validation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;

/**
 * Configures method validation
 * 
 * @author Sanjay Patel
 *
 */
@Configuration
public class ValidationConfig {

	private final Log log = LogFactory.getLog(getClass());

	@Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
    	
        log.info("Configuring MethodValidationPostProcessor.");

		MethodValidationPostProcessor processor =
        		new MethodValidationPostProcessor();
        processor.setValidator(validator());
        return processor;
    }
    
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }	
}
