package server.model;

import javafx.application.Platform;
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

    //@author Fabio
    public void startGame(){

        Srv_Round firstRound = new Srv_Round();
        game.getRounds().add(firstRound);
        logger.info("Created first Round");
        this.joinPlayersToTeam();
        this.sendNextPlayerIdToClients();
        game.getTable().dealCards();
        logger.info("dealed first 8 cards");
        this.sendPlayerHandsToClient();
        Countdown countdown = new Countdown();
        countdown.startCountdown();
        logger.info("waiting for players to announce big tichu");
        while (countdown.isAlive()){
        }
        game.getTable().dealRestOfCards();
        game.getRounds().get(0).checkBeginner(game.getTeams());
        this.sendPlayerHandsToClient();
        logger.info("dealed all cards");
        this.sendPlayersToClients();
        this.sendHasBombStatusToClients();
        this.sendActivePlayerToClients();

    }

    public void sendHasBombStatusToClients(){
        ArrayList<Player> players = game.getTable().getPlayersAtTable();
        Srv_Server server = serviceLocator.getServer();

        for(int i = 0; i < players.size(); i++){
            Message msgOut = null;
            int clientThreadID = server.searchIndexOfClientThreadByID(players.get(i).getClientID());
            try {
                msgOut = new Message("boolean/hasBomb", players.get(i).isHasBomb());
                server.getClientThreads().get(clientThreadID).send(msgOut);
                logger.info("Sent bomb status to Client!");
            } catch (Exception e){
                logger.severe("cant send status to client!");
            }

        }

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
    //@auhtor Fabio
    public void sendPlayersToClients(){
        ArrayList<Player> allPlayers = game.getTable().getPlayersAtTable(); //getting all Players
        ArrayList<Player> tempList = new ArrayList<>();
        ArrayList<Player> otherPlayers = new ArrayList<>(); //empty list for the other players
        Srv_Server server = serviceLocator.getServer();

       for(int i = 0; i < allPlayers.size(); i++){ //getting all players
            Message msgOut = null;
            int clientThreadID = server.searchIndexOfClientThreadByID(allPlayers.get(i).getClientID()); //getClientThread of current player
            Player currentPlayer = allPlayers.get(i); //current player
            tempList = (ArrayList<Player>) allPlayers.clone();
            otherPlayers.clear();
            if(currentPlayer.getClientID() == server.getClientThreads().get(clientThreadID).getID()){
                tempList.remove(currentPlayer); //remove currentPlayer from tempList
                otherPlayers.addAll(tempList); //add his rivals to otherPlayers
                msgOut = new Message("player", otherPlayers.toArray()); //sendThem
                try{
                    server.getClientThreads().get(clientThreadID).send(msgOut);
                    ArrayList<Player> tempPlayers = new ArrayList<>();

                    logger.info("Sent players to clients");
                } catch (Exception e){
                    logger.info("Can't send players to client");
                    e.printStackTrace();
                }
            }

        }

            /*for(int i = 0; i < allPlayers.size(); i++){
            Message msgOut = null;
            int clientThreadID = server.searchIndexOfClientThreadByID(allPlayers.get(i).getClientID()); //getClientThread of current player

            if(allPlayers.get(i).getClientID() == server.getClientThreads().get(clientThreadID).getID()){
                otherPlayers.clear();

                for(Player p : allPlayers){ //getting all the players
                    if(p.getPLAYER_ID() != allPlayers.get(i).getPLAYER_ID()){
                        otherPlayers.add(p);
                    }
                }
            }
            msgOut = new Message("player", otherPlayers.toArray());
            try{
                server.getClientThreads().get(clientThreadID).send(msgOut);
                logger.info("Sent players to clients");
            } catch (Exception e){
                logger.info("Can't send players to client");
                e.printStackTrace();
            }

        }*/

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

    private void sendNextPlayerIdToClients(){
        ArrayList<Srv_Team> teams = game.getTeams();
        Srv_Server server = serviceLocator.getServer();



        for(int i = 0; i < teams.size() -1; i++){ //getting Team 1
            for (int j = i+1; j < teams.size(); j++){  //getting Team 2
                for(Player p : game.getTable().getPlayersAtTable()){
                    if(p.getPLAYER_ID() != 4){
                        p.setNextPlayerID(p.getPLAYER_ID()+1);
                    } else {
                        p.setNextPlayerID(1);
                    }
                }

            }
        }
        ArrayList<Player> players = game.getTable().getPlayersAtTable();
        for(int i = 0; i < players.size(); i++){
            Message msgOut = null;
            int clientThreadIndex = server.searchIndexOfClientThreadByID(players.get(i).getClientID());
            try{
                msgOut = new Message("string/nextPlayer", players.get(i).getNextPlayerID());
                server.getClientThreads().get(clientThreadIndex).send(msgOut);
                logger.info("Sent nextPlayerID to client");

            } catch (Exception e){
                logger.severe("Can't send nextPlayerID to clients");
            }

        }

    }

    //@author Fabio
    private void joinPlayersToTeam(){ //gets only called on startup
        ArrayList<Player> players = game.getTable().getPlayersAtTable();

        for(Srv_Team team : game.getTeams()){
            if(team.getEVEN_ODD().equals("even")){ //if even team
                for(Player p : game.getTable().getPlayersAtTable()){
                    if(p.getPLAYER_ID() % 2 == 0){ // add players with even pID to this team
                        team.getMembers().add(p);
                        p.setTeamID(team.getTEAM_ID());
                    }
                }
            } else {
                if(team.getEVEN_ODD().equals("odd")){ // if odd team
                    for(Player p : game.getTable().getPlayersAtTable()){
                        if(p.getPLAYER_ID() % 2 != 0){ //add players with odd pID to this team
                            team.getMembers().add(p);
                            p.setTeamID(team.getTEAM_ID());
                        }
                    }
                }
            }
        }
    }
    //@author Fabio
    public void removePlayedCardsFromPlayerHand(int senderID, ArrayList<Card> playedCards){

        for(Player p : game.getTable().getPlayersAtTable()){
            if(p.getClientID() == senderID){
                p.getHandCards().removeAll(playedCards);
            }
        }

    }

    public Srv_Game getGame() {
        return game;
    }
}
