package net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Main {

    private static final int DEFAULT_PORT = 2000;
    private static final int MAX_CONNECTION = 1000;
    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)){
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.printf("Created server on: %s and port: %d\n", hostAddress, serverSocket.getLocalPort());
            while(true){
                if(ServerThread.connectionCount() <= MAX_CONNECTION){
                    new ServerThread(serverSocket.accept());
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}