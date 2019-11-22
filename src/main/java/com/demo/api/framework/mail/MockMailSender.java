package com.demo.api.framework.mail;

import lombok.extern.slf4j.Slf4j;

/**
 * A mock mail sender for 
 * writing the mails to the log.
 * 
 * @author Sanjay Patel
 */
@Slf4j
public class MockMailSender implements MailSender {

	@Override
	public void send(String to, String subject, String body) {
		log.info("Sending mail to " + to);
		log.info("Subject: " + subject);
		log.info("Body: " + body);
	}

}
