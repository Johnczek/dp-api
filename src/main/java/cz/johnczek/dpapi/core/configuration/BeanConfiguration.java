package cz.johnczek.dpapi.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class BeanConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {

                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");

//                String[] allowedOriginsArray = Arrays.stream("http://localhost:4200,https://johnczek.eu,http://johnczek.eu,https://holidaywatch.eu,http://holidaywatch.eu".split(","))
//                        .toArray(String[]::new);
//
//                registry.addMapping("/**")
//                        .allowedOrigins(allowedOriginsArray)
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
//                        .allowedHeaders("*")
//                        .allowCredentials(true);
            }
        };
    }
}
