package client.model;

//Basic Class to Start Building the Model -> no functuality

import resources.Message;

public class Clt_Model {

    Clt_Client client;

    public Clt_Model() {

        // TODO: 10.11.2019
        client = new Clt_Client("127.0.0.1");
        client.start();


    }

}
