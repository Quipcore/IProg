package assignment_2.datagram.unicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DatagramSender extends Thread{

    Draw canvas;
    DatagramSocket datagramSocket;
    public DatagramSender(DatagramSocket datagramSocket, Draw canvas) {
        this.canvas = canvas;
        this.datagramSocket = datagramSocket;
        start();
    }

    @Override
    public void run() {
        byte[] message = "Hello World!".getBytes();
        try {
            this.datagramSocket.send(new DatagramPacket(message,message.length));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
