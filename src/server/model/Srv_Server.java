package server.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import resources.Message;
import resources.ServiceLocator;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.logging.Logger;

//@author Fabio
public class Srv_Server extends Thread {

    private final int PORT = 10456;
    private final ObservableList<Socket> clientSockets = FXCollections.observableArrayList();
    private final ObservableList<Srv_ClientThread> clientThreads = FXCollections.observableArrayList();
    private ServerSocket listener;


    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();


    //Creating Server Thread

    public Srv_Server(){
        super("Srv_ServerThread");
        logger.info("Srv_Server created.");
        this.setDaemon(true); //autmatically close Thread when program halt.

    }

    public void run(){

        try(ServerSocket listener = new ServerSocket(PORT,10,null)){ //Make server Listen on port 10456
            logger.info("Server listening on port: "+ PORT);
            while(true){ //Create new Thread for each Client.
                Socket socket = listener.accept();
                clientSockets.add(socket);  //Keep ClientThreads alive
                Srv_Player newPlayer = new Srv_Player();
                Srv_ClientThread client = new Srv_ClientThread(this,socket.getInputStream(),socket.getOutputStream(), newPlayer.getPLAYER_ID());
                newPlayer.setClientID(client.getID());
                clientThreads.add(client);
                client.start();

            }

        } catch (IOException e){
            logger.severe("Server not starting." + e.toString());
        }

    }

    public void broadcast(Message msgOut){  //send a broadcast to all connected clients
        try {

            if(this.searchDisconnectedClients()){  //first check if there is a disconnected client
                this.clientDisconnected();

            }

            for (Srv_ClientThread c : clientThreads) {
                    c.send(msgOut);

                    logger.info("Sent message to ClientThread with ID: " + c.getID());

            }
        } catch (Exception e){
            logger.info("Can't send broadcast to Client Thread");
            //e.printStackTrace();
        }
    }

    protected void clientDisconnected(){  //sends stop message to clients

        logger.warning("Client got disconnected. Game is going to stop");

        String s1 = "s1";

        Message stopMsg = new Message("connection-lost", s1);

        this.broadcast(stopMsg);
    }

    private boolean searchDisconnectedClients(){ //finds closed Client threads, and removes them from server

       ArrayList<Srv_ClientThread> stoppedClients = new ArrayList<>(); //temporary List for inactive Clients
       boolean stoppedClientFound = false;

       for (Srv_ClientThread c : clientThreads){

           if(!c.isAlive()){  //if ClientThread is death ad to temporary list
               stoppedClients.add(c);
               stoppedClientFound = true;
           }
       }

       if(stoppedClientFound){
           for(Srv_ClientThread c : stoppedClients){ //remove death ClientThread, so that broadcasting still works
               clientThreads.remove(c);
           }
       }

       return stoppedClientFound;

    }
}
