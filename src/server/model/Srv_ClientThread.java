package server.model;

import resources.Message;
import resources.ServiceLocator;
import server.controller.Srv_Controller;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class Srv_ClientThread extends Thread { //Fabio

    private static int clientId = 0;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Srv_Server server;
    private Srv_Controller controller;

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();

    public Srv_ClientThread(Srv_Server server, InputStream in, OutputStream out) {
        super("ClientThread for Client " + clientId);
        controller = Srv_Controller.getController();

        try {
            this.server = server;
            this.in = new ObjectInputStream(in);
            this.out = new ObjectOutputStream(out);

            logger.info("Created ClientThread for Client: " + clientId);
            clientId++;
        } catch (IOException e){
            logger.severe("Couldn't start ClientThread.");
        }

    }

    public void run(){

        try{
            while (true){
                Message msgIn = this.receive();
                if(msgIn != null ){ //when a message received redirect it to the Server Controller /Fabio
                    //Message msgIn = this.receive();
                    logger.info("Message received from Client. Message Type: "+msgIn.getType());
                    Message msgOut = controller.processIncomingMessage(msgIn);
                    if(msgOut != null){
                        this.send(msgOut);
                        logger.info("Message sent back to Client");
                    }
                }

            }

        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private Message receive() throws Exception{ //recieving incoming Messages
        Message msgIn = null;

        msgIn = (Message) in.readObject();

        return msgIn;

    }

    private void send(Message msgOut) throws Exception { //sending outgoing Messages

        out.writeObject(msgOut);
        out.flush();
        out.reset();

    }

}
