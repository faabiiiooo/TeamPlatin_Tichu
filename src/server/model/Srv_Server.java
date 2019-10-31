package server.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import resources.ServiceLocator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Logger;

public class Srv_Server extends Thread {

    private final int PORT = 10456;
    private final ObservableList<Socket> clientSockets = FXCollections.observableArrayList();
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

            while(true){ //Create new Thread for each Client.
                Socket socket = listener.accept();
                Srv_ClientThread client = new Srv_ClientThread(socket);
                clientSockets.add(socket);
                client.start();
            }

        } catch (IOException e){

        }

    }
}
