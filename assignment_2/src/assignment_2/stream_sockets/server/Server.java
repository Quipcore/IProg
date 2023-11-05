package assignment_2.stream_sockets.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int STANDARD_PORT = 2000;

    public static void main(String[] args) throws UnknownHostException {
        int port = args.length >= 1 ? Integer.parseInt(args[0]) : STANDARD_PORT;

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

            if(serverThreads.size() < MAX_THREADS) {
                serverThreads.add((ServerThread) serverThread);
                serverThread.start();
            }else{
                System.out.println("Server is full!");
                try {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Server is full!");
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Failed to send message to client");
                }
            }
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
