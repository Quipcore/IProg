package database_connections;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private final static String DROP_ALL_TABLES = "SELECT CONCAT('DROP TABLE IF EXISTS `', TABLE_SCHEMA, '`.`', TABLE_NAME, '`;')\n" +
            "FROM information_schema.TABLES\n" +
            "WHERE TABLE_SCHEMA = 'mydb';";

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        List<String> passwords = Files.readAllLines(Path.of("assignment_3/password/dsv_database.txt")).stream().toList();
        String computer = passwords.get(0);
        String db_name = passwords.get(1);
        String user = passwords.get(2);
        String password = passwords.get(3);

        String url = "jdbc:mysql://" + computer + "/" + db_name;
        Connection dbConnetion = DriverManager.getConnection(url,user,password);

//        dropGuestBook(dbConnetion);
        createTable(dbConnetion);

        List<String> data = new ArrayList<>(List.of("Felix", "<feli8145@student.su.se>", "website.org", "Hello!"));
        insertIntoTable(dbConnetion, data);
        printGuestbook(dbConnetion);

    }

    private static void dropGuestBook(Connection con) throws SQLException {
        con.createStatement().execute("DROP TABLE Guestbook");
    }

    private static void dropAllTables(Connection con) throws SQLException {
        con.createStatement().execute(DROP_ALL_TABLES);
    }

    private static void insertIntoTable(Connection con, List<String> data) throws SQLException {
        if(data.size() % 4 != 0){
            throw new RuntimeException("Data amount incorrect!");
        }
        String values = "(" + "?,".repeat(Math.max(0, data.size() - 1)) + "?)";

        String query = "INSERT INTO Guestbook VALUES" + values + ";";

        PreparedStatement preparedStatement = con.prepareStatement(query);
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).matches("<.*>")){
                data.set(i, "CENSORED");
            }
            preparedStatement.setString(i+1, data.get(i));
        }
        preparedStatement.execute();

    }

    private static void printGuestbook(Connection connection) throws SQLException {
        String query = "SELECT DISTINCT * FROM Guestbook";
        printSet(connection.createStatement().executeQuery(query), "name","email","website","comment");
    }

    private static void createTable(Connection con) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Guestbook(" +
                            "name varchar(255)," +
                            "email varchar(255)," +
                            "website varchar(255)," +
                            "comment varchar(255)" +
                        ");";
        con.createStatement().execute(query);
    }

    private static void printSet(ResultSet rs, String... cols) throws SQLException {
        while (rs.next()) {
            for (String str : cols) {
                String result = rs.getString(str);
                System.out.printf("%s\t\t", result);
            }
            System.out.println();
        }
        System.out.println();
    }
}
