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

public class DataBase{
    private static final String url = "mysql://ensisa-gr5.yj.fr/ensiamve_confined";
    private static final String login = "ensiamve_admin";
    private static final String password = "$VSfN=u,dw.zFWG&HT";

    private Connection connection = null;
    private PreparedStatement pstate = null;

    public DataBase(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver OK");
            this.connection = DriverManager.getConnection(url,login,password);
            System.out.println("Connexion OK");
            this.close();
        } catch (ClassNotFoundException | SQLException e) {
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
