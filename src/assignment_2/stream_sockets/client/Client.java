package assignment_2.stream_sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 2000;

    private static List<String> receivedMessages = new ArrayList<>();
    private static List<String> sentMessages = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        if (args.length > 0) {
            host = args[0];

            if (args.length == 2) {
                port = Integer.parseInt(args[1]);
            }
        }

        startClient(host, port);
    }

    private static void startClient(String host, int port) {
        while (true) {
            Socket socket = createSocketConnection(host, port);
            System.out.printf("Connected to %s:%d\n", host, port);

            try {
                communicate(socket);
                System.out.printf("Client disconnected from: %s and port: %d\n", socket.getInetAddress(), socket.getPort());
            } catch (Exception e) {
                System.out.println("Something went wrong in communication. Trying to reconnect!");
            }
        }
    }

    private static void communicate(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while (!socket.isClosed()) {
            sendMessage(out);
            readMessage(in);
        }
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

    private static void readMessage(BufferedReader in) throws IOException {
        System.out.printf("<Server> %s\n", in.readLine());
        receivedMessages.add(in.readLine());
    }

    private static void sendMessage(PrintWriter out) {
        String messageToSend = scanner.nextLine();
        sentMessages.add(messageToSend);

        System.out.print("<Client> ");
        out.println(messageToSend);

    }
}
