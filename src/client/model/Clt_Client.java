package client.model;

import client.controller.Clt_Controller;
import resources.Message;
import resources.ServiceLocator;
import server.model.Srv_ClientThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;


public class Clt_Client extends Thread{

    private final int PORT=10456;
    private Socket socket;
    private String serverIP;
    private Clt_Controller controller;
    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();


    public Clt_Client(String serverIP,int PORT){
        try{
            socket=new Socket(serverIP,PORT);

        } catch (IOException e) {
            logger.severe("Socket could not be created");
        }

    }
    @Override
    public void run(){
        try{
            while (true){
                if(this.receive() != null ){
                    Message msgIn = this.receive();
                    logger.info("Message received from Server. Message Type: "+msgIn.getType());
                   controller.processIncomingMessage(msgIn);
                    }
                }



        } catch (Exception e){
        }


    }

        private  Message receive () throws Exception { // Recive Messages from Server /Pascal


        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Message msgIn = (Message) in.readObject();
            return msgIn;


        }

        public void send(Message msgOut){ // Send Messages to the Server /Pascal

        try{
            ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(msgOut);
            out.flush();


        } catch (IOException e) {
           logger.severe("Could not send a Message");
        }

        }
    }

