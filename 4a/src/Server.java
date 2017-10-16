import javafx.util.Pair;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements IRemoteServer {
    protected Server() throws RemoteException {
        super();
    }

    public Pair<byte[],Integer> read(String filename, int pos, int amount) throws RemoteException {
        System.out.println("Received read request: " + filename + " " + pos + " " + amount);
        byte[] data = new byte[amount];
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "r");
            int toRead = file.getChannel().size() - pos < 1024 ? (int) file.getChannel().size() - pos : amount;
            System.out.println(toRead);
            file.seek(pos);
            Integer readBytes = file.read(data, 0, toRead);
            file.close();
            return new Pair<>(data, readBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair<>(data,0);
    }

    public int write(String filename, int amount, byte[] data) throws RemoteException {
        System.out.println("Received write request");
        try {
            FileOutputStream file = new FileOutputStream("server_" + filename, true);
            long oldSize = file.getChannel().size();
            file.write(data, 0, amount);
            long currentSize = file.getChannel().size();
            file.close();
            return (int) (currentSize - oldSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
