package server.controller;

import javafx.application.Platform;
import resources.*;
import server.model.Srv_HandType;
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

    private void standardProcessPlayCards(int senderID, ArrayList<Card> cardsToPlay){
        logger.info("standardProcessPlayCards");
        model.removePlayedCardsFromPlayerHand(senderID,cardsToPlay);
        model.sendTableCardsToClients();
        model.sendPlayersToClients();
        model.getGame().getTable().checkPlayerHandsOnBomb(); //check all hands on possible bombs
        model.sendHasBombStatusToClients();//send status to client
        model.getGame().getTable().skip();
        model.sendActivePlayerToClients();
        model.getGame().getTable().checkIfMJWishIsActive(); //check if the mj wish is active


    }


    //@author Fabio
    public Message processIncomingMessage(Message msgIn) { // Generates Answermessage for every Incoming Message

        Message msgOut = null;

        switch (msgIn.getType()) {

            case "card/playCard":  //generating a Card from Message when card should be played
                boolean playsWishedCard = false;
                ArrayList<Card> cardsToPlay = new ArrayList<>();
                Player xy = null;
                for(Object o : msgIn.getObjects()){
                    cardsToPlay.add((Card) o);
                }
                //get the player who want to play the cards
                for(Player b: model.getGame().getTable().getPlayersAtTable() ){
                    if(b.getPLAYER_ID() == msgIn.getSenderID()){
                        xy = b;

                    }
                }



                if(!xy.isHasWishedCard() &&!xy.isWantBomb() ){
                    logger.info("Player wants to play: "+cardsToPlay );
                    if(model.getGame().getTable().playCards(cardsToPlay)) {
                        msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                        logger.info("Sending success Response");
                        model.removePlayedCardsFromPlayerHand(msgIn.getSenderID(), cardsToPlay);
                        model.sendTableCardsToClients();
                        model.sendPlayersToClients();
                        model.getGame().getTable().checkPlayerHandsOnBomb();
                        model.sendHasBombStatusToClients();

                        if (model.getGame().getTable().getLastPlayedCards().get(0).getRank() != Rank.Dog
                                && model.getGame().getTable().getLastPlayedCards().get(0).getRank() != Rank.Mahjong) { //special case if dog played (skip process is include in dogPlayed)
                            model.getGame().getTable().skip(); //normal skip if no dog played
                        } else {
                            if (cardsToPlay.get(0).getRank() == Rank.Mahjong) {
                                logger.info("Client: " + msgIn.getSenderID() + " want top open wishview --> model ");
                                if (cardsToPlay.get(0).getRank() == Rank.Mahjong) {
                                    int senderID = msgIn.getSenderID();
                                    model.openClientWishView(senderID);
                                }
                            }

                        }
                    }else{
                        msgOut = new MessageResponse("string", "n-ok", msgIn.getMessageID());
                    }

                    //only skip if the player didnt play the mah jong - if he played mah jong he first needs to wish a card before skipping

                    model.getGame().getTable().checkPlayerHandsOnBomb(); //check all hands on possible bombs
                    model.sendHasBombStatusToClients();//send status to client
                    model.sendActivePlayerToClients();
                    model.getGame().getTable().checkIfMJWishIsActive();

                } else if(xy.isWantBomb() && Srv_HandType.isBomb(cardsToPlay) ) { //if the player wants to bomb, check if the played cards is a bomb and higher than the last played cards
                    if(model.getGame().getTable().playCards(cardsToPlay))
                        logger.info("Case Bomb: Sending success Response");
                        msgOut = new MessageResponse("string","ok",msgIn.getMessageID());
                        standardProcessPlayCards(msgIn.getSenderID(), cardsToPlay);
                        xy.setWantBomb(false);

                }else if(xy.isHasWishedCard()){// we need to check if the player played the wished card
                    ArrayList<Card> clonedCards = (ArrayList<Card>) cardsToPlay.clone();
                    if(model.checkIfWishedCardIsInPlayedCards(clonedCards) ){
                        logger.info("Case Wished Cards");
                        if(model.getGame().getTable().playCards(cardsToPlay)) { //is the card higher than the one played before?
                            logger.info("Case Wished Cards: Sending success Response");
                            msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                            standardProcessPlayCards(msgIn.getSenderID(), cardsToPlay);
                        }
                    }else {
                        msgOut = new MessageResponse("string", "n-ok", msgIn.getMessageID());
                    }

                }else {
                    msgOut = new MessageResponse("string", "n-ok", msgIn.getMessageID());
                }



                break;




            case "player":


                break;
            //@author thomas
            case "card/wishCard": //setting the wished card from the player
                model.getGame().getTable().setMahJongWishCard((Card)msgIn.getObjects().get(0));
                logger.info("RECEIVED WISHED CARD FROM CLIENT: "+ ((Card)msgIn.getObjects().get(0)));
                msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                logger.info("Wished card has been set on Table : " + model.getGame().getTable().getMahJongWishCard().getRank());
                model.getGame().getTable().mahJongPlayed();
                model.getGame().getTable().skip();
                model.sendActivePlayerToClients();
                break;
            case "string":
                String incoming = (String) msgIn.getObjects().get(0);
                boolean skipProcessEnded = false;
                switch (incoming) {
                    case "skip": //@author Sandro
                        logger.info("Srv_processSkipButton");
                        for (Player p : model.getGame().getTable().getPlayersAtTable()) { //Each Player at Table
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
                    //@author thomas
                    case "bombActiveChange":
                        //set the active player to not active
                        int msgId = msgIn.getSenderID();
                        logger.info(msgId+" msgId bomb active change");
                        logger.info("Bomb: active status change");
                        if(model.getGame().getTable().getLastPlayedCards().get(0).getRank() != Rank.Dog ) {//can only bomb if there wasnt played a dog


                            for (Player p : model.getGame().getTable().getPlayersAtTable()) {
                                if (p.isActive())
                                    logger.info("Player who was active ID: " + p.getPLAYER_ID());
                                p.setActive(false);
                            }
                            //set the player who pressed the bomb button to the active player
                            for (Player p : model.getGame().getTable().getPlayersAtTable()) {
                                if (p.getPLAYER_ID() == msgId) {
                                    p.setActive(true);
                                    p.setWantBomb(true); //set to true, that the played out cards can be checked if they are a bomb
                                    logger.info("Player ID: " + p.getPLAYER_ID() + "Sender ID: " + msgIn.getSenderID());
                                    logger.info("Player who pressed bomb button is now active");
                                    msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                                    model.sendActivePlayerToClients();
                                }
                            }
                            model.sendActivePlayerToClients();
                        }
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
