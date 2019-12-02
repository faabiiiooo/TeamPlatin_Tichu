package server.controller;

import javafx.application.Platform;
import resources.*;
import server.model.Srv_Model;
import server.model.Srv_Table;
import resources.Player;

import java.util.ArrayList;
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

            case "card/playCard":  //generating a Card from Message when card should be played
                ArrayList<Card> cardsToPlay = new ArrayList<>();
                for(Object o : msgIn.getObjects()){
                    cardsToPlay.add((Card) o);
                }
                if(model.getGame().getTable().playCards(cardsToPlay)){
                    logger.info("Sending success Response");
                    msgOut = new MessageResponse("string","ok",msgIn.getMessageID());
                    model.removePlayedCardsFromPlayerHand(msgIn.getSenderID(),cardsToPlay);
                    model.sendTableCardsToClients();
                    model.sendPlayersToClients();
                    model.getGame().getTable().checkPlayerHandsOnBomb();
                    model.sendHasBombStatusToClients();
                    model.getGame().getTable().skip();
                    model.sendActivePlayerToClients();
                    for(Player p : serviceLocator.getTable().getPlayersAtTable()){ //if he skipped previosly and now can play
                        if(p.getClientID() == msgIn.getSenderID()){
                            serviceLocator.getTable().getPlayersThatSkipped().remove(p);
                        }
                    }

                } else {
                    msgOut = new MessageResponse("string", "n-ok", msgIn.getMessageID());
                }

                break;




            case "player":


                break;


            case "string":
                String incoming = (String) msgIn.getObjects().get(0);
                boolean skipProcessEnded = false;
                switch (incoming) {
                    case "skip": //@author Sandro
                        logger.info("Srv_processSkipButton");
                        int clientID = msgIn.getSenderID();
                        for (Player p : model.getGame().getTable().getPlayersAtTable()) { //Each Player at Table
                            if(p.getClientID() == msgIn.getSenderID()){ //add player that skipped to a list
                                if(p.equals(serviceLocator.getTable().getBeginner()) && serviceLocator.getTable().getPlayersThatSkipped().contains(p)){
                                    serviceLocator.getTable().getPlayersThatSkipped().clear();

                                }
                            }
                            if(!serviceLocator.getTable().getPlayersThatSkipped().contains(p)){
                                serviceLocator.getTable().getPlayersThatSkipped().add(p);
                            }

                            if (p.isActive() && !skipProcessEnded) {// Find activePlayer
                                if (p.isHasWishedCard()) { //Check: Must player play wishedCard of mahjong?
                                    logger.info("Skip not allowed - Player must play wished card!");
                                    msgOut = new MessageResponse("string", "n-ok", msgIn.getMessageID());
                                    skipProcessEnded = true;
                                } else {
                                    logger.info("Player can skip");
                                    this.serviceLocator.getTable().skip();
                                    msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                                    skipProcessEnded = true;
                                }
                            }
                            model.sendActivePlayerToClients(); //send new activePlayer to Client
                        }
                        break;
                    case "tichu"://@author Pascal
                        // TODO: 24.11.2019 ANtwort an den Client fehlt noch
                        for (Player p : model.getGame().getTable().getPlayersAtTable()) {//Each Player at Tabel
                            if (p.getPLAYER_ID() == msgIn.getSenderID()) {// Is the player ID equals to the Client ID
                                if (p.getHandCards().size() == 14) {//Check if Handcards equals 14
                                    p.setSaidSmallTichu(true);//Player is abel to call a small Tichu
                                    msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                                } else {
                                    if (p.getHandCards().size() <= 8) {//Check if Handcards <8
                                        p.setSaidBigTichu(true);//Player is abel to call a bigTichu
                                        msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                                    }
                                }
                            }

                        }
                        break;

                    case "bombActiveChange":
                        //set the active player to not active
                        int msgId = msgIn.getSenderID();
                        logger.info(msgId+" msgId bomb active change");
                        logger.info("Bomb: active status change");
                        for(Player p : model.getGame().getTable().getPlayersAtTable()){
                            if(p.isActive())
                                logger.info("Player who was active ID: "+p.getPLAYER_ID());
                                p.setActive(false);
                        }
                        //set the player who pressed the bomb button to the active player
                        for(Player p : model.getGame().getTable().getPlayersAtTable()){
                            if(p.getPLAYER_ID() == msgId){
                                p.setActive(true);
                                logger.info("Player ID: "+p.getPLAYER_ID() + "Sender ID: "+msgIn.getSenderID());
                                logger.info("Player who pressed bomb button is now active");
                                msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                                //model.sendActivePlayerToClients();
                            }
                        }
                        model.sendActivePlayerToClients();
                      /*  if (ok){
                            for(Player p : model.getGame().getTable().getPlayersAtTable()){
                                logger.info("Active status players: "+ p.getPLAYER_ID()+" Status "+p.isActive());
                            }
                            logger.info("Player who pressed bomb button is now active");
                            msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                            model.sendActivePlayerToClients();
                        }else{
                            logger.info("failes to change active player with bomb on hand");
                            msgOut = new MessageResponse("string", "n-ok", msgIn.getMessageID());
                        }*/
                        //send the active status to all clients


                        break;
                    }
                }

                return msgOut;
        }
    }
