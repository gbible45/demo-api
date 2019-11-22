package com.demo.api.framework.security;


import com.demo.api.framework.LemonProperties;
import com.demo.api.framework.LemonProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * A filter to facilitate CORS handling.
 * To disable this (e.g. while testing or in non-browser apps),
 * in your application.properties, don't provide
 * the <code>lemon.cors.allowedOrigins</code> property.
 * 
 * @author Sanjay Patel
 * @see <a href="https://spring.io/guides/gs/rest-service-cors/">Spring guide</a>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // needs to come first
@ConditionalOnProperty(name="lemon.cors.allowed-origins")
@Slf4j
public class LemonCorsFilter extends OncePerRequestFilter {

	protected LemonProperties properties;

	@Autowired
	public void setProperties(LemonProperties properties) {
		this.properties = properties;
	}

	protected static String springProfilesActive;

	@Autowired
	public void setSpringProfilesActive(@Value("${spring.profiles.actives:prod}") String springProfilesActive) {
		LemonCorsFilter.springProfilesActive = springProfilesActive;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if ("prod".equals(LemonCorsFilter.springProfilesActive.toLowerCase())) {
			log.debug("Inside LemonCorsFilter RequestURI: {} : {}", request.getMethod(), request.getRequestURI());
			if (request.getSession() != null) {
				log.debug("Inside LemonCsrfFilter SessionId: {} ", request.getSession().getId());
			}
			log.debug("Inside LemonCsrfFilter queryString: {}", request.getQueryString());
			if (request.getHeader("Content-Type") != null && request.getHeader("Content-Type").indexOf("multipart") == -1) {
                Enumeration<String> parameterNames = request.getParameterNames();
				if (parameterNames != null) {
				    String parameters = "";
                    while (parameterNames.hasMoreElements()) {
                        String name = parameterNames.nextElement();
                        String value = request.getParameter(name);
                        if (StringUtils.isNotEmpty(parameters)) parameters += ", ";
                        parameters += name + "=" + value;
                    }
					log.debug("Inside LemonCsrfFilter parameterMap: {}", parameters);
				}
			}
		}

		LemonProperties.Cors cors = properties.getCors();

		// origin as provided by the browser
		String origin = request.getHeader("Origin");

		log.debug("Inside LemonCorsFilter : Origin => " + origin);

		// host as provided by the browser
		String host = request.getHeader("Host");

		log.debug("Inside LemonCorsFilter : Host => " + host);

		// needed when $httpProvider.defaults.withCredentials = true;
		response.setHeader("Access-Control-Allow-Credentials", "true");

		if (cors.getAllowedOrigins() != null && cors.getAllowedOrigins().length == 1 && "*".equals(cors.getAllowedOrigins()[0]) && origin != null) {
			response.setHeader("Access-Control-Allow-Origin", origin);
		} else {
			if (StringUtils.isNotEmpty(origin) &&  Arrays.asList(cors.getAllowedOrigins()).indexOf(origin) >= 0) {
				response.setHeader("Access-Control-Allow-Origin", origin);
			}
		}

		// allowed methods
		response.setHeader("Access-Control-Allow-Methods",
			StringUtils.join(cors.getAllowedMethods(), ","));

		// allow headers
		response.setHeader("Access-Control-Allow-Headers",
			StringUtils.join(cors.getAllowedHeaders(), ","));

		// See http://stackoverflow.com/questions/25673089/why-is-access-control-expose-headers-needed#answer-25673446
		response.setHeader("Access-Control-Expose-Headers",
				StringUtils.join(cors.getExposedHeaders(), ","));

		// max age
		response.setHeader("Access-Control-Max-Age",
			Long.toString(cors.getMaxAge()));

		if (!request.getMethod().equals("OPTIONS")) {
			filterChain.doFilter(request, response);
		}
	}
}
