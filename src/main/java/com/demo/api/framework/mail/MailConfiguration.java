package com.demo.api.framework.mail;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Configures a mail sender, only if
 * your application hasn't already configured it
 * in a configuration class. 
 * 
 * @author Sanjay Patel
 */
@Configuration
@ConditionalOnMissingBean(MailSender.class)
public class MailConfiguration {
	
	/**
	 * Configures a MockMailSender when the property
	 * <code>spring.mail.host</code> isn't defined.
	 */
	@Bean
	@ConditionalOnProperty(name="spring.mail.host", havingValue="foo", matchIfMissing=true)
	public MailSender mockMailSender() {
		return new MockMailSender();
	}

	
	/**
	 * Configures an SmtpMailSender when the property
	 * <code>spring.mail.host</code> is defined.
	 */
	@Bean
	@ConditionalOnProperty("spring.mail.host")
	public MailSender smtpMailSender(JavaMailSender javaMailSender) {
		SmtpMailSender mailSender = new SmtpMailSender();
		mailSender.setJavaMailSender(javaMailSender);
		return mailSender;
	}

}
