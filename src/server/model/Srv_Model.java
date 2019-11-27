package server.model;

import resources.*;

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

        ArrayList<Player> players = game.getTable().getPlayersAtTable();
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

    public void sendPlayersToClients(){
        ArrayList<Player> allPlayers = (ArrayList<Player>) serviceLocator.getTable().getPlayersAtTable().clone();
        ArrayList<Player> otherPlayers = new ArrayList<>();
        Srv_Server server = serviceLocator.getServer();

        for(int i = 0; i < allPlayers.size(); i++){
            Message msgOut = null;
            int clientThreadID = server.searchIndexOfClientThreadByID(allPlayers.get(i).getClientID());

            if(allPlayers.get(i).getClientID() == server.getClientThreads().get(clientThreadID).getID()){
                otherPlayers.clear();
                for(Player p : allPlayers){
                    if(p.getPLAYER_ID() != allPlayers.get(i).getPLAYER_ID()){
                        otherPlayers.add(p);
                    }
                }
            }
            msgOut = new Message("player", otherPlayers.toArray());
            try{
                server.getClientThreads().get(clientThreadID).send(msgOut);
            } catch (Exception e){
                logger.info("Can't send players to client");
            }

        }

    }

    //@author Fabio
    public void sendActivePlayerToClients(){

        ArrayList<Player> players = game.getTable().getPlayersAtTable();
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

    public void sendNextPlayerIdToClients(){



    }

    private void joinPlayersToTeam(){
        ArrayList<Player> players = game.getTable().getPlayersAtTable();

        for(Srv_Team team : game.getTeams()){
            if(team.getEVEN_ODD().equals("even")){
                for(Player p : game.getTable().getPlayersAtTable()){
                    if(p.getPLAYER_ID() % 2 == 0){
                        team.getMembers().add(p);
                    }
                }
            } else {
                if(team.getEVEN_ODD().equals("odd")){
                    for(Player p : game.getTable().getPlayersAtTable()){

                    }
                }
            }
        }
    }

    public void sendRemainingCardsToClients(){


    }

    public Srv_Game getGame() {
        return game;
    }
}
