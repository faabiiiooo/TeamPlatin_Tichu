package server.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import resources.Message;
import resources.Player;
import resources.ServiceLocator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

//@author Fabio
public class Srv_Server extends Thread {

    private final int PORT = 10456;
    private final ObservableList<Socket> clientSockets = FXCollections.observableArrayList();
    private final ObservableList<Srv_ClientThread> clientThreads = FXCollections.observableArrayList();
    private ServerSocket listener;


    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();

    private boolean gameAlreadyStarted = false;


    //Creating Server Thread

    public Srv_Server(){
        super("Srv_ServerThread");
        logger.info("Srv_Server created.");
        serviceLocator.setServer(this);
        this.setDaemon(true); //autmatically close Thread when program halt.

    }

    public void run(){

        try(ServerSocket listener = new ServerSocket(PORT,10,null)){ //Make server Listen on port 10456
            logger.info("Server listening on port: "+ PORT);
            while(true){ //Create new Thread for each Client.
                Socket socket = listener.accept();
                clientSockets.add(socket);  //Keep ClientThreads alive
                Player newPlayer = new Player();
                Srv_ClientThread client = new Srv_ClientThread(this,socket.getInputStream(),socket.getOutputStream(), newPlayer.getPLAYER_ID());
                newPlayer.setClientID(client.getID());
                serviceLocator.getTable().addPlayerToTable(newPlayer); //ad the newly generated player to the table
                clientThreads.add(client);
                client.start();
                if(clientThreads.size() == 4 && !gameAlreadyStarted){ //start game when 4 clients are connected.
                    logger.info("Going to start first Round!");
                    this.startGame();
                }

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

    private void startGame(){
        serviceLocator.getSrvModel().startGame();
        this.gameAlreadyStarted = true;
    }

    public int searchIndexOfClientThreadByID(int id){
        int index = -1;
        for(int i = 0; i < this.clientThreads.size(); i++){
            if(clientThreads.get(i).getID() == id) {
                index = i;
            }
        }
        return index;
    }

    public ObservableList<Srv_ClientThread> getClientThreads() {
        return clientThreads;
    }
}
