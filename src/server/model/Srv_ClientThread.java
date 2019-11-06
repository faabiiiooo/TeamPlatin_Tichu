package server.model;

import resources.Message;
import resources.ServiceLocator;
import server.controller.Srv_Controller;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.util.logging.Logger;

/*
@author Fabio
 */
public class Srv_ClientThread extends Thread {

    private static int id_generator = 0;
    private final int ID;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Srv_Server server;
    private Srv_Controller controller;

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();


    public Srv_ClientThread(Srv_Server server, InputStream in, OutputStream out) {
        super("ClientThread for Client " + id_generator);
        controller = Srv_Controller.getController();
        ID = id_generator;
        id_generator++;

        try {
            this.server = server;
            this.in = new ObjectInputStream(in);
            this.out = new ObjectOutputStream(out);

            logger.info("Created ClientThread for Client: " + ID);
        } catch (IOException e){
            logger.severe("Couldn't start ClientThread.");
        }

    }

    public void run(){

        try{
            while (true){
                Message msgIn = this.receive();
                if(msgIn != null ){ //when a message received redirect it to the Server Controller
                    logger.info("Message received from Client. Message Type: "+msgIn.getType());
                    Message msgOut = controller.processIncomingMessage(msgIn); //generating answer to send back to Client
                    if(msgOut != null){
                        this.send(msgOut);
                        logger.info("Message sent back to Client");
                    }
                }
            Thread.sleep(2);
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

    public void send(Message msgOut) throws Exception { //sending outgoing Messages

        out.writeObject(msgOut);
        out.flush();

    }

    public int getID() {
        return ID;
    }

    public ObjectOutputStream getOut() {
        return out;
    }
}


