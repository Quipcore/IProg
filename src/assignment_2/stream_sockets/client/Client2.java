package assignment_2.stream_sockets.client;

import java.io.IOException;
import java.net.Socket;

public class Client2 {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 2000;

    public static void main(String[] args) throws IOException {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        //Set host to first argument if it exists
        //set port to second argument if it exists

        switch (args.length){
            case 2:
                port = Integer.parseInt(args[1]);
            case 1:
                host = args[0];
        }


        startClient(host, port);
    }

    private static void startClient(String host, int port) throws IOException {
        Socket socketToServer = new Socket(host, port);

        if(!socketToServer.isConnected()){
            System.out.printf("Failed to connect to %s:%d\n",host, port);
            System.exit(1);
        }

        System.out.printf("Connected to %s:%d\n",host, port);

    }
}
