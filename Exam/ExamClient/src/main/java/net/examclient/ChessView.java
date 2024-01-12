package net.examclient;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class ChessView {

    @FXML
    private TextField addressField;

    private BufferedReader receiver;
    private PrintWriter sender;

    private String username = "";
    private String email = "";

    private static final String  CHESS_NOTATION_REGEX = "(O-O|O-O-O|[NBRQK]?[a-h]?[1-8]?[x-]?[a-h][1-8][=#QNR]?[+#]?)";

    public void setSocket(Socket socket) throws IOException {
        receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        sender = new PrintWriter(socket.getOutputStream(),true);

        addressField.setText(String.valueOf(socket.getInetAddress()));
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void displayActiveMatches(ActionEvent actionEvent) throws IOException, JDOMException, URISyntaxException {

        TextInputDialog getUserDialogBox = new TextInputDialog();
        getUserDialogBox.setTitle("ACTIVE");
        getUserDialogBox.setHeaderText("");
        getUserDialogBox.setContentText("User:");
        String user = getUserDialogBox.showAndWait().orElse(null);

        String activeXML = ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE message>" +
                "<message>" +
                "<header>" +
                "<protocol>" +
                "<type/>" +
                "<version>1.0</version>" +
                "<command>ACTIVE</command>" +
                "</protocol>" +
                "<id>" +
                "<username>%s</username>" +
                "<email>%s</email>" +
                "</id>" +
                "</header>" +
                "<body>" +
                "<user>" +
                "<username>%s</username>" +
                "</user>" +
                "</body>" +
                "</message>").formatted(this.username, this.email,user);

        sender.println(activeXML);

        String message = receiver.readLine();
        System.out.println(message);
        StringReader charStream = new StringReader(message);
        Document doc = new SAXBuilder().build(charStream);

        List<ChessGame> gamesFromXML = doc.getRootElement()
                .getChild("body")
                .getChildren("game")
                .stream()
                .map(element -> {
                    String id = element.getChild("id").getValue();
                    String player_white = element.getChild("player_white").getValue();
                    String player_black = element.getChild("player_black").getValue();
                    String fen = element.getChild("fen").getValue();
                    String is_active = element.getChild("is_active").getValue();
                    String time = element.getChild("time").getValue();
                    return new ChessGame(id,player_white,player_black,fen,is_active,time);
                }).toList();

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Games");
        dialog.setHeaderText("");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);

        ListView<String> listView = new ListView<>(FXCollections.observableList(gamesFromXML.stream().map(ChessGame::toString).collect(Collectors.toList())));
        listView.setEditable(false);
        dialog.getDialogPane().setContent(listView);
        AtomicBoolean isCanceled = new AtomicBoolean(false);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData().isDefaultButton()) {
                return listView.getSelectionModel().getSelectedItems().getFirst();
            }
            if (dialogButton.getButtonData().isCancelButton()) {
                isCanceled.set(true);
                return null;
            }
            return null;
        });

        String result = dialog.showAndWait().orElse(null);
        if (isCanceled.get()) {
            return;
        }

        if (result == null || result.isBlank()) {
            return;
        }

        final int DEFAULT_GAME_ID = -1;
        int gameId = gamesFromXML.stream()
                .filter(game -> game.toString().equals(result))
                .map(ChessGame::getId)
                .map(Integer::parseInt)
                .findFirst()
                .orElse(DEFAULT_GAME_ID);

        System.out.println(gameId);
        if(gameId == DEFAULT_GAME_ID){
            return;
        }

        String moveXML = ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE message>" +
                "<message>" +
                "<header>" +
                "<protocol>" +
                "<type/>" +
                "<version>1.0</version>" +
                "<command>MOVE</command>" +
                "</protocol>" +
                "<id>" +
                "<username>%s</username>" +
                "<email>%s</email>" +
                "</id>" +
                "</header>" +
                "<body>" +
                "<user>" +
                "<id>%d</id>" +
                "</user>" +
                "</body>" +
                "</message>").formatted(this.username, this.email,gameId);

        sender.println(moveXML);
        String moveMessage = receiver.readLine();
        System.out.println(moveMessage);
        StringReader moveCarStream = new StringReader(moveMessage);
        Document moveDoc = new SAXBuilder().build(moveCarStream);

        System.out.println(moveDoc);

        String moveList = moveDoc.getRootElement()
                .getChild("body")
                .getChildren("move")
                .stream()
                .map(element -> {
                    String turn = element.getChild("turn").getValue();
                    String white = element.getChild("white").getValue();
                    String black = element.getChild("black").getValue();

                    System.out.println(turn + ", " + white + ", " + black);
                    return turn + ", " + white + ", " + black;
                }).reduce("", (accumulator, element) -> accumulator + "\n" + element);

        Dialog<String> moveDialog = new TextInputDialog();
        moveDialog.setTitle("Message");
        moveDialog.setHeaderText("");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextArea textArea = new TextArea(moveList);
        textArea.setEditable(false);
        TextField activeMove = new TextField();

        grid.add(new Label("Currently made moves:"), 0, 0);
        grid.add(textArea, 1, 0);

        grid.add(new Label("Next move:"), 0, 1);
        grid.add(activeMove, 1, 1);

        moveDialog.getDialogPane().setContent(grid);

        moveDialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData().isDefaultButton()) {
                return activeMove.getText();
            }
            if (dialogButton.getButtonData().isCancelButton()) {
                isCanceled.set(true);
                return null;
            }
            return null;
        });

        moveDialog.showAndWait().ifPresent(System.out::println);

        //Send next move over to server....

    }
    public void displayPreviousMatches(ActionEvent actionEvent) throws IOException, JDOMException {

        TextInputDialog getUserDialogBox = new TextInputDialog();
        getUserDialogBox.setTitle("History");
        getUserDialogBox.setHeaderText("");
        getUserDialogBox.setContentText("User:");
        String user = getUserDialogBox.showAndWait().orElse(null);

        String xml = ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE message>" +
                "<message>" +
                    "<header>" +
                        "<protocol>" +
                            "<type/>" +
                            "<version>1.0</version>" +
                            "<command>HISTORY</command>" +
                        "</protocol>" +
                        "<id>" +
                            "<username>%s</username>" +
                            "<email>%s</email>" +
                        "</id>" +
                    "</header>" +
                    "<body>" +
                        "<user>" +
                            "<username>%s</username>" +
                        "</user>" +
                    "</body>" +
                "</message>").formatted(username, this.email,user);

        sender.println(xml);


        String message = receiver.readLine();
//        System.out.println(message);
        StringReader charStream = new StringReader(message);
        Document doc = new SAXBuilder().build(charStream);

        List<String> gamesFromXML = doc.getRootElement()
                .getChild("body")
                .getChildren("game")
                .stream()
                .map(element -> {
                    String id = element.getChild("id").getValue();
                    String player_white = element.getChild("player_white").getValue();
                    String player_black = element.getChild("player_black").getValue();
                    String fen = element.getChild("fen").getValue();
                    String is_active = element.getChild("is_active").getValue();
                    String time = element.getChild("time").getValue();
                    return new ChessGame(id,player_white,player_black,fen,is_active,time);
                })
                .map(ChessGame::toString)
                .toList();


        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Games");
        dialog.setHeaderText("");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        ListView<String> listView = new ListView<>(FXCollections.observableList(gamesFromXML));
        listView.setEditable(false);
        dialog.getDialogPane().setContent(listView);
        dialog.setResultConverter(dialogButton -> listView.getSelectionModel().getSelectedItems().getFirst());

        String result = dialog.showAndWait().orElse(null);
//        System.out.println(result);
    }

    public void startNewMatch(ActionEvent actionEvent) throws IOException, JDOMException {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Match");
        dialog.setHeaderText("Start new match");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField userField = new TextField();
        TextField emailField = new TextField();

        grid.add(new Label("Opponent username:"), 0, 0);
        grid.add(userField, 1, 0);

        grid.add(new Label("Opponent Email:"), 0, 1);
        grid.add(emailField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        AtomicBoolean isCanceled = new AtomicBoolean(false);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData().isDefaultButton()) {
                return new Pair<>(userField.getText(), emailField.getText());
            }
            if (dialogButton.getButtonData().isCancelButton()) {
                isCanceled.set(true);
                return null;
            }
            return null;
        });

        Pair<String, String> dialogPair = dialog.showAndWait().orElse(null);
        if (isCanceled.get()) {
            return;
        }

        if (dialogPair == null
                || dialogPair.getKey().isBlank()
                || dialogPair.getValue().isBlank()) {
            return;
        }

        Pair<String, String> oppenentPair = new Pair<>(dialogPair.getKey(), dialogPair.getValue());
//        System.out.println(oppenentPair);

        String xml = ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE message>" +
                "<message>" +
                    "<header>" +
                        "<protocol>" +
                            "<type/>" +
                            "<version>1.0</version>" +
                            "<command>START</command>" +
                        "</protocol>" +
                        "<id>" +
                            "<username>%s</username>" +
                            "<email>%s</email>" +
                        "</id>" +
                    "</header>" +
                    "<body>" +
                        "<challenger>" +
                            "<username>%s</username>" +
                            "<email>%s</email>" +
                        "</challenger>" +
                        "<opponent>" +
                            "<username>%s</username>" +
                            "<email>%s</email>" +
                        "</opponent>" +
                    "</body>" +
                "</message>").formatted(username, this.email,username, this.email,oppenentPair.getKey(),oppenentPair.getValue());
        sender.println(xml);
    }
}