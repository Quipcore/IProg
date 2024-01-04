package net.examclient;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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
//        System.out.println(doc);
//        Element response = doc.getRootElement();
//        Element body = response.getChild("body");
//        Element game = response.getChild("game");
//
//        System.out.println(body);

        return doc.getContent().stream()
                .filter(content -> content instanceof Element)
                .map(content -> (Element)content)
                .filter(element -> element.getName().equals("Response"))
                .map(Element::getContent)
                .flatMap(List::stream)
                .filter(content -> content instanceof Element)
                .map(content -> (Element)content)
                .filter(elem -> elem.getName().equals("body"))
                .map(Element::getContent)
                .flatMap(List::stream)
                .filter(content -> content instanceof Element)
                .map(content -> (Element)content)
                .filter(elem -> elem.getName().equals("game"))
                .map(element -> {
                    String id = getChildElement(element, "id").getContent(0).getValue();
                    String fen = getChildElement(element, "fen").getContent(0).getValue();
                    String time = getChildElement(element, "time").getContent(0).getValue();
                    Timestamp timestamp = new Timestamp(Integer.parseInt(time));
                    String moves = getChildElement(element, "moves").getContent(0).getValue();
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
        Document doc = getXMLDocumentFromServer(this.username,this.email,"MATCH");
        System.out.println(doc);
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

    private Element getChildElement(Element element, String name){
        return element.getContent().stream()
                .filter(content -> content instanceof Element)
                .map(content -> (Element)content)
                .filter(elem -> elem.getName().equals(name))
                .findFirst()
                .get();
    }
}
