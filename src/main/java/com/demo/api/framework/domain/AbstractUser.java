package com.demo.api.framework.domain;


import com.demo.api.framework.security.LemonSecurityConfig;
import com.demo.api.framework.util.XpertUtil;
import com.demo.api.framework.validation.Password;
import com.demo.api.framework.validation.UniqueUserId;
import com.demo.api.framework.security.LemonSecurityConfig;
import com.demo.api.framework.util.XpertUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Base class for User entity
 * 
 * @author Sanjay Patel
 *
 * @param <U>	The User class
 * @param <ID>	The Primary key type of User class 
 */
@MappedSuperclass
public abstract class AbstractUser
	<U extends AbstractUser<U,ID>, ID extends Serializable>
extends VersionedEntity<U, ID>
implements UserDetails {
	
	private static final Log log = LogFactory.getLog(AbstractUser.class);
			
	private static final long serialVersionUID = 655067760361294864L;
	
	public static final int USRID_MIN = 4;
	public static final int USRID_MAX = 250;
	
	public static final int UUID_LENGTH = 36;
	
	public static final int PASSWORD_MAX = 50;
	public static final int PASSWORD_MIN = 6;

	/**
	 * Role constants. To allow extensibility, this couldn't
	 * be made an enum
	 */
	public static interface Role {
		static final String UNVERIFIED = "UNVERIFIED";
		static final String BLOCKED = "BLOCKED";
		static final String ADMIN = "ADMIN";
		static final String DEVELOPER = "DEVELOPER";
		static final String AUDITOR = "AUDITOR";
		static final String MONITOR = "MONITOR";
		static final String DEREGISTERED = "DEREGISTERED";
	}
	
	// validation groups
	public interface SignUpValidation {}
	public interface UpdateValidation {}
	public interface ChangeEmailValidation {}

	// email
	@UniqueUserId(groups = {SignUpValidation.class})
	@Column(nullable = false, unique=true, length = USRID_MAX)
	protected String email;
	
	// password
	@Password(groups = {SignUpValidation.class, ChangeEmailValidation.class})
	@Column(nullable = false) // no length because it will be encrypted
	protected String password;
	
	// roles collection
	@ElementCollection(fetch = FetchType.EAGER)
	//@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	private Set<String> roles = new HashSet<String>();
	
	// verification code
	@Column(length = UUID_LENGTH, unique=true)
	protected String verificationCode;

	// forgot password code
	@Column(length = UUID_LENGTH, unique=true)
	protected String forgotPasswordCode;

	@Transient
	protected boolean unverified = false;

	@Transient
	protected boolean blocked = false;

	@Transient
	protected boolean admin = false;

	@Transient
	protected boolean developer = false;

	@Transient
	protected boolean auditor = false;

	@Transient
	protected boolean monitor = false;

	@Transient
	protected boolean deRegistered = false;

	@Transient
	protected boolean goodUser = false;

	@Transient
	protected boolean goodMonitor = false;

	@Transient
	protected boolean goodAuditor = false;

	@Transient
	protected boolean goodDeveloper = false;

	@Transient
	protected boolean goodAdmin = false;

	@Transient
	protected boolean editable = false;
	
	@Transient
	protected boolean rolesEditable = false;

	//
	public AbstractUser() {}

	public AbstractUser(String email, String password) {
		this.email = email;
		this.password = password;
	}


	// getters and setters

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getForgotPasswordCode() {
		return forgotPasswordCode;
	}

	public void setForgotPasswordCode(String forgotPasswordCode) {
		this.forgotPasswordCode = forgotPasswordCode;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// override this method
	// if email isn't your username
	@Override
	public String getUsername() {
		return email;
	}
	
	// override this method
	// if email isn't your username
	public void setUsername(String username) {
		email = username;
	}	
	
	public boolean isUnverified() {
		return unverified;
	}

	public void setUnverified(boolean unverified) {
		this.unverified = unverified;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isDeveloper() {
		return developer;
	}

	public void setDeveloper(boolean developer) {
		this.developer = developer;
	}

	public boolean isAuditor() {
		return auditor;
	}

	public void setAuditor(boolean auditor) {
		this.auditor = auditor;
	}

	public boolean isMonitor() {
		return monitor;
	}

	public void setMonitor(boolean monitor) {
		this.monitor = monitor;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isRolesEditable() {
		return rolesEditable;
	}

	public final boolean hasRole(String role) {
		return roles.contains(role);
	}

	public boolean isGoodUser() {
		return goodUser;
	}

	public void setGoodUser(boolean goodUser) {
		this.goodUser = goodUser;
	}

	public boolean isGoodMonitor() {
		return goodMonitor;
	}

	public void setGoodMonitor(boolean goodMonitor) {
		this.goodMonitor = goodMonitor;
	}

	public boolean isGoodAuditor() {
		return goodAuditor;
	}

	public void setGoodAuditor(boolean goodAuditor) {
		this.goodAuditor = goodAuditor;
	}

	public boolean isGoodDeveloper() {
		return goodDeveloper;
	}

	public void setGoodDeveloper(boolean goodDeveloper) {
		this.goodDeveloper = goodDeveloper;
	}

	public boolean isGoodAdmin() {
		return goodAdmin;
	}

	public void setGoodAdmin(boolean goodAdmin) {
		this.goodAdmin = goodAdmin;
	}

	/**
	 * Sets the transient fields of this user
	 * 
	 * @return	this user
	 */
	public U decorate() {
		// delegates
		return decorate(XpertUtil.getUser());
	}
	
	
	/**
	 * Sets the transient fields of this user,
	 * given the current-user
	 * 
	 * @param currentUser	the current-user
	 * @return	this user
	 */
	public U decorate(U currentUser) {
		unverified = hasRole(Role.UNVERIFIED);
		blocked = hasRole(Role.BLOCKED);
		deRegistered = hasRole(Role.DEREGISTERED);

		admin = hasRole(Role.ADMIN);
		developer = hasRole(Role.DEVELOPER);
		auditor = hasRole(Role.AUDITOR);
		monitor = hasRole(Role.MONITOR);

		goodUser = !(unverified || blocked || deRegistered);
		goodMonitor = goodUser && (monitor || auditor || developer || admin);
		goodAuditor = goodUser && (auditor || developer || admin);
		goodDeveloper = goodUser && (developer || admin);
		goodAdmin = goodUser && admin;
		
		editable = false;
		rolesEditable = false;
		
		if (currentUser != null) {
			editable = currentUser.isGoodAdmin() || equals(currentUser); // admin or self
			rolesEditable = currentUser.isGoodAdmin() && !equals(currentUser); // another admin
		}
		
		computeAuthorities();
		
		log.debug("Decorated user: " + this);

		return (U) this;
	}
	
	
	/**
	 * Hides the confidential fields before sending to client
	 */
	public void hideConfidentialFields() {
		
		//setCreatedDate(null);
		//setLastModifiedDate(null);
		password = null;
		verificationCode = null;
		forgotPasswordCode = null;
		authorities = null;
		log.debug("Hid confidential fields for user: " + this);
	}

	
	/**
	 * Called by spring security permission evaluator
	 * to check whether the current-user has the given permission
	 * on this entity. 
	 */
	@Override
	public boolean hasPermission(U currentUser, String permission) {
		
		log.debug("Computing " + permission	+ " permission for : " + this
			+ "\n  Logged in user: " + currentUser);

		// decorate this entity
		decorate(currentUser);
		
		if (permission.equals("edit"))
			return editable;

		return false;
	}

	
	/**
	 * Sets the Id of the user. setId is protected,
	 * hence this had to be coded
	 * 
	 * @param id
	 */
	public void setIdForClient(ID id) {
		setId(id);
	}
	
	
	/**
	 * A convenient toString method
	 */
	@Override
	public String toString() {
		return "AbstractUser [username=" + getUsername() + ", roles=" + roles + "]";
	}
	
	
	@Transient
	protected Collection<GrantedAuthority> authorities;
	
	/**
	 * Returns the authorities, for Spring Security
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		return authorities;
	}
	
	
	/**
	 * Computes the authorities for Spring Security, and stores
	 * those in the authorities transient field
	 */
	protected void computeAuthorities() {
		
		authorities = roles.stream()
			.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
			.collect(Collectors.toCollection(() ->
				new HashSet<GrantedAuthority>(roles.size() + 2))); 
		
		if (goodUser) {
			
			authorities.add(new SimpleGrantedAuthority("ROLE_" + LemonSecurityConfig.GOOD_USER));

			if (goodAdmin || goodDeveloper || goodAuditor || goodMonitor)
				authorities.add(new SimpleGrantedAuthority("ROLE_" + LemonSecurityConfig.GOOD_MONITOR));

			if (goodAdmin || goodDeveloper || goodAuditor)
				authorities.add(new SimpleGrantedAuthority("ROLE_" + LemonSecurityConfig.GOOD_AUDITOR));

			if (goodAdmin || goodDeveloper)
				authorities.add(new SimpleGrantedAuthority("ROLE_" + LemonSecurityConfig.GOOD_DEVELOPER));

			if (goodAdmin)
				authorities.add(new SimpleGrantedAuthority("ROLE_" + LemonSecurityConfig.GOOD_ADMIN));
		}

		log.debug("Authorities of " + this + ": " + authorities);
	}

	
	/**
	 * The following are needed
	 * because we have implemented UserDetails. 
	 */
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
