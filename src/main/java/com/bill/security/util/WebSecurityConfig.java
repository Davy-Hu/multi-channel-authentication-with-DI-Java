package com.bill.security.util;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bill.security.authentication.IAuthenticate;
import com.bill.security.oauth.CustomOAuth2UserService;
import com.bill.security.oauth.OAuth2LoginSuccessHandler;
import com.bill.security.user.CustomUserDetailsService;
import com.bill.security.user.UserCredential;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private CustomOAuth2UserService oAuth2UserService;
	
	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder()).dataSource(dataSource)
				.usersByUsernameQuery("select email, password from users where email=?")
				.authoritiesByUsernameQuery("select email, role from users where email=?");
	}

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {	
		
		http.authorizeRequests()
		.antMatchers("/oauth2/**").permitAll()
		.antMatchers("/register").permitAll()
		.antMatchers("/process_register").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
            .loginPage("/login")   
            .usernameParameter("email")
            .permitAll()
            .defaultSuccessUrl("/")
        .and()
        .oauth2Login()
        	.loginPage("/login")
        	.userInfoEndpoint().userService(oAuth2UserService)
        	.and()
        	.successHandler(oAuth2LoginSuccessHandler)
        .and()
        .logout().permitAll()
        .and()
        .rememberMe().key("12345");
	}

	
}
//password:codejava