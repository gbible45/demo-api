package com.demo.api.framework.validation;


import com.demo.api.framework.repository.AbstractUserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for unique-email
 * 
 * @author Sanjay Patel
 */
@Component
public class UniqueUserIdValidator
implements ConstraintValidator<UniqueUserId, String> {

	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private AbstractUserRepository<?,?> userRepository;
	
	@Override
	public void initialize(UniqueUserId constraintAnnotation) {
		log.debug("UniqueEmailValidator initialized");
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		log.debug("Validating whether email is unique: " + email);
		return !userRepository.findByEmail(email).isPresent();
	}

}
