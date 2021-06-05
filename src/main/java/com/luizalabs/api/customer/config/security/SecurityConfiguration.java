package com.luizalabs.api.customer.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .httpBasic()
            .and()
            .csrf().disable()
            .formLogin().disable()
            .logout().disable();
    }

    @Override public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers( "/v2/api-docs", "/swagger-resources/**", "/configuration/ui","/configuration/security", "/swagger-ui.html");
    }
    
}
