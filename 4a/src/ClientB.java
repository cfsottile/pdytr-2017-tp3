import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.rmi.Naming;
import java.rmi.registry.Registry;

import static java.lang.System.exit;

public class ClientB {
    static final int BUF_SIZE = 1024;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("args: server_host file");
            exit(1);
        }
        String host = args[0];
        String filename = args[1];

        String remoteName = "//" + host + ":" + Registry.REGISTRY_PORT + "/remote";
        try {
            IRemoteServer server = (IRemoteServer) Naming.lookup(remoteName);
            readFromServer(server, filename);
            writeToServer(server, "copia_" + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readFromServer(IRemoteServer server, String filename) throws Exception {
        RandomAccessFile file = new RandomAccessFile("copia_" + filename, "rw");
        Pair<byte[],Integer> readPair;
        int pos = 0;

        readPair = server.read(filename, pos, BUF_SIZE);
        file.write(readPair.getKey(), (int) file.getChannel().size(), readPair.getValue());
        while (readPair.getValue() == BUF_SIZE) {
            pos += readPair.getValue();
            readPair = server.read(filename, pos, BUF_SIZE);
            file.write(readPair.getKey(), 0, readPair.getValue());
        }
        file.close();
    }

    private static void writeToServer(IRemoteServer server, String filename) throws Exception {
        FileInputStream file = new FileInputStream(filename);
        byte[] data = new byte[BUF_SIZE];

        int readBytes = file.read(data);
        server.write(filename, readBytes, data);
        while (readBytes == BUF_SIZE) {
            readBytes = file.read(data);
            server.write(filename, readBytes, data);
        }
        file.close();
    }
}
