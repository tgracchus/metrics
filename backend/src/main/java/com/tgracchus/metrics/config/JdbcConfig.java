package com.tgracchus.metrics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    @Value(value = "${jdbc.host}")
    private String host;

    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        var connUrl = String.format("jdbc:postgresql://%s:5432/timeseries", host);
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(connUrl);
        dataSource.setUsername("timemaster");
        dataSource.setPassword("password");

        return dataSource;
    }
}
