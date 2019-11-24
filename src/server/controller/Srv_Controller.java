package server.controller;

import resources.Message;
import resources.ServiceLocator;
import server.model.Srv_Model;

import java.util.logging.Logger;

public class Srv_Controller { //Servercontroller is generated as a Singleton

    private Srv_Model model;
    private static Srv_Controller controller;
    private ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private Logger logger = serviceLocator.getLogger();

    //@author Fabio
    public static Srv_Controller getController(){
        if(controller == null){
            controller = new Srv_Controller();
        }
        return controller;
    }

    public void setModel(Srv_Model model){
        this.model = model;
    }


    //@author Fabio
    private Srv_Controller(){ //private constructor becaus Srv_Controller is a singleton

    }

    //@author Fabio
    public Message processIncomingMessage(Message msgIn){ // Generates Answermessage for every Incoming Message

        Message msgOut = null;

        switch (msgIn.getType()){

            case "card":  //generating a Card from Message

            break;

            case "player":


            break;

            case "string":
                String incoming = (String) msgIn.getObjects().get(0);
                switch (incoming) {
                    case "skip": //@author Sandro
                        logger.info("Srv_processSkipButton");

                        for(int i = 0; i < this.serviceLocator.getTable().getSeats().size(); i++) {
                            if (this.serviceLocator.getTable().getSeats().get(i).getPlayer().isActive()) { //Search isActivePlayer
                                if (this.serviceLocator.getTable().getSeats().get(i).getPlayer().isHasWishedCard()) { //Case: Player want to skip, but has wishedCard of mahjong
                                    logger.info("Player cant skip -> Play the wished card!");
                                } else {
                                    logger.info("Player can skip");
                                    this.serviceLocator.getTable().skip();
                                }
                            }
                        }
                        break;
                }

            break;

        }

        return msgOut;
    }
}
