package com.demo.api.framework.security;

import com.demo.api.framework.LemonService;
import com.demo.api.framework.domain.AbstractUser;
import com.demo.api.framework.LemonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication success handler for sending the response
 * to the client after successful authentication. This would replace
 * the default handler of Spring Security
 * 
 * @author Sanjay Patel
 */
@Component
@Slf4j
public class AuthenticationSuccessHandler
	extends SimpleUrlAuthenticationSuccessHandler {
	
    private ObjectMapper objectMapper;
    private LemonService<?,?> lemonService;
    
	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Autowired
	public void setLemonService(LemonService<?, ?> lemonService) {
		this.lemonService = lemonService;
	}

	@Override
    public void onAuthenticationSuccess(HttpServletRequest request,
    		HttpServletResponse response,
            Authentication authentication)
    throws IOException, ServletException {

        // Instead of handle(request, response, authentication),
		// the statements below are introduced
    	response.setStatus(HttpServletResponse.SC_OK);
    	response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		// get the current-user
    	AbstractUser<?,?> currentUser = lemonService.userForClient();
		String password = request.getParameterMap().get("password")[0];
		lemonService.loginPostProcess(password);

    	// Patched: write current-user data to the response
    	response.getWriter().print(objectMapper.writeValueAsString(currentUser));

    	// response.getOutputStream().print(
    	// objectMapper.writeValueAsString(currentUser));

    	// as done in the base class
    	clearAuthenticationAttributes(request);
        
        log.debug("Authentication succeeded for user: " + currentUser);
    }
}
