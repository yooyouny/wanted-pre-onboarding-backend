package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthenticationConfig{

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeRequests((authorizeRequests) ->
						authorizeRequests
								.requestMatchers("/", "/auth/**", "/api/*/members/register", "/api/*/members/login").permitAll()
								.requestMatchers("/api/**").authenticated()
				)
				.sessionManagement((sessionManagement) ->
								sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);

		return http.build();
	}
}
