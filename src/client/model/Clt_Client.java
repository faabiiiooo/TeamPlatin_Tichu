package client.model;

import client.controller.Clt_Controller;
import resources.Message;
import resources.ServiceLocator;

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

    private ObjectOutputStream out;
    private ObjectInputStream in;


    public Clt_Client(String serverIP){
        super("Client");
        this.serverIP = serverIP;
        this.controller = serviceLocator.getCltController();
        try{
            socket=new Socket(this.serverIP,PORT);

                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                this.setDaemon(true); //autmatically close Thread when program gets halted.



        } catch (IOException e) {
            logger.severe("Socket could not be created");
        }

    }
    @Override
    public void run(){
        try{
            while (true){
                Message msgIn = this.receive();
                if(msgIn != null ){
                    logger.info("Message received from Server. Message Type: "+msgIn.getType());
                      controller.processIncomingMessage(msgIn);

                    }
                }



        } catch (Exception e){
            logger.severe("Error while retrieving messages");
        }


    }

        private Message receive () throws Exception { // Recive Messages from Server /Pascal

                Message msgIn = (Message) in.readObject();

                return msgIn;



        }

        public void send(Message msgOut){ // Send Messages to the Server /Pascal

        try{
                out.writeObject(msgOut);
                out.flush();
                logger.info("Message sent");

        } catch (IOException e) {
           logger.severe("Could not send a Message");
        }

        }
    }

