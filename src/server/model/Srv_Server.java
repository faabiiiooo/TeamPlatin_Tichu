package server.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import resources.Message;
import resources.ServiceLocator;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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




    }

    public void run(){

        try(ServerSocket listener = new ServerSocket(PORT,10,null)){ //Make server Listen on port 10456
            logger.info("Server listening on port: "+ PORT);
            while(true){ //Create new Thread for each Client.
                Socket socket = listener.accept();
                clientSockets.add(socket);  //Keep ClientThreads alive
                Srv_ClientThread client = new Srv_ClientThread(this,socket.getInputStream(),socket.getOutputStream());
                clientThreads.add(client);
                client.start();
            }

        } catch (IOException e){
            logger.severe("Server not starting." + e.toString());
        }

    }

    public void broadcast(Message msgOut){
        try {
            for (Srv_ClientThread c : clientThreads) {
                    c.send(msgOut);
                    logger.info("Sent message to ClientThread with ID: " + c.getID());

            }
        } catch (Exception e){
            logger.info("Can't send broadcast to Client Thread");
        }
    }
}
