package client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Clt_Client extends Thread{

    private final int PORT=10456;
    private Socket socket;
    private String serverIP;


    public Clt_Client(String serverIP,int PORT){
        try{
            socket=new Socket(serverIP,PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void run(){
        try (ObjectInputStream in =new ObjectInputStream(socket.getInputStream())){
            ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
