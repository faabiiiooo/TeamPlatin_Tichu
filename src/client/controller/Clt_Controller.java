package client.controller;

import client.model.Clt_Model;
import client.view.Clt_View;
import javafx.stage.Stage;
import resources.Message;
import resources.ServiceLocator;

import java.util.logging.Logger;

//Controllerclass for Client Part of the Game


public class Clt_Controller { //Controller is a Singleton

    private Stage primaryStage;
    private Clt_View view;
    private Clt_Model model;
    private ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private Logger logger = serviceLocator.getLogger();
    private static Clt_Controller controller;

    public static Clt_Controller getController(){
        if(controller == null){
            controller = new Clt_Controller();
        }
        return controller;
    }

    private Clt_Controller(){//private constructor becaus Srv_Controller is a singleton

    }

    //@author Fabio
    public void processIncomingMessage(Message msgIn) { // Generates Answermessage for every Incoming Message

        switch (msgIn.getType()) {

            case "card":  //generating a Card from Message

                break;

            case "player":


                break;

            case "string":

                break;

            case "connection-lost": //stop game. A client got disconnected.
                logger.warning("Client is going to stop becaus of connection loss");


                break;

        }

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setView(Clt_View view) {
        this.view = view;
    }

    public void setModel(Clt_Model model) {
        this.model = model;
    }
}
