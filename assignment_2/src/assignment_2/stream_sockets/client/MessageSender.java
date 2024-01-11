package assignment_2.stream_sockets.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MessageSender extends Thread{

    private final Scanner scanner;

    private final PrintWriter out;
    private final Socket socket;
    private int exitCode;

    public MessageSender(Socket socket) throws IOException {
        exitCode = -1;
        this.out = new PrintWriter(socket.getOutputStream(),true);
        this.scanner = new Scanner(System.in);
        this.socket = socket;
        start();
    }

    @Override
    public void run() {
        while(!socket.isClosed()){
            String message = scanner.nextLine();
            System.out.printf("<Client> %s\n", message);
            out.println(message);
            if (message.equals("exit")){
                closeSocket();
            }
        }
        exitCode = 0;
    }

    private void closeSocket() {
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
