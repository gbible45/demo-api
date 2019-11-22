package com.demo.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackageClasses = {DemoApplication.class})
@EnableAutoConfiguration//(exclude={SecurityAutoConfiguration.class})
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class DemoApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        try {
            SpringApplication.run(DemoApplication.class, args);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
