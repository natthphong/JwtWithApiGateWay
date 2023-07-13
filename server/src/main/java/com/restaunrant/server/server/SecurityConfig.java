
package com.restaunrant.server.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(corsConfigurer-> corsConfigurer.disable())
                .csrf(csrfConfigurer -> csrfConfigurer.disable())
                .authorizeHttpRequests(auth-> {
                    auth.requestMatchers("/**").permitAll();
                    auth.requestMatchers("/eureka/**").permitAll();
                    auth.anyRequest().authenticated();
                });
        return httpSecurity.build();
    }
}
