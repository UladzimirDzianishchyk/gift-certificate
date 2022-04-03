package com.epam.esm.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
@PropertySource(value = {"classpath:jdbc.properties"})
@Profile("prod")
public class ProdConfig {

    @Autowired
    Environment env;


    private final String DRIVER = "jdbc.driverClassName";
    private final String URL = "jdbc.url";
    private final String USER = "jdbc.userName";
    private final String PASSWORD = "jdbc.password";
    private final String SIZE = "jdbc.initialSize";

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(env.getProperty(DRIVER));
        dataSource.setUrl(env.getProperty(URL));
        dataSource.setUsername(env.getProperty(USER));
        dataSource.setPassword(env.getProperty(PASSWORD));
        dataSource.setInitialSize(Integer.parseInt(env.getProperty(SIZE)));

        return dataSource;
    }


    @Bean
    public TransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }


    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
