package com.raiot.raiotprojects.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteDao {
    private Connection connection = null;

    public Connection getConnection() {
        if (connection == null) {
            try {
                File dbfile=new File(".");
                String url= "jdbc:sqlite:" + dbfile.getAbsolutePath() + "/src/main/database/riotprojects.sqlite";
                connection = DriverManager.getConnection(url);
                return connection;
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
                return connection;
            }
        } else {
            return connection;
        }
    }
}
