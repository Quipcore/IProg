package assignment_2.datagram.unicast;

import java.awt.Point;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class DatagramSender extends Thread{

    private Draw canvas;
    private DatagramSocket datagramSocket;

    private InetAddress address;

    private int port;

    private HashSet<Point> drawnPoints;

    public DatagramSender(DatagramSocket datagramSocket, Draw canvas) {
        this.canvas = canvas;
        this.datagramSocket = datagramSocket;
        this.port = datagramSocket.getPort();
        this.address = datagramSocket.getInetAddress();
        this.drawnPoints = new HashSet<>();

        System.out.printf("%s:%d\n",address,port);

        start();
    }

    @Override
    public void run() {
        while(datagramSocket.isConnected()){
            List<Point> points = canvas.getDrawnPoints();

            //DONT CHANGE TO FOREACH OR USE ITERATORS!!!!! WILL CAUSE EXCEPTION!!!!
            for(int i = 0; i < points.size(); i++){
                Point point = points.get(i);
                if(!drawnPoints.contains(point)){
                    byte[] buf = String.format("%d,%d",point.x,point.y).getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                    try {
                        this.datagramSocket.send(packet);
                    } catch (IOException e) {
                        continue;
                    }
                    this.drawnPoints.add(new Point(point.x,point.y));
                }
            }
        }
    }
}
