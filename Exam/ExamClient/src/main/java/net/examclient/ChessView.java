package net.examclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import com.jamesmurty.utils.*;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jdom2.Document;
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
//        showHTMLPopup();
        List<String> htmlLines = Files.readAllLines(Path.of(this.getClass().getResource("chessboard.html").toURI()))
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());
        String html = String.join("",htmlLines);
        System.out.println(html);
        showHTMLPopup(html);

//        Document doc = getXMLDocumentFromServer(this.username,this.email,"ACTIVE");
//        System.out.println(doc);
    }

    private void showHTMLPopup(String html) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("HTML Popup");

        WebView webView = new WebView();
        webView.getEngine().loadContent(html);

        StackPane popupLayout = new StackPane();
        popupLayout.getChildren().add(webView);

        Scene popupScene = new Scene(popupLayout, 400, 300);
        popupStage.setScene(popupScene);

        // Position the popup relative to the main stage
        popupStage.initOwner(getPrimaryStage());

        popupStage.showAndWait();
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
        StringReader charStream = new StringReader(message);
        return new SAXBuilder().build(charStream);
    }
}
