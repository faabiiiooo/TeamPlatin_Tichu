package server.controller;

import resources.Message;
import resources.ServiceLocator;
import server.model.Srv_Model;
import server.model.Srv_Player;

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
    public Message processIncomingMessage(Message msgIn) { // Generates Answermessage for every Incoming Message

        Message msgOut = null;

        switch (msgIn.getType()) {

            case "card":  //generating a Card from Message

                break;

            case "player":


                break;

            case "string":
                String incoming = (String) msgIn.getObjects().get(0);
                switch (incoming) {
                    case "skip": //@author Sandro
                        logger.info("Srv_processSkipButton");
                        for (Srv_Player p : model.getGame().getTable().getPlayersAtTable()) { //Each Player at Table
                            if (p.isActive()) {// Find activePlayer
                                if (p.isHasWishedCard()) { //Check: Must player play wishedCard of mahjong?
                                    logger.info("Skip not allowed - Player must play wished card!");
                                } else {
                                    logger.info("Player can skip");
                                    this.serviceLocator.getTable().skip();
                                }
                            }
                        }
                        break;
                    case "tichu"://Pascal
                        // TODO: 24.11.2019 ANtwort an den Client fehlt noch
                        for (Srv_Player p : model.getGame().getTable().getPlayersAtTable()) {//Each Player at Tabel
                            if (p.getPLAYER_ID() == msgIn.getSenderID()) {// Is the player ID equals to the Client ID
                                if (p.getHandCards().size() == 14) {//Check if Handcards equals 14
                                    p.setSaidSmallTichu(true);//Player is abel to call a small Tichu
                                } else {
                                    if (p.getHandCards().size() <= 8) {//Check if Handcards <8
                                        p.setSaidBigTichu(true);//Player is abel to call a bigTichu
                                    }
                                }
                            }
                        }
                        break;
                    }
                }

                return msgOut;
        }
    }
