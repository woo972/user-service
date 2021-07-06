package com.wowls.userservice.security;

import com.wowls.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncorder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 인증
        // select pwd                           // encrypt pwd
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncorder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 권한
        http.csrf().disable(); // https://swk3169.tistory.com/entry/Web-CSRFCross-Site-Request-Forgery-%EA%B3%B5%EA%B2%A9-%EA%B8%B0%EB%B2%95
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests().antMatchers("/**")
//                .hasIpAddress(environment.getProperty("gateway.ip"))
                .permitAll()
                .and()
                .addFilter(getAuthenticationFilter());
        http.headers().frameOptions().disable(); // h2 console 화면에 접근을 허용한다
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, environment);
//        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
//        authenticationFilter.setAuthenticationManager(authenticationManager()); // spring에서 제공하는 login 기능 사용을 위해 authenticationmanager를 사용
        return authenticationFilter;
    }



}

