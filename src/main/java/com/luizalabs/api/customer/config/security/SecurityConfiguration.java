package com.luizalabs.api.customer.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
            .antMatchers("/postgresql-console/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin();

        httpSecurity.csrf().ignoringAntMatchers("/postgresql-console/**");
        httpSecurity.headers().frameOptions().sameOrigin();
    }
    
}
