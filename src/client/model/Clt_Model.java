package client.model;



import resources.Message;
import resources.MessageStats;
import resources.ServiceLocator;


import java.util.logging.Logger;

//@author Fabio
public class Clt_Model {

    private Clt_Client client;
    private final ServiceLocator sl = ServiceLocator.getServiceLocator();
    private final Clt_DataStore dataStore;
    private final Logger logger = sl.getLogger();


    public Clt_Model() {
        dataStore = Clt_DataStore.getDataStore();

    }

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
        client.send(msgOut); //sends the message to te server
        dataStore.addMessageToQueue(msgOut);
        msgOut.setMessageStatus(MessageStats.inEvaluation); //set status of message to inEvaluation
        logger.info("Sent message to Server, waiting for Response...");

        while(msgOut.getMessageStatus() == MessageStats.inEvaluation){ //waiting for response
            logger.info("Waiting...");
        }
        if(msgOut.getMessageStatus() == MessageStats.accepted){
            logger.info("Message got accepted!");
            successful = true;
        }

        logger.info("Response received from Server.");

        return successful;

    }

    public Clt_DataStore getDataStore() {
        return dataStore;
    }
}
