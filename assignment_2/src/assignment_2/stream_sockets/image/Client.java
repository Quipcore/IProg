package assignment_2.stream_sockets.image;

import java.io.*;
import java.net.Socket;

public class Client {

//    private static final String HOST = "atlas.dsv.su.se";
//    private static final int PORT = 4848;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 2000;

    public static void main(String[] args) {
        try(Socket socket = new Socket(HOST, PORT)){
            System.out.printf("Socket is connected:%s\n",socket.isConnected());
            System.out.printf("Socket is bound:%s\n",socket.isBound());
            System.out.printf("Connected to %s:%d\n", HOST, PORT);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeUTF("hello World!");
            Object object = new Storage(new byte[]{0x11, 0x13});
            out.writeObject(object);

            Storage storage = null;
            while(storage == null) {
                System.out.println("Storage is null!");
                InputStream inputStream = socket.getInputStream();
                System.out.println("Created in stream!");
                ObjectInputStream in = new ObjectInputStream(inputStream);
                System.out.println("Created Object in stream!");
                storage = (Storage) in.readObject();
            }

            print(storage.getData());

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void print(byte[] data) {
        for(byte b : data){
            System.out.println((char)b);
        }
    }


}
