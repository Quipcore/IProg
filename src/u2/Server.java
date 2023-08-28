package u2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.printf("Created server on: %s and port: %d\n", serverSocket.getLocalSocketAddress(), serverSocket.getLocalPort());

        Socket clientSocket;
        while(true){
            clientSocket = getClient(serverSocket);
            System.out.printf("Client connected from: %s and port: %d\n", clientSocket.getInetAddress(), clientSocket.getPort());
            communicate(clientSocket);
            System.out.printf("Client disconnected from: %s and port: %d\n", clientSocket.getInetAddress(), clientSocket.getPort());
        }

    }

    private static void communicate(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        while(!clientSocket.isClosed()){
            String inMessage = readMessages(in);
            if(inMessage.equals("exit")){
                clientSocket.close();
                return;
            }
            sendMessages(out);
        }
    }

    private static void sendMessages(PrintWriter out) {
        String message = "ACK";
        System.out.printf("<Server> %s\n",message);
        out.println(message);
    }

    private static String readMessages(BufferedReader in) throws IOException {
        String message = in.readLine();
        System.out.printf("<Client> %s\n",message);
        return message;
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
