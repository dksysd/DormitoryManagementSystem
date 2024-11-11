package server.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseConnectionPool {
    private static final HikariDataSource DATASOURCE = new HikariDataSource(getConfig());

    public static Connection getConnection() throws SQLException {
        return DATASOURCE.getConnection();
    }

    public static void close() {
        DATASOURCE.close();
    }

    private static HikariConfig getConfig() {
        HikariConfig config = new HikariConfig();
        // db connection config
        config.setJdbcUrl(getJdbcUrl());
//        config.setUsername(ConfigLoader.get("DB_ADMIN"));
//        config.setPassword(ConfigLoader.get("DB_ADMIN_PASSWORD"));
        config.setUsername(ConfigLoader.get("DB_USER"));
        config.setPassword(ConfigLoader.get("DB_PASSWORD"));

        // jdbc connection config
        config.setMaximumPoolSize(ConfigLoader.getInt("DB_JDBC_MAX_POOL"));
        config.setConnectionTimeout(ConfigLoader.getInt("DB_JDBC_CONNECTION_TIMEOUT"));
        config.setIdleTimeout(ConfigLoader.getInt("DB_JDBC_IDLE_TIMEOUT"));
        config.setMaxLifetime(ConfigLoader.getInt("DB_JDBC_MAX_LIFETIME"));

        // cache config
        config.addDataSourceProperty("useServerPrepStmts", ConfigLoader.get("DB_JDBC_USE_SERVER_PREP_STMTS"));
        config.addDataSourceProperty("cachePrepStmts", ConfigLoader.get("DB_JDBC_CACHE_PREP_STMTS"));
        config.addDataSourceProperty("prepStmtCacheSize", ConfigLoader.get("DB_JDBC_PREP_STMT_CACHE_SIZE"));
        config.addDataSourceProperty("prepStmtCacheSqlLimit", ConfigLoader.get("DB_JDBC_PREP_STMT_CACHE_SQL_LIMIT"));
        config.addDataSourceProperty("useLocalSessionState", ConfigLoader.get("DB_JDBC_USE_LOCAL_SESSION_STATE"));
        config.addDataSourceProperty("rewriteBatchedStatements", ConfigLoader.get("DB_JDBC_REWRITE_BATCH_STMTS"));

        return config;
    }

    private static String getJdbcUrl() {
        String driver = ConfigLoader.get("DB_DRIVER");
        String dns = ConfigLoader.get("DB_DNS");
        String port = ConfigLoader.get("DB_PORT");
        String schema = ConfigLoader.get("DB_SCHEMA");
        return String.format("jdbc:%s://%s:%s/%s", driver, dns, port, schema);
    }
}
