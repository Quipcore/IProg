package webbserver_connections;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    private static String urlString = "https://en.wikipedia.org/wiki/Main_Page";
    public static void main(String[] args) throws IOException, URISyntaxException {
        String text = WebbConnection.getText(urlString);
        System.out.println(text);

        WebbViewer webbViewer = new WebbViewer(text);
        webbViewer.pack();
        webbViewer.setVisible(true);
    }
}
