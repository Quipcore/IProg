package net.examclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;

import com.jamesmurty.utils.*;
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

    public void displayActiveMatches(ActionEvent actionEvent) throws IOException, JDOMException {
        Document doc = getXMLDocumentFromServer(this.username,this.email,"ACTIVE");
        System.out.println(doc);
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
