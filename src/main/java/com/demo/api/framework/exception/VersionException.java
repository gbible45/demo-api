package com.demo.api.framework.exception;

import com.demo.api.framework.util.XpertUtil;
import com.demo.api.framework.util.XpertUtil;

/**
 * Version exception, to be thrown when concurrent
 * updates of an entity is noticed. 
 * 
 * @author Sanjay Patel
 */
public class VersionException extends RuntimeException {

	private static final long serialVersionUID = 6020532846519363456L;
	
	public VersionException(String entityName) {
		super(XpertUtil.getMessage(
				"com.naturalprogrammer.spring.versionException", entityName));
	}
}
