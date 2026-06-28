package com.productivitytracker.config;

/**
 * Database configuration facade used by JDBC infrastructure.
 */
public final class DatabaseConfig {

    private DatabaseConfig() {
        throw new AssertionError("DatabaseConfig should not be instantiated");
    }

    public static String driverClassName() {
        String url = url();
        String driver = AppConfig.get("db.driver", null);
        
        // If driver is not explicitly configured, infer from URL
        if (driver == null || driver.isBlank()) {
            if (url.startsWith("jdbc:sqlite:")) {
                return "org.sqlite.JDBC";
            } else if (url.startsWith("jdbc:mysql:")) {
                return "com.mysql.cj.jdbc.Driver";
            }
        }
        
        return driver != null ? driver : "com.mysql.cj.jdbc.Driver";
    }

    public static String url() {
        return AppConfig.get(
                "db.url",
                "jdbc:mysql://localhost:3306/productivity_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        );
    }

    public static String username() {
        return AppConfig.get("db.user", "root");
    }

    public static String password() {
        return AppConfig.get("db.password", "root@123");
    }
}
