package webbserver_connections;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    private static String liuTermin2SimpelHTML = "http://it-programmet.gitlab-pages.liu.se/termin2/index.html";
    private static String urlString = liuTermin2SimpelHTML;
    public static void main(String[] args) throws IOException, URISyntaxException {
        String text = WebbConnection.getText(urlString);
        System.out.println(text);

        WebbViewer webbViewer = new WebbViewer(text);
        webbViewer.pack();
        webbViewer.setVisible(true);
    }
}
