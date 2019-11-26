package server.model;

import resources.Card;
import resources.Countdown;
import resources.Message;
import resources.ServiceLocator;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Srv_Model {

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();

    private Srv_Game game;

    public Srv_Model(){

        Srv_Server server = new Srv_Server();
        server.start();
        createGame();
        serviceLocator.setSrvModel(this);

    }

    //@author Fabio
    private void createGame(){ //creating the game

        game = new Srv_Game();
        game.createTable();

    }

    //author Fabio
    public void startGame(){

        Srv_Round firstRound = new Srv_Round();
        game.getRounds().add(firstRound);
        logger.info("Created first Round");

        game.getTable().dealCards();
        logger.info("dealed first 8 cards");
        this.sendPlayerHandsToClient();
        Countdown countdown = new Countdown();
        countdown.startCountdown();
        logger.info("waiting for players to announce big tichu");
        while (countdown.isAlive()){
        }
        game.getTable().dealRestOfCards();
        game.getRounds().get(0).checkBeginner();
        this.sendPlayerHandsToClient();
        logger.info("dealed all cards");
    }

    private void addPlayerToTeams(){ //Player 1 and 2 are in a team,

    }

    //@author Fabio
    public void sendPlayerHandsToClient(){

        ArrayList<Srv_Player> players = game.getTable().getPlayersAtTable();
        Srv_Server server = serviceLocator.getServer();

        for(int i = 0; i < players.size(); i++){
            Message msgOut = null;
            int clientThreadID = server.searchIndexOfClientThreadByID(players.get(i).getClientID());
            try {
                msgOut = new Message("card/dealCards", players.get(i).getHandCards().toArray());
                server.getClientThreads().get(clientThreadID).send(msgOut);
                logger.info("Sent player Hands to Client!");
            } catch (Exception e){
                logger.severe("cant send cards to client!");
            }

        }
    }

    //@author Fabio
    public void sendTableCardsToClients(){

        ArrayList<Card> tableCards = serviceLocator.getTable().getLastPlayedCards();
        Srv_Server server = serviceLocator.getServer();

        Message msgOut = new Message("card/tableCards",tableCards.toArray());
        server.broadcast(msgOut);

    }

    //@author Fabio
    public void sendActivePlayerToClients(){

        ArrayList<Srv_Player> players = game.getTable().getPlayersAtTable();
        Srv_Server server = serviceLocator.getServer();

        for(int i = 0; i < players.size(); i++){
            Message msgOut = null;
            int clientThreadID = server.searchIndexOfClientThreadByID(players.get(i).getClientID());
            try {
                msgOut = new Message("boolean/isActive", players.get(i).isActive());
                server.getClientThreads().get(clientThreadID).send(msgOut);
                logger.info("Sent activity status to client");
            } catch (Exception e){
                logger.severe("cant send activity status to client");
            }

        }

    }

    public void sendRemainingCardsToClients(){


    }

    public Srv_Game getGame() {
        return game;
    }
}
