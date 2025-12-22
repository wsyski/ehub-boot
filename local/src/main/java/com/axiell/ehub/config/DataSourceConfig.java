package com.axiell.ehub.config;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@PropertySource("classpath:persistence.properties")
public class DataSourceConfig {
    @Value("${connection.name}")
    private String name;

    @Value("${connection.username}")
    private String username;

    @Value("${connection.password}")
    private String password;

    @Value("${connection.url}")
    private String url;

    @Bean(name = "dataSource")
    public DataSource dataSource(
    ) throws SQLException {
        PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
        dataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setConnectionPoolName(name);
        dataSource.setMinPoolSize(8);
        dataSource.setMaxPoolSize(256);
        dataSource.setInactiveConnectionTimeout(60);
        dataSource.setAbandonedConnectionTimeout(30);
        dataSource.setValidateConnectionOnBorrow(true);
        dataSource.setSQLForValidateConnection("select 1 from dual");
        return dataSource;
    }
}
