package com.lleviy.auth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AuthApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
