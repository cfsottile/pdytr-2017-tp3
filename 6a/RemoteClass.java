import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RemoteClass extends UnicastRemoteObject implements RemoteClassInterface {

    protected RemoteClass() throws RemoteException {
        super();
    }

    public byte[] sendThisBack(byte[] data) throws RemoteException {
        System.out.println("Me llego: "+data);
        return data;
    }

}
