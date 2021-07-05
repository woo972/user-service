package com.wowls.userservice.security;

import com.wowls.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncorder;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 권한
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests().antMatchers("/**")
                .hasIpAddress("192.168.0.8")
                .and()
                .addFilter(getAuthenticationFilter());
        http.headers().frameOptions().disable(); // h2 console 화면에 접근을 허용한다
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, environment);
//        authenticationFilter.setAuthenticationManager(authenticationManager()); // spring에서 제공하는 login 기능 사용을 위해 authenticationmanager를 사용
        return authenticationFilter;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 인증
        // select pwd                           // encrypt pwd
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncorder);
    }
}

/*
AuthenticationManager
Attempts to authenticate the passed Authentication object, returning a fully populated Authentication object (including granted authorities) if successful.
An AuthenticationManager must honour the following contract concerning exceptions:
A DisabledException must be thrown if an account is disabled and the AuthenticationManager can test for this state.
A LockedException must be thrown if an account is locked and the AuthenticationManager can test for account locking.
A BadCredentialsException must be thrown if incorrect credentials are presented. Whilst the above exceptions are optional, an AuthenticationManager must always test credentials.
 */
