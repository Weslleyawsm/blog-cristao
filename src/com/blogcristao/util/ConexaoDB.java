package com.blogcristao.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    private static final String ENDPOINT = System.getenv("DB_HOST") != null
            ? System.getenv("DB_HOST")
            : "prancheta-db.cgz4mmcgy3ns.us-east-1.rds.amazonaws.com";

    private static final String PORTA = System.getenv("DB_PORT") != null
            ? System.getenv("DB_PORT")
            : "3306";

    private static final String DATABASE = System.getenv("DB_NAME") != null
            ? System.getenv("DB_NAME")
            : "blogcristao";

    private static final String USER = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER")
            : "admin";

    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null
            ? System.getenv("DB_PASSWORD")
            : "awsm1944";
    private static final String URL = "jdbc:mysql://" + ENDPOINT + ":" + PORTA + "/" + DATABASE
            + "?useSSL=true&serverTimezone=UTC";
    public static Connection getConexao() throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (ClassNotFoundException e){
            throw new SQLException("Driver MySQL não encontrado!", e);
        }
    }
}
