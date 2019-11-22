package com.demo.api.framework.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * An SMTP mail sender, which sends mails
 * using an injected JavaMailSender.
 * 
 * @author Sanjay Patel
 *
 */
@Slf4j
public class SmtpMailSender implements MailSender {
	
	private JavaMailSender javaMailSender;
	
	/**
	 * Setter method for injecting a JavaMailSender.
	 */
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Value("${portal.admin.email:}")
	String ADMIN_EMAIL;

	/**
	 * Sends a mail using a MimeMessageHelper
	 */
	@Override
	@Async
	public void send(String to, String subject, String body)
			throws MessagingException {
		
		log.info("Sending SMTP mail from thread " + Thread.currentThread().getName()); // toString gives more info    	

		// create a mime-message
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;

		// create a helper
		helper = new MimeMessageHelper(message, true); // true indicates
													   // multipart message
		// set the attributes
		helper.setSubject(subject);
		helper.setTo(to);
		helper.setFrom(ADMIN_EMAIL);
		helper.setText(body, true); // true indicates html
		// continue using helper object for more functionalities like adding attachments, etc.  
		
		//send the mail
		javaMailSender.send(message);
		
		log.info("Sent SMTP mail from thread " + Thread.currentThread().getName());    	
	}

}
