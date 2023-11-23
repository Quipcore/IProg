package assignment_2.datagram.unicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class DatagramSender extends Thread{


    public DatagramSender(DatagramSocket datagramSocket, Draw canvas) {
//        String message = "Hello World!";
//        DatagramPacket packet = new DatagramPacket(message.getBytes(StandardCharsets.UTF_8), message.length());
//        try {
//            datagramSocket.send(packet);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        start();
    }

    @Override
    public void run() {
        System.out.println("Hello Sender");
        super.run();
    }
}
