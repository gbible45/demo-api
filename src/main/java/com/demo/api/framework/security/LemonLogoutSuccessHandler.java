package com.demo.api.framework.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logout success handler for sending the response
 * to the client after successful logout. This would replace
 * the default handler of Spring Security that redirects the user
 * to the login page.
 * 
 * @author Sanjay Patel
 */
@Component
@Slf4j
public class LemonLogoutSuccessHandler
	implements LogoutSuccessHandler {

    @Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

    	response.setStatus(HttpServletResponse.SC_OK);
    	log.debug("Logout succeeded.");
	}

}
