package assignment_2.datagram.unicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DatagramReceiver extends Thread{

    DatagramSocket datagramSocket;

    public DatagramReceiver(DatagramSocket datagramSocket, Draw canvas) {
        this.datagramSocket = datagramSocket;
        start();
    }

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket("".getBytes(),0);
        try {
            this.datagramSocket.receive(packet);
            System.out.printf("Recived packet from %s",packet.getAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
