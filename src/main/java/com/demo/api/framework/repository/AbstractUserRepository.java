package com.demo.api.framework.repository;

import com.demo.api.framework.domain.AbstractUser;
import com.demo.api.framework.domain.AbstractUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

/**
 * Abstract UserRepository interface
 * 
 * @see <a href="http://stackoverflow.com/questions/27545276/how-to-implement-a-spring-data-repository-for-a-mappedsuperclass">how-to-implement-a-spring-data-repository-for-a-mappedsuperclass</a>
 * @author Sanjay Patel
 *
 * @param <U>	The User class
 * @param <ID>	The Primary key type of User class 
 */
@NoRepositoryBean
public abstract interface AbstractUserRepository
	<U extends AbstractUser<U,ID>, ID extends Serializable>
extends JpaRepository<U, ID> {
	Optional<U> findByEmail(String email);
	Optional<U> findByForgotPasswordCode(String forgotPasswordCode);
	Optional<U> findByVerificationCode(String verificationCode);
}
