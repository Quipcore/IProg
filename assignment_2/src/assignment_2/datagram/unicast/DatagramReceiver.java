package assignment_2.datagram.unicast;

import java.net.DatagramSocket;

public class DatagramReceiver extends Thread{
    public DatagramReceiver(DatagramSocket datagramSocket, Draw canvas) {
        start();
    }

    @Override
    public void run() {
        System.out.println("Hello Receiver");
        super.run();
    }
}
