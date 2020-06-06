package ensisa.group5.confined.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ensisa.group5.confined.exceptions.DataBaseException;

/**
 * Author VALLERICH Vincent on 05-06-2020
 */

public class DataBase implements Runnable{
    private static final String url = "jdbc:mysql://fdb25.atspace.me:3306/3462862_confined";
    private static final String login = "3462862_confined";
    private static final String password = "42NNYZFJx@j5ZA3";

    private Connection connection = null;
    private PreparedStatement pstate = null;

    @Override
    public void run() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("Driver OK");
            connection = DriverManager.getConnection(url,login,password);
            System.out.println("Connect");
            close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ResultSet execute(String sql) throws DataBaseException {
        try {
            if (this.connection == null)
                throw new DataBaseException();
            else {
                pstate = connection.prepareStatement(sql);
                return pstate.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            connection.close();
            pstate.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
