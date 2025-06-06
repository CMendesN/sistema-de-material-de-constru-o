package com.materialsystem.util;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String DB_IP = dotenv.get("DB_IP");
    private static final String DB_PORT = dotenv.get("DB_PORT");
    private static final String DB_DATABASE = dotenv.get("DB_DATABASE");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASS = dotenv.get("DB_PASS");

    private static final String URL = "jdbc:postgresql://" + DB_IP + ":" + DB_PORT + "/" + DB_DATABASE;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DB_USER, DB_PASS);
    }
}
    