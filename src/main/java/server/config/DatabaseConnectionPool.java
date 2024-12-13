package server.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DatabaseConnectionPool 클래스는 데이터베이스 연결 풀을 관리하는 추상 클래스입니다.
 * HikariCP 라이브러리를 사용하여 효율적이고 신뢰할 수 있는 JDBC 커넥션 풀을 제공합니다.
 * 데이터베이스 연결을 설정, 획득 및 종료하는 기능을 포함합니다.
 */
public abstract class DatabaseConnectionPool {

    /**
     * HikariCP DataSource 인스턴스로, 연결 풀을 관리합니다.
     */
    private static final HikariDataSource DATASOURCE = new HikariDataSource(getConfig());

    /**
     * DatabaseConnectionPool을 초기화합니다.
     * 현재 이 함수는 비어 있으며, 별도의 초기화 작업이 필요한 경우 구현할 수 있습니다.
     */
    public static void load() {
    }

    /**
     * 연결 풀에서 새로운 {@link java.sql.Connection} 객체를 획득합니다.
     *
     * @return 사용 가능한 데이터베이스 연결
     * @throws SQLException 연결 획득 실패 시 발생
     */
    public static Connection getConnection() throws SQLException {
        return DATASOURCE.getConnection();
    }

    /**
     * 데이터베이스 연결 풀을 종료하고, 모든 연결을 반환합니다.
     * 프로그램 종료 시 호출해야 합니다.
     */
    public static void close() {
        DATASOURCE.close();
    }

    /**
     * HikariCP 설정을 구성하여 {@link HikariConfig} 객체를 반환합니다.
     * 환경 변수에서 JDBC 설정을 읽어와 데이터베이스 연결 정보를 구성합니다.
     *
     * @return HikariCP 설정 객체
     */
    private static HikariConfig getConfig() {
        HikariConfig config = new HikariConfig();

        // 데이터베이스 연결 정보 설정
        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(Config.get("DB_USER"));
        config.setPassword(Config.get("DB_PASSWORD"));

        // JDBC 연결 설정
        config.setMaximumPoolSize(Config.getInt("DB_JDBC_MAX_POOL"));
        config.setConnectionTimeout(Config.getInt("DB_JDBC_CONNECTION_TIMEOUT"));
        config.setIdleTimeout(Config.getInt("DB_JDBC_IDLE_TIMEOUT"));
        config.setMaxLifetime(Config.getInt("DB_JDBC_MAX_LIFETIME"));

        // 캐시 관련 설정
        config.addDataSourceProperty("useServerPrepStmts", Config.get("DB_JDBC_USE_SERVER_PREP_STMTS"));
        config.addDataSourceProperty("cachePrepStmts", Config.get("DB_JDBC_CACHE_PREP_STMTS"));
        config.addDataSourceProperty("prepStmtCacheSize", Config.get("DB_JDBC_PREP_STMT_CACHE_SIZE"));
        config.addDataSourceProperty("prepStmtCacheSqlLimit", Config.get("DB_JDBC_PREP_STMT_CACHE_SQL_LIMIT"));
        config.addDataSourceProperty("useLocalSessionState", Config.get("DB_JDBC_USE_LOCAL_SESSION_STATE"));
        config.addDataSourceProperty("rewriteBatchedStatements", Config.get("DB_JDBC_REWRITE_BATCH_STMTS"));

        return config;
    }

    /**
     * JDBC URL을 생성하여 반환합니다.
     * 데이터베이스 드라이버, DNS, 포트, 스키마 정보를 바탕으로 URL을 구성합니다.
     *
     * @return 데이터베이스 연결 문자열 (JDBC URL)
     */
    private static String getJdbcUrl() {
        String driver = Config.get("DB_DRIVER");
        String dns = Config.get("DB_DNS");
        String port = Config.get("DB_PORT");
        String schema = Config.get("DB_SCHEMA");
        return String.format("jdbc:%s://%s:%s/%s", driver, dns, port, schema);
    }
}