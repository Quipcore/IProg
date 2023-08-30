package u2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "127.0.0.1"; //DEFAULT HOST
        int port = 2000; //DEFAULT PORT

        switch (args.length){
            case 2:
                port = Integer.parseInt(args[1]);
            case 1:
                host = args[0];
                break;
        }

        while(true){
            Socket socket = createSocketConnection(host, port);

            System.out.printf("Connected to %s:%d\n",host, port);

            try {
                communicate(socket);
                System.out.printf("Client disconnected from: %s and port: %d\n", socket.getInetAddress(), socket.getPort());
            }catch (Exception e){
                System.out.println("Something went wrong in communication. Trying to reconnect!");
            }
        }

    }

    private static void communicate(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while(!socket.isClosed()){
            sendMessage(out);
            readMessage(in);
        }
    }

    private static Socket createSocketConnection(String host, int port){
        Socket socket = null;
        while(socket == null){
            try{
                socket = new Socket(host, port);
            }catch (Exception e){
                System.out.printf("Failed to connect to %s:%d\n",host, port);
            }
        }

        return socket;
    }

    private static void readMessage(BufferedReader in) throws IOException {
        System.out.printf("<Server> %s\n",in.readLine());
    }

    private static void sendMessage(PrintWriter out) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("<Client> ");
        out.println(scanner.nextLine());
    }
}
