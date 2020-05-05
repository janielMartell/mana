package org.mana.db.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPDataSource implements DataSource {

    private static HikariConfig config;
    private static HikariDataSource dataSource;

    static {
        config = new HikariConfig("src/main/resources/application.properties");
        dataSource = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public HikariCPDataSource() {
    }
}
