package server.model;

import resources.Message;
import server.controller.Srv_Controller;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


//@author Fabio
public class Test_Server {

    public static void main(String[] args){


        Scanner scan  = new Scanner(System.in);
        String answer = "";
        server.start();
        while(!answer.equals("stop")){
            System.out.println("Broadcast senden?");
            answer = scan.nextLine();
            if(answer.equals("y")){
                Message msgOut = generateMessage();
                    testBroadcast(msgOut);
                    //sendMessage(msgOut);

            }
        }


    }

    public static Socket s;

    public static Srv_Controller controller = Srv_Controller.getController();
    public static Srv_Server server = new Srv_Server();

    public static void testBroadcast(Message msgOut)  {
            server.broadcast(msgOut);



    }


    private static void sendMessage(Message msgOut){

        try{
            s = new Socket("127.0.0.1", 10456);
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(msgOut);
            out.flush();


        } catch (Exception e){
            System.err.println(e);
        }



    }

    private static Message generateMessage(){
        String s1 = "Test1";
        String s2 = "Test2";

        Message msgOut = new Message("card",s1,s2);

        return msgOut;
    }

    private static void recieveMessage(){




    }

}
