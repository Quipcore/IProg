package assignment_2.stream_sockets.xml;

import assignment_2.stream_sockets.client.MessageReceiver;
import assignment_2.stream_sockets.client.MessageSender;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.net.*;
import java.util.List;
import java.util.Objects;

public class XMLClient{


    //TEMPLATE!!!
    private static String DOCUMENT_URL = "https://atlas.dsv.su.se/~pierre/i/05_ass/ip1/2/2.1.3/message.dtd";

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 2000;
    private static int threadExitCode;

    public static void main(String[] args) throws IOException, JDOMException, URISyntaxException {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        switch (args.length){
            case 2:
                port = Integer.parseInt(args[1]);
            case 1:
                host = args[0];
        }

        threadExitCode = -1;
//        startClient("atlas.dsv.su.se",9494);
        startClient(host, port);
    }




    private static void startClient(String host, int port) throws IOException {
        while (threadExitCode != 0) {
            Socket socket = createSocketConnection(host, port);
            System.out.printf("Connected to %s:%d\n", host, port);

            try {
                communicate(socket);
                System.out.printf("Client disconnected from: %s and port: %d\n", socket.getInetAddress(), socket.getPort());
                if(!socket.isClosed()){
                    socket.close();
                }
            } catch (Exception e) {
                socket.close();
                System.out.println("Something went wrong in communication. Trying to reconnect!");
            }
        }
    }

    private static void communicate(Socket socket) throws IOException, InterruptedException {
        XMLReceiver messageReceiver = new XMLReceiver(socket);
        XMLSender xmlSender = new XMLSender(socket);

        xmlSender.join();
        messageReceiver.join();
        threadExitCode = messageReceiver.getExitCode();
    }

    private static Socket createSocketConnection(String host, int port) {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket(host, port);
            } catch (Exception e) {
                System.out.printf("Failed to connect to %s:%d\n", host, port);
            }
        }

        return socket;
    }
}
