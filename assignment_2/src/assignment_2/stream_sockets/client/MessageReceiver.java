package assignment_2.stream_sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiver extends Thread{

    private final BufferedReader in;
    private final Socket socket;
    private int exitCode;
    public MessageReceiver(Socket socket) throws IOException {
        exitCode = -1;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.socket = socket;
        start();
    }


    @Override
    public void run() {
        while(!socket.isClosed()){
            try {
                System.out.printf("<Server> %s\n", in.readLine());
            } catch (IOException e) {
                closeSocket();
            }
        }
        System.out.println("Closing Reciver");
        exitCode = 0;
    }

    private void closeSocket(){
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getExitCode(){
        return exitCode;
    }
}
