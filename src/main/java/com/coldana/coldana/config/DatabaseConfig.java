package com.coldana.coldana.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    static {
        try {
            Properties props = new Properties();

            // Cari file di classpath (src/main/resources)
            InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("config.properties");

            if (input == null) {
                throw new RuntimeException("File config.properties tidak ditemukan di classpath!");
            }

            props.load(input);
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");

//            System.out.println("Database Props " + dbUrl + " " + dbUser + " " + dbPassword);

        } catch (Exception e) {
            System.err.println("Gagal membaca file konfigurasi: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC tidak ditemukan!", e);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal koneksi ke database!", e);
        }
    }

    public static String getDbUrl(){
        return dbUrl;
    }public static String getDbUser(){
        return dbUser;
    }public static String getDbPassword(){
        return dbPassword;
    }
}
