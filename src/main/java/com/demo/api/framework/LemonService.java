package com.demo.api.framework;

import com.demo.api.framework.domain.AbstractUser;
import com.demo.api.framework.domain.ChangePasswordForm;
import com.demo.api.framework.exception.MultiErrorException;
import com.demo.api.framework.mail.Mail;
import com.demo.api.framework.mail.MailSender;
import com.demo.api.framework.repository.AbstractUserRepository;
import com.demo.api.framework.util.XpertUtil;
import com.demo.api.framework.validation.Password;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The Lemon Service class
 * 
 * @author Sanjay Patel
 *
 * @param <U>	The User class
 * @param <ID>	The Primary key type of User class 
 */
@Validated
@Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
@Slf4j
public abstract class LemonService
	<U extends AbstractUser<U,ID>, ID extends Serializable> {

	private LemonProperties properties;
	private PasswordEncoder passwordEncoder;
    private MailSender mailSender;
	protected AbstractUserRepository<U, ID> abstractUserRepository;
	private UserDetailsService userDetailsService;
    
	@Autowired
    public void setProperties(LemonProperties properties) {
		this.properties = properties;
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Autowired
	public void setAbstractUserRepository(AbstractUserRepository<U, ID> abstractUserRepository) {
		this.abstractUserRepository = abstractUserRepository;
	}

	@Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/**
     * This method is called after the application is ready.
     * Needs to be public - otherwise Spring screams.
     * 
     * @param event
     */
    @EventListener
    public void afterApplicationReady(ApplicationReadyEvent event) {
    	
    	log.info("Starting up Spring Lemon ...");
    	onStartup(); // delegate to onStartup()
    	log.info("Spring Lemon started");	
    }

    
	/**
	 * Creates a the initial Admin user, if not found.
	 * Override this method if needed.
	 */
	@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public void onStartup() {
    	
		try {
			// Check if the user already exists
			userDetailsService.loadUserByUsername(properties.getAdmin().getUsername());
		} catch (UsernameNotFoundException e) {
			// Doesn't exist. So, create it.
	    	// U user = createAdminUser();
			// userRepository.save(user);
		}
	}

	/**
	 * Gets the current-user to be sent to a client.
	 *
	 * @return
	 */
	public U userForClient() {

		// delegates
		return userForClient(XpertUtil.getUser());
	}


	/**
	 * Gets the current-user to be sent to a client.
	 * Override this if you have more fields.
	 *
	 * @param currentUser
	 */
	protected U userForClient(U currentUser) {

		if (currentUser == null)
			return null;

		U userS = abstractUserRepository.findByEmail(currentUser.getEmail())
				.orElseThrow(() -> MultiErrorException.of("email",
						"com.naturalprogrammer.spring.userNotFound"));

		U user = newUser();
		user.setIdForClient(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		user.setRoles(currentUser.getRoles());
		user.decorate(currentUser);

		user.setVersion(userS.getVersion());

		log.debug("Returning user for client: " + user);

		return user;
	}

	/**
	 * Creates the initial Admin user.
	 * Override this if needed.
	 */
    protected U createAdminUser() {
		
    	// fetch data about the user to be created
    	LemonProperties.Admin initialAdmin = properties.getAdmin();
    	
    	log.info("Creating the first admin user: " + initialAdmin.getUsername());

    	// create the user
    	U user = newUser();
		user.setUsername(initialAdmin.getUsername());
		user.setPassword(passwordEncoder.encode(properties.getAdmin().getPassword()));
		user.getRoles().add(AbstractUser.Role.ADMIN);
		
		return user;
	}

	/**
	 * Creates the initial Admin user.
	 * Override this if needed.
	 */
	protected U createAdminUser(String username, String password) {

		log.info("Creating the first admin user: " + username);

		// create the user
		U user = newUser();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.getRoles().add(AbstractUser.Role.ADMIN);

		return user;
	}


	/**
	 * Creates a new user object. Must be overridden in the
	 * subclass, like this:
	 * 
	 * <pre>
	 * protected User newUser() {
	 *    return new User();
	 * }
	 * </pre>
	 */
    abstract protected U newUser();


	/**
	 * Returns the context data to be sent to the client,
	 * i.e. <code>reCaptchaSiteKey</code> and all the properties
	 * prefixed with <code>lemon.shared</code>.
	 * 
	 * To send custom properties, put those in your application
	 * properties in the format <em>lemon.shared.fooBar</em>.
	 * 
	 * Override this method if needed.
	 */
	public Map<String, Object> getContext() {
		
		// make the context
		Map<String, Object> context = new HashMap<String, Object>(2);
		context.put("reCaptchaSiteKey", properties.getRecaptcha().getSitekey());
		context.put("shared", properties.getShared());
		
		log.debug("Returning context: " + context);

		return context;		
	}
	
	
	/**
	 * Signs up a user.
	 * 
	 * @param user	data fed by the user
	 */
	//@PreAuthorize("isAnonymous()")
	@Validated(AbstractUser.SignUpValidation.class)
	@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
	public U signup(@Valid U user, String password) {
		log.debug("Signing up user: " + user);
		initUser(user); // sets right all fields of the user
		abstractUserRepository.save(user);
		return user;
	}
	
	
	/**
	 * Initializes the user based on the input data
	 * 
	 * @param user
	 */
	@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
	protected void initUser(U user) {
		
		log.debug("Initializing user: " + user);

		user.setPassword(passwordEncoder.encode(user.getPassword())); // encode the password
		makeUnverified(user); // make the user unverified
	}

	
	/**
	 * Makes a user unverified
	 * @param user
	 */
	protected void makeUnverified(U user) {
		user.getRoles().add(AbstractUser.Role.UNVERIFIED);
		user.setVerificationCode(UUID.randomUUID().toString());
	}
	
	
	/***
	 * Makes a user verified
	 * @param user
	 */
	protected void makeVerified(U user) {
		user.getRoles().remove(AbstractUser.Role.UNVERIFIED);
		user.setVerificationCode(null);
	}

	/**
	 * Returns a non-null, decorated user for the client.
	 * 
	 * @param user
	 * @return
	 */
	public U processUser(U user) {
		
		log.debug("Fetching user: " + user);

		// ensure that the user exists
		XpertUtil.check("id", user != null,
			"com.naturalprogrammer.spring.userNotFound").go();
		
		// decorate the user, and hide confidential fields
		user.decorate().hideConfidentialFields();
		
		return user;
	}
	
	
	/**
	 * Verifies the email id of current-user
	 *  
	 * @param verificationCode
	 */
	//@PreAuthorize("isAuthenticated()")
	@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
	public U verifyUser(@Valid @NotBlank String verificationCode) {
		
		log.debug("Verifying user ...");

		U user = abstractUserRepository.findByVerificationCode(verificationCode)
				.orElseThrow(() -> MultiErrorException.of(
						"com.verificationCode"));

		makeVerified(user); // make him verified
		abstractUserRepository.save(user);
		
		log.debug("Verified user: " + user);
		return user;
	}

	@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
	public void forgotAdminPassword(@Valid @NotBlank String email) {

		log.debug("Processing forgot password for email: " + email);

		// fetch the user record from database
		U user = abstractUserRepository.findByEmail(email)
				.orElseThrow(() -> MultiErrorException.of(
						"com.userNotFound"));

		// set a forgot password code
		user.setForgotPasswordCode(UUID.randomUUID().toString());
		abstractUserRepository.save(user);
	}

	/**
	 * Forgot password.
	 * 
	 * @param email	the email of the user who forgot his password
	 */
	@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
	public void forgotPassword(@Valid @NotBlank String email) {
		
		log.debug("Processing forgot password for email: " + email);
		
		// fetch the user record from database
		U user = abstractUserRepository.findByEmail(email)
				.orElseThrow(() -> MultiErrorException.of(
					"com.userNotFound"));

		// set a forgot password code
		user.setForgotPasswordCode(UUID.randomUUID().toString());
		abstractUserRepository.save(user);

	}

	/**
	 * Resets the password.
	 * 
	 * @param forgotPasswordCode
	 * @param newPassword
	 */
	@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
	public U resetPassword(@Valid @NotBlank String forgotPasswordCode,
							  @Valid @Password String newPassword) {
		
		log.debug("Resetting password ...");

		// fetch the user
		U user = abstractUserRepository
			.findByForgotPasswordCode(forgotPasswordCode)
			.orElseThrow(() -> MultiErrorException.of(
				"com.invalidLink"));
		
		// sets the password
		user.setPassword(passwordEncoder.encode(newPassword));
		// 회원가입 후 이메일 인증하기 전에 비밀번호 재설정하여 재설정 메일 인증한 경우
		// 회원가입 이메일 인증도 되었다고 판단
		if(user.getVerificationCode() != null) {
			makeVerified(user); // make him verified
		}
		user.setForgotPasswordCode(null);

		abstractUserRepository.save(user);
		
		log.debug("Password reset.");

		return user;
	}

	
	/**
	 * Updates a user with the given data.
	 * 
	 * @param user
	 * @param updatedUser
	 */
	@PreAuthorize("hasPermission(#user, 'edit')")
	@Validated(AbstractUser.UpdateValidation.class)
	@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
	public void updateUser(U user, @Valid U updatedUser) {
		
		log.debug("Updating user: " + user);

		// checks
		XpertUtil.check("id", user != null,
			"com.userNotFound").go();
		XpertUtil.validateVersion(user, updatedUser);

		abstractUserRepository.save(user);
		
		log.debug("Updated user: " + user);		
	}
	
	
	/**
	 * Changes the password.
	 * 
	 * @param user
	 * @param changePasswordForm
	 */
	@PreAuthorize("hasRole('ROLE_GOOD_USER')")
	@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
	public void changePassword(U user, @Valid ChangePasswordForm changePasswordForm) {
		
		log.debug("Changing password for user: " + user);

		// checks
		XpertUtil.check("changePasswordForm.password",
				changePasswordForm.getPassword().equals(changePasswordForm.getRetypePassword()),
				"com.different.passwords").go();
		XpertUtil.check("id", user != null,
			"com.naturalprogrammer.spring.userNotFound").go();
		XpertUtil.check("changePasswordForm.oldPassword",
			passwordEncoder.matches(changePasswordForm.getOldPassword(), user.getPassword()),
			"com.wrong.password").go();

		// sets the password
		user.setPassword(passwordEncoder.encode(changePasswordForm.getPassword()));
		U userSaved = abstractUserRepository.save(user);
	}

	public void loginPostProcess(String password) {
		loginPostProcess(XpertUtil.getUser(), password);
	}

	protected void loginPostProcess(U currentUser, String password) {

	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
	public void sendAppUpdateResult(String toEmail, String appName, String state) {
		Mail mail = new Mail();
		String strSubject = "";
		String strMessage = "";
		String link = properties.getApplicationUrl();

		log.debug("Sending email about updating marketapp to user");
		if ("STARTED".equals(state)) {
			strSubject = XpertUtil.getMessage("demo.updateSucceedSubject", appName);
			strMessage = mail.sendMail(XpertUtil.getMessage("demo.updateSucceedMessage", appName), link);
		} else if ("FAILED".equals(state)) {
			strSubject = XpertUtil.getMessage("demo.updateFailedSubject", appName);
			strMessage = mail.sendMail(XpertUtil.getMessage("demo.updateFailedMessage", appName), link);
		}

		try {
			mailSender.send(toEmail, strSubject, strMessage);
		} catch (MessagingException e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
		log.debug("mail about updating marketapp to queued.");
	}
}
