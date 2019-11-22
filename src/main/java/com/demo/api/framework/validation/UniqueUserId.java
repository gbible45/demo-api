package com.demo.api.framework.validation;

import com.demo.api.framework.domain.AbstractUser;
import com.demo.api.framework.domain.AbstractUser;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Constraint;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * Annotation for unique-email constraint,
 * ensuring that the given email id is not already
 * used by a user.  
 * 
 * @author Sanjay Patel
 */
@NotBlank(message = "{com.naturalprogrammer.spring.blank.email}")
@Size(min= AbstractUser.USRID_MIN, max=AbstractUser.USRID_MAX, message = "{com.invalid.userId.size}")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy= UniqueUserIdValidator.class)
public @interface UniqueUserId {
 
    String message() default "{com.duplicate.userId}";

    Class[] groups() default {};
    
    Class[] payload() default {};
}
