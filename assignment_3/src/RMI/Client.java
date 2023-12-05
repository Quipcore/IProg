package RMI;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;

//public class Client extends JFrame {
public class Client {

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

        System.setProperty("java.security.policy","assignment_3/src/RMI/policy");

        String host = "atlas.dsv.su.se";
        String url = "rmi://" + host + "/";
        RemoteServer  remoteServer = (RemoteServer)Naming.lookup(url + "server");

        Client client = new Client();
        client.run(remoteServer);
    }

    public Client(){

    }

    private void run(RemoteServer remoteServer) throws RemoteException {
        Vector<Integer> ballsRaw = new Vector<>();
        int xLim = 500, yLim = 500, rMax = 70;
        while (true) {
            // Hämtar först till en tmp-vektor för att slippa blinkandet
            Vector ballsRawTmp = null;
            try {
                ballsRawTmp = remoteServer.getBalls();
            } catch (RemoteException re) {
                System.out.println("Exception generated: " + re.getMessage());
            }

            // Sudda de tidigare bollarna
//            Graphics g = getGraphics();
//            g.setColor(getBackground());
            for (int i = 0; i < ballsRaw.size(); i = i + 3) {
                int x = ballsRaw.elementAt(i);
                int y = ballsRaw.elementAt(i + 1);
                int r = ballsRaw.elementAt(i + 2);
//                g.fillOval(x, y, r, r);
            }

            // Rita de nya bollarna
            ballsRaw = ballsRawTmp;
            for (int i = 0; i < ballsRaw.size(); i = i + 3) {
                int x = ballsRaw.elementAt(i);
                int y = ballsRaw.elementAt(i + 1);
                int r = ballsRaw.elementAt(i + 2);

                int blue = r * 255 / rMax;

                if (blue > 255) {
                    blue = 255;
                }
                if (blue < 0) {
                    blue = 0;
                }

//                g.setColor(new Color(0, 0, blue));
//                g.fillOval(x, y, r, r);
            }

//            setTitle("Antal bollar: " + ballsRaw.size() / 3);
            System.out.println("Antal bollar: " + ballsRaw.size() / 3);
            remoteServer.addBall();
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
    }
}
