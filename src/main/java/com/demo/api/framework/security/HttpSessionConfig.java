package com.demo.api.framework.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@Configuration
public class HttpSessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName(LemonCsrfFilter.SESSION_ID_NAME);
        return serializer;
    }

    /**
     * Session 정보를 cookie 대신 HTTP header 에 저장
     * (REST API 요청을 위해)
     * @return HttpSessionStrategy
     */
    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }


    /**
     * springSessionRepositoryFilter 를 Servlet Container 에 적용
     */
    static class Initializer  extends AbstractHttpSessionApplicationInitializer {
        public Initializer() {
            super(HttpSessionConfig.class);
        }
    }

    /**
     * springSessionRepositoryFilter 를 Spring Security 에 적용
     */
    static class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
        public SecurityInitializer() {
            super(HttpSessionConfig.class);
        }
    }
}