package net;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class ServerThread extends Thread{
    private final static char COMMAND_SYMBOL = '/';
    private final Socket client;
    private final PrintWriter out;
    private final BufferedReader in;
    private static Set<ServerThread> threadPool;

    public ServerThread(Socket client) throws IOException {
        this.client = client;
        printClientInfo(client);
        if(threadPool == null){
            threadPool = Collections.synchronizedSet(new HashSet<>());
        }
        threadPool.add(this);
        this.out = new PrintWriter(client.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        start();
    }

    public static int connectionCount() {
        if(threadPool == null){
            return 0;
        }
        return threadPool.size();
    }

    private void printClientInfo(Socket client) {
        System.out.println(client.getInetAddress());
    }

    @Override
    public void run() {
        try {
            communicate(client);
            System.out.printf("Client disconnected from: %s and port: %d\n", client.getInetAddress(), client.getPort());
        }catch (Exception e){
            System.out.println("Something went wrong in communication");
        }

        threadPool.remove(this);
        System.out.println("Removed "  + this);
    }

    private void communicate(Socket clientSocket) throws IOException, JDOMException {
        while(!clientSocket.isClosed()){
            String message = this.in.readLine();
            System.out.printf("<Client> %s\n",message);

            String response = respondToMessage(generateXMLFromString(message));
            this.out.println(response);

            if(message.equals("exit")){
                clientSocket.close();
                return;
            }
        }
    }

    private Document generateXMLFromString(String message) throws IOException, JDOMException {
        StringReader charStream = new StringReader(message);
        return new SAXBuilder().build(charStream);
    }

    private String respondToMessage(Document document) {

        Element header = getChildElement(document.getRootElement(), "header");
        Element protocol = getChildElement(header,"protocol");
        String command = getChildElement(protocol,"command").getContent(0).getValue();

        ProtocolBuilder protocolBuilder = new ProtocolBuilder("Response")
                .addHeader("server","","RESPONSE");

        switch (command){
            case "ACTIVE" -> getActiveRespons(document, protocolBuilder);
            case "HISTORY" -> getUserHistory(document);
            case null, default -> getUnkownRespons();
        };

        return protocolBuilder.toString();
    }

    private String getUserHistory(Document document) {
        return "";
    }

    private String getUnkownRespons() {
        return "";
    }

    private String getActiveRespons(Document document, ProtocolBuilder protocolBuilder) {
        protocolBuilder.addGame("1234567890", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "0987654321");




        return "";
    }

    private Element getChildElement(Element element, String name){
        return element.getContent().stream()
                .filter(content -> content instanceof Element)
                .map(content -> (Element)content)
                .filter(elem -> elem.getName().equals(name))
                .findFirst()
                .get();
    }

    private void sendMessage(String message) {
        this.out.println(message);
    }
}
