package RMI;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class Server extends UnicastRemoteObject implements RemoteServer{
    
    Vector<Integer> balls = new Vector<>();

    protected Server() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws MalformedURLException, AlreadyBoundException, RemoteException {

        LocateRegistry.createRegistry(1099);

        Server server = new Server();

        Naming.bind("server",server);
    }
    @Override
    public void addBall() throws RemoteException {
        balls.add(1);
    }

    @Override
    public void pauseBalls() throws RemoteException {
        System.out.println("PAUSING");
    }

    @Override
    public Vector getBalls() throws RemoteException {
        return balls;
    }
}
