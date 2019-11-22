package com.demo.api;

import com.demo.api.framework.security.LemonSecurityConfig;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.apache.directory.api.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Filter;
import java.nio.charset.Charset;

import static springfox.documentation.builders.PathSelectors.ant;

@Configuration
@EntityScan("com.crossent.demo")
@ComponentScan("com.crossent.demo")
@EnableSwagger2
public class ApplicationConfig extends LemonSecurityConfig {

    @Value("${autoapi.swagger.host:}")
    private String swaggerHost;

    @Value("${autoapi.swagger.pathMapping:}")
    private String swaggerPathMapping;

	@Value("${autoapi.swagger.basePackage:com.demo.api}")
	private String swaggerbasePackage;

    @Value("${autoapi.swagger.basePath:/api/**}")
    private String swaggerbasePath;

    // SWAGGER
    @Bean
    public Docket api() {
	    String[] swaggerbasePackages = swaggerbasePackage.split(",");
        String[] swaggerbasePaths = swaggerbasePath.split(",");
        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2).select();

        Predicate[] packagePredicates = new Predicate[swaggerbasePackages.length];
        for (int i=0; i<swaggerbasePackages.length; i++) {
            packagePredicates[i] = RequestHandlerSelectors.basePackage(swaggerbasePackages[i]);
        }
        builder.apis(Predicates.or(packagePredicates));

        Predicate[] pathPredicates = new Predicate[swaggerbasePaths.length];
        for (int i=0; i<swaggerbasePaths.length; i++) {
            pathPredicates[i] = Predicates.and(ant(swaggerbasePaths[i]));
        }
        builder.paths(Predicates.or(pathPredicates));

	    Docket docket = builder.build();
        if (Strings.isNotEmpty(swaggerHost) || Strings.isNotEmpty(swaggerPathMapping)) {
            String hostPath = "";
            if (Strings.isNotEmpty(swaggerHost)) {
                hostPath = swaggerHost;
            }
            if (Strings.isNotEmpty(swaggerPathMapping)) {
                hostPath += swaggerPathMapping;
                //docket.pathMapping(swaggerPathMapping);
            }
            docket.host(hostPath);
        }
        return docket;
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

}
