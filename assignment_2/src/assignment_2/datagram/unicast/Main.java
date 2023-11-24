package assignment_2.datagram.unicast;

import javax.xml.crypto.Data;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Main {

    private static int port;
    private static String remoteHost;
    private static int remotePort;

    public static void main(String[] args){
        port = Integer.parseInt(args[0]);
        remoteHost = args[1];
        remotePort = Integer.parseInt(args[2]);

        Draw canvas = new Draw();

        try(DatagramSocket datagramSocket = new DatagramSocket(port)){
            datagramSocket.connect(InetAddress.getByName(remoteHost), remotePort);
            System.out.println(datagramSocket.isConnected());


            DatagramSender sender = new DatagramSender(datagramSocket, canvas);
            DatagramReceiver reciver = new DatagramReceiver(datagramSocket, canvas);
        }catch (Exception e){

        }
    }
}
