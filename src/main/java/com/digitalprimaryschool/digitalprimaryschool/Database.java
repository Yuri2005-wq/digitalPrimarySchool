package com.digitalprimaryschool.digitalprimaryschool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class Database {
        private static final String URL = "jdbc:sqlite:ecole.db";
        //metode qui retourn une connection sqlite
        public static Connection getConnexion() throws SQLException {
            return DriverManager.getConnection(URL);
        }
        public static void initializeDatabase(){
            try(Connection conn = getConnexion(); Statement stmt = conn.createStatement()){
                InputStream is = Database.class.getResourceAsStream("DB.sql");

                if (is == null){
                    System.err.println("Fichier DB.sql introuvables dans les ressources");
                    return;
                }
                String sqlScript = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
                stmt.executeUpdate(sqlScript);
                System.out.println("base de donnée initialisée avec succées");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
}
