package assignment_2.stream_sockets.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int DEFAULT_PORT = 2000;
    public static void main(String[] args) {
        int port = args.length >= 1 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        try(ServerSocket serverSocket = new ServerSocket(port)){
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.printf("Created server on: %s and port: %d\n", hostAddress, serverSocket.getLocalPort());
            List<ServerThread> serverThreads = new ArrayList<>();
            while(true){
                removeDeadThreads(serverThreads);
                Socket client = serverSocket.accept();
                serverThreads.add(new ServerThread(client));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void removeDeadThreads(List<ServerThread> serverThreads) {
        serverThreads.removeIf(serverThread -> {

            boolean isDead = !serverThread.isAlive();
            if(isDead){
                System.out.printf("Removing: %s \n",serverThread);
                return true;
            }

            return false;
        });
    }
}
