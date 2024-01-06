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
        Document doc = getXMLDocumentFromServer(this.username,this.email,"ACTIVE");
        List<ChessGame> games = getGamesFromXML(doc);

        games.forEach(System.out::println);
        ChessGame chessGame = getSelectedMatchFromPopup(games);
    }

    private List<ChessGame> getGamesFromXML(Document doc) {

        return doc.getRootElement()
                .getChildren("body")
                .stream()
                .map(element -> element.getChildren("game"))
                .flatMap(List::stream)
                .map(element -> {
                    String id = element.getChild("id").getValue();
                    String fen = element.getChild("fen").getValue();
                    String time = element.getChild("time").getValue();
                    Timestamp timestamp = new Timestamp(Integer.parseInt(time));
                    String moves = element.getChild("moves").getValue();
                    return new ChessGame(id, fen,timestamp, moves);
                })
                .collect(Collectors.toList());
    }

    private ChessGame getSelectedMatchFromPopup(List<ChessGame> games) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Connection");
        dialog.setHeaderText(String.format("Connection from %s to %s", "p0", "p1"));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableList(games.stream().map(Object::toString).collect(Collectors.toList())));
        listView.setEditable(false);
        dialog.getDialogPane().setContent(listView);
        dialog.setResultConverter(dialogButton -> listView.getSelectionModel().getSelectedItems().getFirst());

        System.out.println(dialog.showAndWait().orElse(null));
        return null;
    }

    private Stage getPrimaryStage() {
        return (Stage) addressField.getScene().getRoot().getScene().getWindow();
    }

    public void displayPreviousMatches(ActionEvent actionEvent) throws IOException, JDOMException {
        Document doc = getXMLDocumentFromServer(this.username,this.email,"HISTORY");
        System.out.println(doc);
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

        TextField nameField = new TextField();
        TextField timeField = new TextField();

        grid.add(new Label("Email:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("First move:"), 0, 1);
        grid.add(timeField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        AtomicBoolean isCanceled = new AtomicBoolean(false);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData().isDefaultButton()) {
                return new Pair<>(nameField.getText(), timeField.getText());
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
                || dialogPair.getValue().isBlank()
                || !dialogPair.getValue().matches(CHESS_NOTATION_REGEX)) {
            return;
        }

        Pair<String, String> emailMove = new Pair<>(dialogPair.getKey(), dialogPair.getValue());
        System.out.println(emailMove);


//        Document doc = getXMLDocumentFromServer(this.username,this.email,"MATCH");
//        System.out.println(doc);
    }

    private Document getXMLDocumentFromServer(String username, String email, String command) throws IOException, JDOMException {
        ProtocolBuilder protocolBuilder = new ProtocolBuilder("Message")
                .addHeader(username, email, command)
                .addBody();

        String xml = protocolBuilder.toString();
        sender.println(xml);

        String message = receiver.readLine();
        System.out.println(message);
        StringReader charStream = new StringReader(message);
        return new SAXBuilder().build(charStream);
    }
}
