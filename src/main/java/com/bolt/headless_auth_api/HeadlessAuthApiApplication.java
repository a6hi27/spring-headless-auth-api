package com.bolt.headless_auth_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class HeadlessAuthApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeadlessAuthApiApplication.class, args);
    }

}
