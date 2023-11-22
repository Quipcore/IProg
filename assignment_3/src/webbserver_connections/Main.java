package webbserver_connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Main {

    private static String liuTermin2SimpelHTML = "http://it-programmet.gitlab-pages.liu.se/termin2/index.html";
    private static String urlString = liuTermin2SimpelHTML;
    public static void main(String[] args) throws IOException {
        StringBuffer buffer = new StringBuffer();
        String line = "";
        URL url = new URL(args[0]);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        while((line = br.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        String text = buffer.toString();
        System.out.println(text);
    }
}
