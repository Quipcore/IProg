package webbserver_connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebbConnection {

    public static String getText(String text) throws URISyntaxException, IOException {
        URL url = new URI(text).toURL();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer buffer = new StringBuffer();
        br.lines().forEach(str -> buffer.append(str).append("\n"));
        return buffer.toString();
    }
}
