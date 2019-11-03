package server.model;

import resources.Message;
import resources.ServiceLocator;
import server.controller.Srv_Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class Srv_ClientThread extends Thread { //Fabio

    private static int clientId = 0;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Srv_Server server;
    private Srv_Controller controller;

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();

    public Srv_ClientThread(Srv_Server server, InputStream in) {
        super("ClientThread for Client " + clientId);
        controller = Srv_Controller.getController();

        try {
            this.server = server;
            this.in = new ObjectInputStream(in);
            //this.out = new ObjectOutputStream();

            logger.info("Created ClientThread for Client: " + clientId);
            clientId++;
        } catch (IOException e){
            logger.severe("Couldn't start ClientThread.");
        }

    }

    public void run(){

        try{
            while (true){
                if(this.receive() != null ){ //when a message received redirect it to the Server Controller /Fabio
                    Message msgIn = this.receive();
                    logger.info("Message received from Client. Message Type: "+msgIn.getType());
                    Message msgOut = controller.processIncomingMessage(msgIn);
                    if(msgOut != null){
                        this.send(msgOut);
                    }
                }

            }

        } catch (Exception e){
        }


    }

    private Message receive() throws Exception{
        Message msgIn = null;

        msgIn = (Message) in.readObject();

        return msgIn;

    }

    private void send(Message msgOut) throws Exception {


    }

}
