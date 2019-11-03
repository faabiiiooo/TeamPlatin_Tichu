package server.model;

import resources.Message;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Test_Server {

    public static void main(String[] args){
        sendMessage();
    }

    public static Socket s;

    private static void sendMessage(){
        String s1 = "Test1";
        String s2 = "Test2";

        Message msgOut = new Message("card",s1,s2);

        try{
            s = new Socket("127.0.0.1", 10456);
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(msgOut);
            out.flush();


        } catch (Exception e){
            System.err.println(e);
        }



    }

    private static void recieveMessage(){




    }

}
