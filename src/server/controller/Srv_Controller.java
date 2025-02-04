package server.controller;


import resources.*;
import server.model.Srv_HandType;
import server.model.Srv_Model;
import server.model.Srv_Round;
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

    //@author Thomas
    private void standardProcessPlayCards(int senderID, ArrayList<Card> cardsToPlay){
        logger.info("standardProcessPlayCards");
        model.removePlayedCardsFromPlayerHand(senderID,cardsToPlay);
        model.sendTableCardsToClients();
        model.sendPlayersToClients();
        model.getGame().getTable().checkPlayerHandsOnBomb(); //check all hands on possible bombs
        model.sendHasBombStatusToClients();//send status to client
        model.getGame().getTable().skip();
        model.sendPlayersToClients();
        model.sendActivePlayerToClients();
        if(!model.getGame().getTable().isWishCardPlayedOut()){
            model.getGame().getTable().checkIfMJWishIsActive();
        }

    }

    public Message processIncomingMessage(Message msgIn) { // Generates Answermessage for every Incoming Message

        Message msgOut = null;

        switch (msgIn.getType()) {

            //@author Fabio, Thomas
            case "card/playCard":  //generating a Card from Message when card should be played
                boolean playsWishedCard = false;
                ArrayList<Card> cardsToPlay = new ArrayList<>();
                Player playingPlayer = null;
                for(Object o : msgIn.getObjects()){
                    cardsToPlay.add((Card) o);
                }
                //get the player who want to play the cards
                for(Player p: model.getGame().getTable().getPlayersAtTable() ){
                    if(p.getPLAYER_ID() == msgIn.getSenderID()){
                        playingPlayer = p;

                    }
                }

                if(!playingPlayer.isHasWishedCard() &&!playingPlayer.isWantBomb() ){
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
                            model.sendPlayersToClients();
                        } else {
                            if (cardsToPlay.get(0).getRank() == Rank.Mahjong && playingPlayer.getHandCards().size() != 0) {
                                logger.info("Client: " + msgIn.getSenderID() + " want top open wishview --> model ");
                                if (cardsToPlay.get(0).getRank() == Rank.Mahjong) {
                                    int senderID = msgIn.getSenderID();
                                    model.openClientWishView(senderID);
                                }
                            }

                        }
                        if(!serviceLocator.getTable().getPlayersThatSkipped().isEmpty()){ //belongs to sting, if player that skipped previously plays, everyone can replay.
                            serviceLocator.getTable().getPlayersThatSkipped().clear();
                        }
                    }else{
                        msgOut = new MessageResponse("string", "n-ok", msgIn.getMessageID());
                    }

                    model.getGame().getTable().checkPlayerHandsOnBomb(); //check all hands on possible bombs
                    model.sendHasBombStatusToClients();//send status to client
                    model.sendActivePlayerToClients();
                    model.sendPlayersToClients();
                    if(!model.getGame().getTable().isWishCardPlayedOut()){
                        model.getGame().getTable().checkIfMJWishIsActive();
                    }
                    for(Player p : serviceLocator.getTable().getPlayersAtTable()){ //if he skipped previously and now can play
                        if(p.getClientID() == msgIn.getSenderID()){
                            serviceLocator.getTable().getPlayersThatSkipped().remove(p);
                        }
                    }

                } else if(playingPlayer.isWantBomb() && Srv_HandType.isBomb(cardsToPlay) ) { //if the player wants to bomb, check if the played cards is a bomb and higher than the last played cards
                    if(model.getGame().getTable().playCards(cardsToPlay))
                        logger.info("Case Bomb: Sending success Response");
                        msgOut = new MessageResponse("string","ok",msgIn.getMessageID());
                        standardProcessPlayCards(msgIn.getSenderID(), cardsToPlay);
                        playingPlayer.setWantBomb(false);

                }else if(playingPlayer.isHasWishedCard()){// we need to check if the player played the wished card
                    ArrayList<Card> clonedCards = (ArrayList<Card>) cardsToPlay.clone();
                    if(model.checkIfWishedCardIsInPlayedCards(clonedCards) ){
                        logger.info("Case Wished Cards");
                        if(model.getGame().getTable().playCards(cardsToPlay)) { //is the card higher than the one played before?
                            model.clientInfoWishedCardPlayed(true);
                            logger.info("Case Wished Cards: Sending success Response");
                            msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                            standardProcessPlayCards(msgIn.getSenderID(), cardsToPlay);
                        }else {
                            msgOut = new MessageResponse("string", "n-ok", msgIn.getMessageID());
                        }
                    }else {
                        model.clientInfoWishedCardPlayed(false);
                        msgOut = new MessageResponse("string", "n-ok", msgIn.getMessageID());
                    }

                }else {
                    msgOut = new MessageResponse("string", "n-ok", msgIn.getMessageID());
                }
                break;

            //@author Thomas
            case "card/wishCard": //setting the wished card from the player
                model.getGame().getTable().setMahJongWishCard((Card)msgIn.getObjects().get(0));
                msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                logger.info("Wished card has been set on Table : " + model.getGame().getTable().getMahJongWishCard().getRank());
                model.getGame().getTable().skip();
                model.getGame().getTable().mahJongPlayed();
                model.sendActivePlayerToClients();
                model.sendPlayersToClients();
                model.sendWishedCardToClients();
                break;

            //@author Fabio
            case "string/finished":
                logger.info("Player " + msgIn.getSenderID() + " finished the round");

                Srv_Round thisRound = model.getGame().getRounds().get(model.getGame().getRounds().size()-1);
                int senderID = msgIn.getSenderID();

                if(thisRound.getFinisher().size() < 3){
                    for(Player p : model.getGame().getTable().getPlayersAtTable()){
                        if(p.getPLAYER_ID() == senderID){
                            thisRound.getFinisher().add(p);
                        }
                    }
                }

                if(thisRound.getFinisher().size() == 3){
                    model.roundFinished();
                }

                msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());

                break;

            case "string":
                String incoming = (String) msgIn.getObjects().get(0);
                boolean skipProcessEnded = false;
                switch (incoming) {
                    //@author Sandro
                    case "skip":
                        logger.info("Srv_processSkipButton");
                        int clientID = msgIn.getSenderID();
                        for (Player p : model.getGame().getTable().getPlayersAtTable()) { //Each Player at Table
                            if(p.getClientID() == msgIn.getSenderID()){ //add player that skipped to a list
                                if(p.equals(serviceLocator.getTable().getBeginner()) && serviceLocator.getTable().getPlayersThatSkipped().contains(p)){
                                    serviceLocator.getTable().getPlayersThatSkipped().clear();
                                } else {
                                    serviceLocator.getTable().getPlayersThatSkipped().add(p);
                                }
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

                        }
                        model.sendPlayersToClients();
                        model.sendActivePlayerToClients(); //send new activePlayer to Client
                        break;

                    //@author Pascal
                    case "tichu":

                        for (Player p : model.getGame().getTable().getPlayersAtTable()) {//Each Player at Tabel
                            if (p.getPLAYER_ID() == msgIn.getSenderID()) {// Is the player ID equals to the Client ID
                                if (p.getHandCards().size() == 14) {//Check if Handcards equals 14
                                    p.setSaidSmallTichu(true);//Player is abel to call a small Tichu
                                    msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                                    Message msgBroadcast = new Message("string/oSaidSmallTichu",p.getPLAYER_ID());
                                    serviceLocator.getServer().broadcast(msgBroadcast);
                                } else {
                                    if (p.getHandCards().size() <= 8) {//Check if Handcards <8
                                        p.setSaidBigTichu(true);//Player is abel to call a bigTichu
                                        msgOut = new MessageResponse("string", "ok", msgIn.getMessageID());
                                        Message msgBroadcast = new Message("string/oSaidBigTichu",p.getPLAYER_ID());
                                        serviceLocator.getServer().broadcast(msgBroadcast);
                                    }
                                }
                            }
                        }
                        break;

                    //@author Thomas
                    case "bombActiveChange":
                        //set the active player to not active
                        int msgId = msgIn.getSenderID();
                        logger.info(msgId+" msgId bomb active change");
                        logger.info("Bomb: active status change");
                        if(model.getGame().getTable().getLastPlayedCards().size() == 0 ||model.getGame().getTable().getLastPlayedCards().get(0).getRank() != Rank.Dog ) {//can only bomb if there wasnt played a dog


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
                                    model.clientInfoPlayerWantsBomb(msgIn.getSenderID()+"");
                                }
                            }
                            model.sendActivePlayerToClients();
                            model.sendPlayersToClients();
                        }

                        break;
                    }
                }

                return msgOut;
        }
    }
