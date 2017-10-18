import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RemoteClass extends UnicastRemoteObject implements RemoteClassInterface {

    protected RemoteClass() throws RemoteException {
        super();
    }

    public String sendThisBack(String data) throws RemoteException {
      for (int i=1; i <= 5; i++) {
          System.out.println("Me llego algo de: "+data+" a la "+i);
          try {
              Thread.sleep(3000);
          } catch(InterruptedException ex) {
              Thread.currentThread().interrupt();
          }
      }
      System.out.println("Termine con "+data);
      return data;
    }

}
