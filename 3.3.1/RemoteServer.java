package RMI;

import java.util.*;
import java.rmi.*;

public interface RemoteServer extends Remote {
    void addBall() throws RemoteException;
    void pauseBalls() throws RemoteException;
    Vector getBalls() throws RemoteException;
}
