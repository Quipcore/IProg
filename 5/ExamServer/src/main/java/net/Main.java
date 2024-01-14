package net;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Main {

    private static final int DEFAULT_PORT = 2000;
    private static final int MAX_CONNECTION = 1000;

    private static final int Port  = 587;
    private static final String SMTP  = "smtp.gmail.com";

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.startServer();
    }

    Main(){

    }

    public void startServer(){
        try(ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)){
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.printf("Created server on: %s and port: %d\n", hostAddress, serverSocket.getLocalPort());

            Connection dbConnection = connectToDatabase();
            createTables(dbConnection);

            Session emailSession = createEmailSession();

            while(true){
                if(ServerThread.connectionCount() <= MAX_CONNECTION){
                    new ServerThread(serverSocket.accept(),dbConnection,emailSession);
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private Session createEmailSession() throws IOException, URISyntaxException {
        Path credentialsPath = Path.of(Main.class.getResource("credentials").toURI());

        List<Pair<String,String>> credentials = Files.readAllLines(credentialsPath).stream()
                .filter(credential -> credential.startsWith("email-"))
                .map(credential -> credential.split("-")[1])
                .map(credential -> credential.split(":"))
                .map(credential -> new Pair<>(credential[0],credential[1]))
                .toList();


        String username = "";
        String password = "";

        for(Pair<String,String> pair: credentials){
            switch (pair.getKey()){
                case "address":
                    username = pair.getValue();
                    break;
                case "credit":
                    password = pair.getValue();
                    break;
            }
        }


        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP);
        properties.put("mail.smtp.port", Port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); //TLS

        String finalUsername = username;
        String finalPassword = password;
        return Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(finalUsername, finalPassword);
                    }
                });

    }

    private void createTables(Connection dbConnection) throws SQLException {

        //temp
//        dbConnection.createStatement().execute("DROP TABLE IF EXISTS chessgames");
        String createMainTable = "CREATE TABLE IF NOT EXISTS chessgames(" +
                "game_id INT AUTO_INCREMENT  PRIMARY KEY," +
                "player_white VARCHAR(255)," +
                "player_black VARCHAR(255)," +
                "fen VARCHAR(255),"+
                "result VARCHAR(10)," +
                "is_active BOOLEAN," +
                "game_date TIMESTAMP" +
                ")";
        dbConnection.createStatement().execute(createMainTable);

        //temp
        dbConnection.createStatement().execute("DROP TABLE IF EXISTS chessmoves");
        String createMoveTable = "CREATE TABLE IF NOT EXISTS chessmoves(" +
                "game_id INT PRIMARY KEY," +
                "turn INT," +
                "white_move VARCHAR(255)," +
                "black_move VARCHAR(255)" +
                ")";
        dbConnection.createStatement().execute(createMoveTable);

        String testData = "INSERT INTO chessmoves VALUES (2,1,'e4','e5')";
        dbConnection.createStatement().execute(testData);
    }

    private Connection connectToDatabase() throws IOException, URISyntaxException, SQLException {

        Path credentialsPath = Path.of(Main.class.getResource("credentials").toURI());

        List<Pair<String,String>> credentials = Files.readAllLines(credentialsPath).stream()
                .filter(credential -> credential.startsWith("db-"))
                .map(credential -> credential.split("-")[1])
                .map(credential -> credential.split(":"))
                .map(credential -> new Pair<>(credential[0],credential[1]))
                .toList();

        String host = "",db_name = "",user = "",password = "";
        for(Pair<String,String> pair: credentials){
            switch (pair.getKey()){
                case "host":
                    host = pair.getValue();
                    break;
                case "name":
                    db_name = pair.getValue();
                    break;
                case "user":
                    user = pair.getValue();
                    break;
                case "password":
                    password = pair.getValue();
                    break;
            }
        }

        String url = "jdbc:mysql://" + host + "/" + db_name;
        return DriverManager.getConnection(url,user,password);
    }

    private void printSet(ResultSet rs, String... cols) throws SQLException {
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