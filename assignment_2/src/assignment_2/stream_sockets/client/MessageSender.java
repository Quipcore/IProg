package assignment_2.stream_sockets.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MessageSender extends Thread{

    private final Scanner scanner;

    private final PrintWriter out;

    public MessageSender(Socket socket) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(),true);
        this.scanner = new Scanner(System.in);
        start();
    }

    @Override
    public void run() {
        while(isAlive()){
            String message = scanner.nextLine();
            System.out.printf("<Client> %s\n", message);
            out.println(message);
        }
    }
}
