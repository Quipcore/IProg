package net.examclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;

public class StartupViewController {

    private static final int DEFAULT_PORT = 2000;
    private static final String DEFAULT_ADDRESS = "192.168.86.36";

    @FXML
    private TextField addressField;

    @FXML
    private Button connectBtn;

    @FXML
    protected void onConnectButtonClick(ActionEvent actionEvent) throws IOException {
        Socket socket = getSocket();
        switchScene(socket);
    }

    private void switchScene(Socket socket) throws IOException {
        Stage stage = (Stage) this.connectBtn.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("chess-view.fxml"));
        Parent root = loader.load();
        ChessView chessView = loader.getController();
        setViewProperties(chessView, socket);


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void setViewProperties(ChessView chessView, Socket socket) throws IOException {
        chessView.setSocket(socket);
        chessView.setEmail("felix.lido@gmail.com");
        chessView.setUsername("felixlido");
    }

    private Socket getSocket() throws IOException {
        int port = DEFAULT_PORT;
        String address = DEFAULT_ADDRESS;

        if(!addressField.getText().isBlank()){
            String[] enteredInformation = addressField.getText().split(":");
            address = enteredInformation[0];
            port = Integer.parseInt(enteredInformation[1]);
        }

        System.out.printf("Connecting to %s:%d\n",address,port);

        return new Socket(address,port);
    }
}
