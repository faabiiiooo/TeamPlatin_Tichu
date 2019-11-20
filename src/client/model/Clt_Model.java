package client.model;

//Basic Class to Start Building the Model -> no functuality

import javafx.collections.ObservableList;
import resources.Message;
import resources.MessageStats;
import resources.ServiceLocator;
import server.Tichu_Srv;

public class Clt_Model {

    private Clt_Client client;
    private final ServiceLocator sl = ServiceLocator.getServiceLocator();
    private final Clt_DataStore dataStore;


    public Clt_Model() {
        dataStore = Clt_DataStore.getDataStore();

    }

    //@author Fabio
    public void startServer(){ //Start Server if user wants to be Server
        Tichu_Srv.main(new String[]{});

    }

    //@author Fabio
    public void startClient(String serverIP){  //start client
        client = new Clt_Client(serverIP);
        client.start();
        sl.setClient(client);
    }

    public Message createMessage(String type, Object ... objects){

        return new Message(type, objects);

    }

    public boolean sendMessage(Message msgOut){
        boolean successful = false;
        client.send(msgOut);
        dataStore.addMessageToQueue(msgOut);
        msgOut.setMessageStatus(MessageStats.inEvaluation); //set status of message to inEvaluation
        while (msgOut.getMessageStatus() == MessageStats.inEvaluation){ //wait until Message isn't anymore in evaluation

        }
        if(msgOut.getMessageStatus() == MessageStats.accepted){ //if new status is accepted, return true, else return false
            successful = true;
        }

        return successful;

    }


}
