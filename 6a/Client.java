import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.util.Calendar;
import java.util.Arrays;

public class Client {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("1 argument needed: (remote) hostname");
            System.exit(1);
        }

        try {
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            RemoteClassInterface remote = (RemoteClassInterface) Naming.lookup(rname);

            byte[] data = new byte[1024*1024];

            for (int i = 0; i < 10; i++) {
                Calendar time1 = Calendar.getInstance();
                remote.sendThisBack(data);
                Calendar time2 = Calendar.getInstance();
                System.out.println((time2.getTimeInMillis() - time1.getTimeInMillis())+" ms");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
