package com.veciapp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class VeciAppApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VeciAppApiApplication.class, args);
    }
}
