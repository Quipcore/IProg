package database_connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();


        String url = "";
        String user = "";
        String password = "";

        Connection dbConnetion = DriverManager.getConnection(url,user,password);


    }
}
