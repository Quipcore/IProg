package assignment_2.datagram.unicast;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    private static int port;
    private static String remoteHost;
    private static int remotePort;

    public static void main(String[] args){
        port = Integer.parseInt(args[0]);
        remoteHost = args[1];
        remotePort = Integer.parseInt(args[2]);


        System.out.printf("Connecting to %s:%d\n",remoteHost,remotePort);

        Draw canvas = new Draw();

        try(DatagramSocket datagramSocket = new DatagramSocket(port)){
            datagramSocket.connect(InetAddress.getByName(remoteHost), remotePort);
            System.out.println(datagramSocket.isClosed());
            Thread.sleep(2000);

            DatagramSender sender = new DatagramSender(datagramSocket, canvas);
            DatagramReceiver receiver = new DatagramReceiver(datagramSocket, canvas);

            sender.join();
            receiver.join();

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
