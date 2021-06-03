package com.luizalabs.api.customer.config;

import java.io.IOException;

import javax.sql.DataSource;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan
public class DataSourceConfig {
    
    @Bean
    @Primary
    public DataSource inMemoryDataSource() throws IOException {
        return EmbeddedPostgres.builder().start().getPostgresDatabase();
    }
}
