package com.demo.api.framework.security;


import com.demo.api.framework.domain.AbstractUser;
import com.demo.api.framework.domain.VersionedEntity;
import com.demo.api.framework.util.XpertUtil;
import com.demo.api.framework.util.XpertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Needed to check the permission for the service methods
 * annotated with @PreAuthorize("hasPermission(...
 * 
 * @author Sanjay Patel
 *
 * @param <U>	The user class
 * @param <ID>	Primary key class, e.g. Long
 */
@Component
@Slf4j
public class LemonPermissionEvaluator
<U extends AbstractUser<U,ID>, ID extends Serializable>
implements PermissionEvaluator {

	/**
	 * Called by Spring Security to evaluate the permission
	 * 
	 * @param auth	Spring Security authentication object,
	 * 				from which the current-user can be found
	 * @param targetDomainObject	Object for which permission is being checked
	 * @param permission			What permission is being checked for, e.g. 'edit'
	 */
	@Override
	public boolean hasPermission(Authentication auth,
			Object targetDomainObject, Object permission) {
		
		log.debug("Checking whether " + auth
			+ "\n  has " + permission + " permission for "
			+ targetDomainObject);
		
		if (targetDomainObject == null)	// if no domain object is provided,
			return true;				// let's pass, allowing the service method
										// to throw a more sensible error message
		
		// Let's delegate to the entity's hasPermission method
		VersionedEntity<U, ID> entity = (VersionedEntity<U, ID>) targetDomainObject;
		return entity.hasPermission(XpertUtil.getUser(auth), (String) permission);
	}

	
	/**
	 * We need to override this method as well. To keep things simple,
	 * Let's not use this, throwing exception is someone uses it.
	 */
	@Override
	public boolean hasPermission(Authentication authentication,
			Serializable targetId, String targetType, Object permission) {
		
		throw new UnsupportedOperationException("hasPermission() by ID is not supported");		
	}

}
