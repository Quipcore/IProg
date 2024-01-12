package net;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ServerThread extends Thread{
    private final Socket client;
    private final PrintWriter out;
    private final BufferedReader in;

    private final Connection connection;
    private final Session emailSession;

    private static Set<ServerThread> threadPool;

    private final static String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public ServerThread(Socket client, Connection connection, Session emailSession) throws IOException {
        this.client = client;
        this.connection = connection;
        this.emailSession = emailSession;
        printClientInfo(client);
        if(threadPool == null){
            threadPool = Collections.synchronizedSet(new HashSet<>());
        }
        threadPool.add(this);
        this.out = new PrintWriter(client.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        start();
    }

    public static int connectionCount() {
        if(threadPool == null){
            return 0;
        }
        return threadPool.size();
    }

    private void printClientInfo(Socket client) {
        System.out.println(client.getInetAddress());
    }

    @Override
    public void run() {
        try {
            communicate(client);
            System.out.printf("Client disconnected from: %s and port: %d\n", client.getInetAddress(), client.getPort());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.remove(this);
            System.out.println("Removed "  + this);
        }
    }

    private void communicate(Socket clientSocket) throws IOException, JDOMException, SQLException, MessagingException {
        while(!clientSocket.isClosed()){
            String message = this.in.readLine();
            System.out.printf("<Client> %s\n",message);

            String response = respondToMessage(generateXMLFromString(message));
            this.out.println(response);

            if(message.equals("exit")){
                clientSocket.close();
                return;
            }
        }
    }

    private Document generateXMLFromString(String message) throws IOException, JDOMException {
        StringReader charStream = new StringReader(message);
        return new SAXBuilder().build(charStream);
    }

    private String respondToMessage(Document document) throws SQLException, MessagingException {

        Element header = getChildElement(document.getRootElement(), "header");
        Element protocol = getChildElement(header,"protocol");
        String command = getChildElement(protocol,"command").getContent(0).getValue();

        ProtocolBuilder protocolBuilder = new ProtocolBuilder("message")
                .addHeader("server","","RESPONSE").addBody();

        System.out.println(protocolBuilder);

        switch (command){
            case "ACTIVE" -> getActiveRespons(document, protocolBuilder);
            case "HISTORY" -> getUserHistory(document, protocolBuilder);
            case "BEGIN","START" -> startGame(document, protocolBuilder);
            case "MOVE" -> getMoves(document, protocolBuilder);
            case null, default -> getUnkownResponse();
        }

        System.out.println(protocolBuilder);
        return protocolBuilder.closeTag().toString();
    }

    private void getMoves(Document document, ProtocolBuilder protocolBuilder) throws SQLException {
        String matchId = document.getRootElement()
                .getChild("body")
                .getChild("user")
                .getChild("id")
                .getValue();

        String query = "SELECT * FROM `chessmoves` WHERE game_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,matchId);

        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            int gameId = resultSet.getInt("game_id");
            int turn = resultSet.getInt("turn");
            String whiteMove = resultSet.getString("white_move");
            String blackMove = resultSet.getString("black_move");

            System.out.printf("%d, %d, %s, %s\n",gameId,turn,whiteMove,blackMove);
            protocolBuilder.addMove(gameId, turn, whiteMove, blackMove);
        }
    }

    private void startGame(Document document, ProtocolBuilder protocolBuilder) throws SQLException, MessagingException {

        Element body = document.getRootElement().getChild("body");

        Element challenger = body.getChild("challenger");
        String challengerUsername = challenger.getChild("username").getValue();
        String challengerEmail = challenger.getChild("email").getValue();

        Element opponent = body.getChild("opponent");
        String opponentUsername = opponent.getChild("username").getValue();
        String opponentEmail = challenger.getChild("email").getValue();

        String preppedQuery = "INSERT INTO chessgames (player_white, player_black, fen, result, is_active, game_date)" +
                " VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(preppedQuery);
        preparedStatement.setString(1, challengerUsername);
        preparedStatement.setString(2, opponentUsername);
        preparedStatement.setString(3,START_FEN);
        preparedStatement.setNull(4, Types.VARCHAR); //Will set null
        preparedStatement.setBoolean(5,true);
        preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

        if(preparedStatement.execute()){
            sendEmailTo(opponentUsername, challengerUsername,opponentEmail,challengerEmail);
        }

    }

    private void sendEmailTo(String oppoent, String challenger, String... emails) throws MessagingException {
        Message message = new MimeMessage(emailSession);

        InternetAddress[] addresses = Arrays.stream(emails)
                .map(email -> {
                    try {
                        return new InternetAddress(email);
                    } catch (AddressException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(InternetAddress[]::new);

        message.setRecipients(Message.RecipientType.TO, addresses);
        message.setSubject("New chess game avaiable");

        String text = String.format("There's a new match avaible between \n %s(%s) vs %s(%s)",emails[0],oppoent,emails[1],challenger);
        message.setText(text);

        Transport.send(message);
    }

    private void getUserHistory(Document document, ProtocolBuilder protocolBuilder) throws SQLException {

        String user = document.getRootElement().getChild("body").getChild("user").getValue();

        String sqlQuery = "SELECT * FROM chessgames WHERE player_white = ? OR player_black = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1,user);
        preparedStatement.setString(2,user);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            int gameId = resultSet.getInt("game_id");
            String playerWhite = resultSet.getString("player_white");
            String playerBlack = resultSet.getString("player_black");
            String fenString = resultSet.getString("fen");
            String result = resultSet.getString("result");
            boolean isActive = resultSet.getBoolean("is_active");
            Timestamp game_date = resultSet.getTimestamp("game_date");


            protocolBuilder.addGame(gameId,playerWhite,playerBlack,fenString,result,isActive,game_date);
        }
    }

    private void getUnkownResponse() {
        return;
    }

    private void getActiveRespons(Document document, ProtocolBuilder protocolBuilder) throws SQLException {
        String user = document.getRootElement().getChild("body").getChild("user").getValue();

        String sqlQuery = "SELECT * FROM chessgames WHERE is_active = '1' AND (player_white = ? OR player_black = ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1,user);
        preparedStatement.setString(2,user);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            int gameId = resultSet.getInt("game_id");
            String playerWhite = resultSet.getString("player_white");
            String playerBlack = resultSet.getString("player_black");
            String fenString = resultSet.getString("fen");
            String result = resultSet.getString("result");
            boolean isActive = resultSet.getBoolean("is_active");
            Timestamp game_date = resultSet.getTimestamp("game_date");


            protocolBuilder.addGame(gameId,playerWhite,playerBlack,fenString,result,isActive,game_date);
        }
    }

    private Element getChildElement(Element element, String name){
        return element.getContent().stream()
                .filter(content -> content instanceof Element)
                .map(content -> (Element)content)
                .filter(elem -> elem.getName().equals(name))
                .findFirst()
                .get();
    }

    private void sendMessage(String message) {
        this.out.println(message);
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
