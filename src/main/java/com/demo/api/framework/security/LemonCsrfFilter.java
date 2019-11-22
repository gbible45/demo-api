package com.demo.api.framework.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for attaching the CSRF token as a cookie and a header.
 * 
 * @see <a href="https://spring.io/guides/tutorials/spring-security-and-angular-js/#_csrf_protection">CSRF protection</a>
 * 
 * @author Sanjay Patel
 */
@Slf4j
public class LemonCsrfFilter extends OncePerRequestFilter {
	
	// name of the cookie
	public static final String XSRF_TOKEN_HEADER_NAME = "X-CSRF-TOKEN";

	// name of the cookie
	public static final String XSRF_TOKEN_COOKIE_NAME = "XSRF-TOKEN";

	// name of the cookie
	public static final String SESSION_ID_NAME = "JSESSIONID";

	public static final String COOKIES_DEFAULE_PATH = "/";

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {

//		String referer = request.getHeader("Referer");
		String requestUri = request.getRequestURI();

		// Get csrf attribute from request
		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		// Or "_csrf" (See CSRFFilter.doFilterInternal).

		if (csrf != null) { // if csrf attribute was found

			String token = csrf.getToken();

			if (token != null) { // if there is a token

				// CORS requests can't see the cookie if domains are different,
				// even if httpOnly is false. So, let's add it as a header as well.
				response.addHeader(XSRF_TOKEN_HEADER_NAME, token);

				log.debug("Sending CSRF token to client: {}", token);
			}
		}

		// swagger 예외 처리
		if (requestUri.indexOf("/swagger-ui.html") == 0
				|| requestUri.indexOf("/webjars") == 0
				|| requestUri.indexOf("/swagger-resources") == 0
				|| requestUri.indexOf("/v2/api-docs") == 0
				) {
			filterChain.doFilter(request, response);
			return;
		}

		filterChain.doFilter(request, response);
	}
}
