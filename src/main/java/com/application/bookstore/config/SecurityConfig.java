package com.application.bookstore.config;

import com.application.bookstore.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

//    private static final String[] PUBLIC_URLS = {"/home"};
    private static final String[] H2_CONSOLE_WHITELIST = {"/h2-console/**"};
    private static final String[] SWAGGER_UI_WHITELIST = {"/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/swagger-resources/**", "/v3/api-docs/**"};

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.cors().disable();

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(HttpMethod.GET, "/api/book-store-service/v1/books").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/book-store-service/v1/books/{id}").permitAll()
//                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(SWAGGER_UI_WHITELIST).permitAll()
                .requestMatchers(H2_CONSOLE_WHITELIST).permitAll()
                .anyRequest().authenticated());

//        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    //    @Bean
//    public UserDetailsService userDetailService() {
//        return new UserService();
//    }



}
