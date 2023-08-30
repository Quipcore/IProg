package u2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{
    Socket clientSocket;

    public ServerThread(Socket clientSocket){
        this.clientSocket = clientSocket;
        start();
    }
    @Override
    public void run() {
        try {
            communicate(clientSocket);
            System.out.printf("Client disconnected from: %s and port: %d\n", clientSocket.getInetAddress(), clientSocket.getPort());
        }catch (Exception e){
            System.out.println("Something went wrong in communication");
        }
    }

    private static void communicate(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        while(!clientSocket.isClosed()){
            String inMessage = processMessage(in);
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

    private static String processMessage(BufferedReader in) throws IOException {
        String message = in.readLine();
        System.out.printf("<Client> %s\n",message);
        return message;
    }

    public void close(){

    }
}
