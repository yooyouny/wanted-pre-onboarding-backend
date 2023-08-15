package com.example.demo.configuration;

import com.example.demo.configuration.filter.JwtTokenFilter;
import com.example.demo.exception.CustomAuthenticationEntryPoint;
import com.example.demo.service.MemberDetailsService;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig{

	private final MemberDetailsService detailsService;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((authorizeRequests) ->
						authorizeRequests
								.requestMatchers("/", "/h2-console/**", "/api/*/members/register", "/api/*/members/login").permitAll()
								.requestMatchers("/api/**").authenticated()
				)
				.exceptionHandling((exceptionHandling) ->
						exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
				)
				.sessionManagement((sessionManagement) ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.csrf(csrf -> csrf
						.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
						.disable());

		http
				.addFilterBefore(new JwtTokenFilter(secretKey, detailsService), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
