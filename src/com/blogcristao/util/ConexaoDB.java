package com.blogcristao.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    private static final String ENDPOINT = "prancheta-db.cgz4mmcgy3ns.us-east-1.rds.amazonaws.com";
    private static final String PORTA = "3306";
    private static final String DATABASE = "blogcristao";
    private static final String USER = "admin";
    private static final String PASSWORD = "awsm1944";

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
