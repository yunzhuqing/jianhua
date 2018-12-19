package com.platform.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@EnableAutoConfiguration
@ComponentScan
@ImportResource(locations = {"classpath:spring-mvc.xml"})
public class PlatformShopApplication {

    public static void main(String [] args) {
        try {
            ConfigurableApplicationContext applicationContext = SpringApplication.run(PlatformShopApplication.class);
            String [] names = applicationContext.getBeanDefinitionNames();
            for(String name:names) {
                System.out.println("name: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
