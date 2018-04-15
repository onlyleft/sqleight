package com.onlyleft.sqleight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatasourceTestFactory {
    private static String dbURL = "jdbc:derby:memory:testDB;create=true";

    static Connection getConnection() {
        try {
            return DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect", e);
        }
    }

    static void setupTables(Connection connection) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE Person (name varchar(30), age int not null)");

            stmt.executeUpdate("insert into Person (name, age) values ('Mary', 42)");
            stmt.executeUpdate("insert into Person (name, age) values ('Peter', 31)");
        } catch (SQLException e) {
            throw new RuntimeException("Bad query", e);
        }
    }
}
