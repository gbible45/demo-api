package com.demo.api.framework.domain;

import com.demo.api.framework.util.XpertUtil;
import com.demo.api.framework.util.XpertUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Needed for auto-filling of the
 * AbstractAuditable columns of AbstractUser
 *  
 * @author Sanjay Patel
 *
 * @param <U>	The User class
 * @param <ID>	The Primary key type of User class 
 */
@Component
public class AuditorAwareImpl
	<U extends AbstractUser<U,ID>,
	 ID extends Serializable>
implements AuditorAware<U> {

	@Override
	public U getCurrentAuditor() {
		return XpertUtil.getUser();
	}	
}
