package com.demo.api.framework.exception;


import com.demo.api.framework.util.XpertUtil;
import com.demo.api.framework.validation.FieldError;
import com.demo.api.framework.util.XpertUtil;
import com.demo.api.framework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * An exception class which can contain multiple errors.
 * Used for validation, in service classes.
 * 
 * @author Sanjay Patel
 */
public class MultiErrorException extends RuntimeException {

	private static final long serialVersionUID = 6020532846519363456L;
	
	// list of errors
	private List<FieldError> errors = new ArrayList<FieldError>(10);
	
	public List<FieldError> getErrors() {
		return errors;
	}
	
	
	/**
	 * Overrides the standard getMessage
	 */
	@Override
	public String getMessage() {

		if (errors.size() == 0)
			return null;
		
		// return the first message
		return errors.get(0).getMessage();
	}
	
	
	/**
	 * Adds a global-error if the given condition isn't true
	 * 
	 * @param valid			the given condition
	 * @param messageKey	message key
	 * @param args			optional message arguments
	 * 
	 * @return				the exception object
	 */
	public MultiErrorException check(boolean valid,
			String messageKey, Object... args) {
		
		// delegate
		return check(null, valid, messageKey, args);
	}


	/**
	 * Adds a field-error if the given condition isn't true
	 * 
	 * @param fieldName		the name of the associated field
	 * @param valid			the given condition
	 * @param messageKey	message key
	 * @param args			optional message arguments
	 * 
	 * @return				the exception object
	 */
	public MultiErrorException check(String fieldName, boolean valid,
			String messageKey, Object... args) {
		
		if (!valid)
			errors.add(new FieldError(fieldName, messageKey,
				XpertUtil.getMessage(messageKey, args)));
			
		return this;
	}
	
	
	/**
	 * Factory method for a field-level error
	 * 
	 * @param fieldName		the name of the associated field
	 * @param messageKey	message key
	 * @param args			optional message arguments
	 * 
	 * @return				the exception object
	 */
	public static MultiErrorException of(String fieldName, 
			String messageKey, Object... args) {
		
		MultiErrorException exception = new MultiErrorException();
		exception.errors.add(new FieldError(fieldName, messageKey,
				XpertUtil.getMessage(messageKey, args)));
		return exception;
	}
	
	
	/**
	 * Factory method for a global-level error
	 * 
	 * @param messageKey	message key
	 * @param args			optional message arguments
	 * 
	 * @return				the exception object
	 */
	public static MultiErrorException of(String messageKey, Object... args) {
		
		return MultiErrorException.of(null, messageKey, args);
	}
	
	
	/**
	 * Throws the exception, if there are accumulated errors
	 */
	public void go() {
		if (errors.size() > 0)
			throw this;
	}

}
