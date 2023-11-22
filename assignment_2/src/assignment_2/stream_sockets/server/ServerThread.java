package assignment_2.stream_sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{
    private final static char COMMAND_SYMBOL = '/';
    private final Socket client;
    //private final Scanner scanner;
    private final PrintWriter out;
    private final BufferedReader in;

    public ServerThread(Socket client) throws IOException {
        this.client = client;
        printClientInfo(client);

        //this.scanner = new Scanner(System.in);
        this.out = new PrintWriter(client.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        start();
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
    }

    private static void communicate(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        while(!clientSocket.isClosed()){
            String message = readMessage(in);
            if(message.equals("exit")){
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

    private static String readMessage(BufferedReader in) throws IOException {
        String message = in.readLine();
        System.out.printf("<Client> %s\n",message);
        return message;
    }
}
