package assignment_2.stream_sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiver extends Thread{

    private final BufferedReader in;

    public MessageReceiver(Socket socket) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        start();
    }

    @Override
    public void run() {
        while(isAlive()){
            try {
                System.out.printf("<Server> %s\n", in.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
