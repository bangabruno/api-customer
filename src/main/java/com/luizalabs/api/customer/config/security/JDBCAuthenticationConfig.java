package com.luizalabs.api.customer.config.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JDBCAuthenticationConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
    throws Exception {
        auth.jdbcAuthentication()
        .passwordEncoder(this.passwordEncoder())
        .usersByUsernameQuery(" SELECT username, password, enabled FROM users WHERE username = ?")
        .authoritiesByUsernameQuery(
            " SELECT username, a.authority FROM authorities WHERE username = ?")
        .dataSource(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
