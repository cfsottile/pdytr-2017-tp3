import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RemoteClass extends UnicastRemoteObject implements RemoteClassInterface {

    protected RemoteClass() throws RemoteException {
        super();
    }

    public String sendThisBack(String data) throws RemoteException {
        System.out.println("Arranco a esperar.");
        try {
            Thread.sleep(10000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Termine de esperar.");
        return "ES EL FIN DEL MUNDO!!";
    }

}
