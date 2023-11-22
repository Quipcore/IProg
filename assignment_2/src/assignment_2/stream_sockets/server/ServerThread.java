package assignment_2.stream_sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    private void communicate(Socket clientSocket) throws IOException {
        while(!clientSocket.isClosed()){
            String message = this.in.readLine();
            System.out.printf("<Client> %s\n",message);
            if(message.equals("exit")){
                clientSocket.close();
                return;
            }

            for(ServerThread serverThread : threadPool){
                if(serverThread.equals(this)){
                    continue;
                }
                serverThread.sendMessage(message);
            }
        }
    }

    private void sendMessage(String message) {
        this.out.println(message);
    }
}
