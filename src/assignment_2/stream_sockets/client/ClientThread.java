package assignment_2.stream_sockets.client;

public class ClientThread extends Thread{
    public ClientThread() {
        super();
    }

    @Override
    public void run() {
        System.out.println("ClientThread running");
    }
}
