package assignment_2.stream_sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class Client {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 2000;
    private static int threadExitCode;

    public static void main(String[] args) throws IOException {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        switch (args.length){
            case 2:
                port = Integer.parseInt(args[1]);
            case 1:
                host = args[0];
        }

        threadExitCode = -1;
        startClient(host, port);
    }
    private static void startClient(String host, int port) throws IOException {
        while (threadExitCode != 0) {
            Socket socket;
            try {
                socket = new Socket(host, port);
            }catch(ConnectException e){
                System.out.printf("Failed to connect to %s:%d\n", host, port);
                return;
            }

            System.out.printf("Connected to %s:%d\n", host, port);

            try {
                communicate(socket);
            } catch (Exception e) {
                socket.close();
            }
        }
    }

    private static void communicate(Socket socket) throws IOException, InterruptedException {
        MessageReceiver messageReceiver = new MessageReceiver(socket);
        MessageSender messageSender = new MessageSender(socket);

        messageSender.join();
        messageReceiver.join();

        if(messageSender.getExitCode() == messageReceiver.getExitCode()){
            threadExitCode = messageSender.getExitCode();
        }
    }
}
