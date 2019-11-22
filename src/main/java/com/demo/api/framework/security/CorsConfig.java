package com.demo.api.framework.security;

import com.demo.api.framework.LemonProperties;
import com.demo.api.framework.LemonProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
@ConditionalOnProperty(name="lemon.cors.allowedOrigins")
@ComponentScan("com.demo.api")
@Slf4j
public class CorsConfig extends WebMvcConfigurerAdapter {

	@Autowired
    LemonProperties properties;

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		log.debug("Configuring CORS");

		LemonProperties.Cors cors = properties.getCors();

		registry.addMapping("/**")
			.allowedOrigins(cors.getAllowedOrigins())
			.allowedMethods(cors.getAllowedMethods())
			.allowedHeaders(cors.getAllowedHeaders())
			.exposedHeaders(cors.getExposedHeaders())
			.maxAge(cors.getMaxAge())
			.allowCredentials(true);
	}

	// swagger Resource 정의
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/WEB-INF/resources/");

		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
