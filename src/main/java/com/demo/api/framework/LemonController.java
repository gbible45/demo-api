package com.demo.api.framework;


import com.demo.api.framework.domain.AbstractUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

/**
 * The Lemon API
 * 
 * @author Sanjay Patel
 *
 * @param <U>	The User class
 * @param <ID>	The Primary key type of User class 
 */
@Slf4j
public abstract class LemonController
	<U extends AbstractUser<U,ID>, ID extends Serializable> {

	protected LemonService<U, ID> lemonService;
	
	@Autowired
	public void setLemonService(LemonService<U, ID> lemonService) {
		this.lemonService = lemonService;
	}


	/**
	 * A simple function for pinging this server.
	 */
/*
	@RequestMapping(value="/ping/", method= RequestMethod.GET)
	public void ping() {
		log.debug("Received a ping");
	}
*/

	//@RequestMapping(value="/forgot-admin-password", method= RequestMethod.POST)
	public void forgotAdminPassword(@RequestParam String email) {

		log.debug("Received forgot password request for: " + email);
		lemonService.forgotAdminPassword(email);
	}

	/**
	 * The forgot Password feature.
	 */
	//@RequestMapping(value="/forgot-password", method= RequestMethod.POST)
	public void forgotPassword(@RequestParam String email) {
		
		log.debug("Received forgot password request for: " + email);				
		lemonService.forgotPassword(email);
	}
	

	/**
	 * Resets password after it is forgotten.
	 */
	//@RequestMapping(value="/users/{forgotPasswordCode}/reset-password",
	//				method= RequestMethod.POST)
	public void resetPassword(@PathVariable String forgotPasswordCode,
							  @RequestParam String newPassword) {
		
		log.debug("Resetting password ... ");				
		lemonService.resetPassword(forgotPasswordCode, newPassword);
	}
}
