package com.wowls.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
        http.authorizeRequests().antMatchers("/users/**").permitAll();
//        http.authorizeRequests().antMatchers("/**")
//                .hasIpAddress("192.168.0.8")
//                .and()
//                .addFilter(getAuthenticationFilter());
        http.headers().frameOptions().disable(); // h2 console 화면에 접근을 허용한다
    }

    private AuthenticationFilter getAuthenticationFilter() {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
//        authenticationFilter.setAuthenticationManager(authenticationManager());
        return authenticationFilter;
    }
}
