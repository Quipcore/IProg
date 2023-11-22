package assignment_2.stream_sockets.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    private static final int DEFAULT_PORT = 2000;
    private static final int MAX_CONNECTION = 0;
    public static void main(String[] args) {
        int port = args.length >= 1 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        try(ServerSocket serverSocket = new ServerSocket(port)){
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
