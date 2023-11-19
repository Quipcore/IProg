package assignment_2.stream_sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread{
    private final static char COMMAND_SYMBOL = '/';
    private final Socket client;
    private final Scanner scanner;
    private final PrintWriter out;
    private final BufferedReader in;

    public ServerThread(Socket client) throws IOException {
        this.client = client;
        printClientInfo(client);

        this.scanner = new Scanner(System.in);
        this.out = new PrintWriter(client.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        start();
    }

    private void printClientInfo(Socket client) {
        System.out.println(client.getInetAddress());
    }

    @Override
    public void run() {
        String message = "";
        while(!isCommand("EXIT",message)){
            message = readMessage();
            respondToMessage(message);
        }
    }

    private boolean isCommand(String command, String message) {
        if(!message.startsWith(String.valueOf(COMMAND_SYMBOL))){
            return false;
        }

        return message.equals(COMMAND_SYMBOL + command);
    }

    private void respondToMessage(String message) {

    }

    private String readMessage() {
        return "";
    }
}
