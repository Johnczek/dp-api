package cz.johnczek.dpapi.core.security;

import cz.johnczek.dpapi.core.security.jwt.AuthEntryPointJwt;
import cz.johnczek.dpapi.core.security.jwt.AuthTokenFilter;
import cz.johnczek.dpapi.user.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userService;

    private final AuthEntryPointJwt unauthorizedHandler;

    private final PasswordEncoder passwordEncoder;

    @Value("${system.cors.allowedOrigins:http://localhost:4200,https://johnczek.eu,http://johnczek.eu,https://holidaywatch.eu,http://holidaywatch.eu}")
    private String allowedOrigins;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // TODO change configuration
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                    .antMatchers("/user/logged-user").permitAll()
                    .antMatchers("/user/login").permitAll()
                    .antMatchers("/user/register").permitAll()
                    .antMatchers(HttpMethod.GET, "/user/{userId}").permitAll()
                    .antMatchers(HttpMethod.GET, "/item/{id}").permitAll()
                    .antMatchers(HttpMethod.GET, "/item").permitAll()
                    .antMatchers(HttpMethod.GET, "/item/seller/{sellerId}").permitAll()
                    .antMatchers(HttpMethod.GET, "/file/{uuid}").permitAll()
                    .antMatchers(HttpMethod.GET, "/ws**").permitAll()
                    .antMatchers("/swagger-ui/**").permitAll()
                    .antMatchers("/v3/**").permitAll()
                    .antMatchers("/hello/unauthorized").permitAll()
                    .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOriginsArray = Arrays.stream(this.allowedOrigins.split(",")).toArray(String[]::new);

        registry.addMapping("/**")
                .allowedOrigins(allowedOriginsArray)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
