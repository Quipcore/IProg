package assignment_2.datagram.unicast;

import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DatagramReceiver extends Thread{

    private DatagramSocket datagramSocket;
    private Draw canvas;

    public DatagramReceiver(DatagramSocket datagramSocket, Draw canvas) {
        this.datagramSocket = datagramSocket;
        this.canvas = canvas;
        start();
    }

    @Override
    public void run() {

        byte[] buf = new byte[256];
        while(datagramSocket.isConnected()){
            try {

                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                datagramSocket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());

                Point p = parseToPoint(received);

                canvas.drawAt(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Point parseToPoint(String str){
        String[] split = str.split(",");
        Point p;
        try{
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            p = new Point(x,y);
        }catch (NumberFormatException e){
            return null;
        }
        return p;
    }
}
