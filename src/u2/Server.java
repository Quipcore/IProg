package u2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {

    public static void main(String[] args) throws UnknownHostException {
        int port = args.length >= 1 ? Integer.parseInt(args[0]) : 2000;

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        }catch (Exception e){
            System.out.println("Couldn't start server!");
            return;
        }

        serverSocket.getInetAddress();
        System.out.printf("Created server on: %s and port: %d\n", InetAddress.getLocalHost().getHostName(), serverSocket.getLocalPort());
        List<ServerThread> serverThreads = new ArrayList<>();
        int MAX_THREADS = 3;
        while(true){
            Socket clientSocket = getClient(serverSocket);
            System.out.printf("Client connected from: %s and port: %d\n", clientSocket.getInetAddress(), clientSocket.getPort());
            Thread serverThread = new ServerThread(clientSocket);
        }
    }

    private static Socket getClient(ServerSocket serverSocket) {
        while(true){
            try {
                return serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Failed to accept client");
            }
        }
    }
}
