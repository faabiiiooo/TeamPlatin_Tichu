package client.model;

//Basic Class to Start Building the Model -> no functuality

import resources.Message;
import resources.ServiceLocator;
import server.Tichu_Srv;

public class Clt_Model {

    private Clt_Client client;
    private final ServiceLocator sl = ServiceLocator.getServiceLocator();


    public Clt_Model() {

       /* // TODO: 10.11.2019
        client = new Clt_Client("127.0.0.1");
        client.start();*/

    }

    //@author Fabio
    public void startServer(){ //Start Server if user wants to be Server
        Tichu_Srv.main(new String[]{}); //always start client on localhost if he wants to be server

    }

    //@author Fabio
    public void startClient(String serverIP){  //start client
        client = new Clt_Client(serverIP);
        client.start();
        sl.setClient(client);
    }

}
