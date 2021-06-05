package com.luizalabs.api.customer.config.datasource;

import java.io.IOException;

import javax.sql.DataSource;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

@SpringBootConfiguration
@ComponentScan
public class DataSourceConfig {
    
    @Bean
    @Primary
    public DataSource inMemoryDataSource() throws IOException {
        return EmbeddedPostgres.builder().start().getPostgresDatabase();
    }
}
