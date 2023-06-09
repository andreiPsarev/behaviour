package com.fluffy.universe.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.sql.SQLException;

public final class DataSource {
    private static final HikariConfig dataSourceConfiguration = new HikariConfig();
    private static final Sql2o dataSource;

    private DataSource() {}

    static {
        dataSourceConfiguration.setJdbcUrl(String.format("jdbc:sqlite:/%s", Configuration.get("database.filename")));
        dataSource = new Sql2o(new HikariDataSource(dataSourceConfiguration));
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.open();
    }
}
