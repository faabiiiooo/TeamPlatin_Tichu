package client.model;

import client.controller.Clt_Controller;
import resources.Message;
import resources.ServiceLocator;

import java.util.Scanner;

//@author Fabio
public class Test_Client {

    private static Clt_Controller controller;
    private static Clt_Client client;

    private static ServiceLocator sl = ServiceLocator.getServiceLocator();

    public static void main(String[] args){

        controller = sl.getCltController();
        client = new Clt_Client("127.0.0.1");
        client.start();

        String answer ="";
        Scanner scan = new Scanner(System.in);
        while(!answer.equals("stop")){
            System.out.println("Send Message?");
            answer = scan.nextLine();
            if(answer.equals("y")){
                String s1 = "s1";
                String s2 = "s2";
                String s3 = "s3";

                Message msg = new Message("card", s1,s2,s3);

                client.send(msg);
            }

        }


    }


}
