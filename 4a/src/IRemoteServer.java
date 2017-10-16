import javafx.util.Pair;

import java.rmi.RemoteException;

public interface IRemoteServer extends java.rmi.Remote {
    Pair<byte[],Integer> read(String filename, int pos, int amount) throws RemoteException;
    int write(String filename, int amount, byte[] data) throws RemoteException;
}
