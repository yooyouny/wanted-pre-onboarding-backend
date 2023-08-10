package com.example.demo.configuration;

import com.example.demo.configuration.filter.JwtTokenFilter;
import com.example.demo.exception.CustomAuthenticationEntryPoint;
import com.example.demo.service.MemberDetailsService;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig{

	private final MemberDetailsService detailsService;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Bean
	public WebSecurityCustomizer configure() {
		return (web) -> web.ignoring().requestMatchers("^(?!/api/).*");
	}
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
				)
				.exceptionHandling((exceptionHandling) ->
								exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
				)
				.addFilterBefore(new JwtTokenFilter(secretKey, detailsService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
