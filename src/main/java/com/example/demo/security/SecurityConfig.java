package com.example.demo.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.CustomAuthorizationFilter;
import com.example.demo.filter.CustomerAuthentificationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	//private static final String GET = null;
	private UserDetailsService userDetailsService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		CustomerAuthentificationFilter customerAuthentificationFilter = new CustomerAuthentificationFilter(authenticationManagerBean());
		
		customerAuthentificationFilter.setFilterProcessesUrl("/api/login");
		
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(STATELESS);
		http.authorizeRequests().antMatchers("/aoi/login/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/**").hasAnyAuthority("USER");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/token/refresh/**").hasAnyAuthority("USER");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority("ADMIN");		

//		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/**").permitAll();		

//		http.authorizeRequests().anyRequest().permitAll();
		http.addFilter(new CustomerAuthentificationFilter(authenticationManager()));
		http.addFilter(customerAuthentificationFilter);
		http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}
	
	
	
	

}

