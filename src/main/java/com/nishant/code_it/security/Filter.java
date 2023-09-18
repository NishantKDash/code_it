package com.nishant.code_it.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class Filter {

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
  http.authorizeHttpRequests(authorize-> authorize.requestMatchers("/api/**" , "/user/register/admin" , "/user/logout" , "/user/student" , "/user/admin").authenticated().anyRequest().permitAll())
      .addFilterBefore(new JwtTokenValidator() , BasicAuthenticationFilter.class)
      .csrf(csrf -> csrf.disable())
      .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request)
			{
				CorsConfiguration cfg = new CorsConfiguration();
				cfg.setAllowedOrigins(Arrays.asList("http://localhost:3000/"));
				cfg.setAllowedMethods(Collections.singletonList("*"));
				cfg.setAllowCredentials(true);
				cfg.setAllowedHeaders(Collections.singletonList("*"));
				cfg.setExposedHeaders(Arrays.asList("Authorization"));
				cfg.setMaxAge(3600L);
				return cfg;
			}
		}))
       .formLogin(Customizer.withDefaults())
       .httpBasic(Customizer.withDefaults());
		return http.build();
	}
	
	@Bean
	public PasswordEncoder encoder()
	{
		return new BCryptPasswordEncoder();
	}
}
