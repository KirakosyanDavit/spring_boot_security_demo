package ru.itmentor.spring_boot_security_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import ru.itmentor.spring_boot_security_demo.service.impl.ApiServiceImpl;

@SpringBootApplication
public class SpringBootSecurityDemoApplication{

    private static final ApiServiceImpl apiService = new ApiServiceImpl(new RestTemplate());

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
        apiService.performApiOperations();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
