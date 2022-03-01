package com.tgracchus.metrics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {
    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        var connUrl = "jdbc:postgresql://localhost:5432/timeseries";
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(connUrl);
        dataSource.setUsername("timemaster");
        dataSource.setPassword("password");

        return dataSource;
    }
}
