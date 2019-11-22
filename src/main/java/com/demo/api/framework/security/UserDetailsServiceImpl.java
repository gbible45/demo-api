package com.demo.api.framework.security;


import com.demo.api.framework.domain.AbstractUser;
import com.demo.api.framework.repository.AbstractUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

/**
 * UserDetailsService, as required by Spring Security.
 * If this implementation does not meet your requirement,
 * just provide your own. Providing the property
 * lemon.enabled.user-details-service: false will
 * suppress this configuration. 
 * 
 * @author Sanjay Patel
 *
 * @param <U>	The user class
 * @param <ID>	Primary key class, e.g. Long
 */
@Service
@ConditionalOnProperty(name="lemon.enabled.user-details-service", matchIfMissing=true)
@Slf4j
public class UserDetailsServiceImpl
	<U extends AbstractUser<U,ID>, ID extends Serializable>
implements UserDetailsService {

	protected AbstractUserRepository<U,ID> abstractUserRepository;
	
	@Autowired
	public void setAbstractUserRepository(AbstractUserRepository<U, ID> abstractUserRepository) {
		this.abstractUserRepository = abstractUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		log.debug("Loading user having username: " + username);
		
		// delegates to findUserByUsername
		U user = findUserByEmail(username)
			.orElseThrow(() -> new UsernameNotFoundException(username));

		// deregistered user 처리
		if(user.hasRole(AbstractUser.Role.DEREGISTERED) ||
				user.hasRole(AbstractUser.Role.BLOCKED) ||
				user.hasRole(AbstractUser.Role.UNVERIFIED))
			throw new UsernameNotFoundException(username);

		user.decorate(user);
		
		log.debug("Loaded user having username: " + user);
		
		return user;
	}

	/**
	 * Finds a user by the given username. Override this
	 * if you aren't using email as the username.
	 * 
	 * @param email
	 * @return
	 */
	protected Optional<U> findUserByEmail(String email) {
		return abstractUserRepository.findByEmail(email);
	}
}
