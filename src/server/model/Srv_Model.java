package server.model;

import javafx.application.Platform;
import resources.*;

import java.util.ArrayList;
import java.util.Collections;
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
        game.getRounds().get(game.getRounds().size()-1).checkBeginner(game.getTeams());
        this.sendPlayerHandsToClient();
        logger.info("dealed all cards");
        this.sendPlayersToClients();
        this.sendHasBombStatusToClients();
        this.sendActivePlayerToClients();

    }
    //@author thomas
    public void sendHasBombStatusToClients(){
        ArrayList<Player> players = game.getTable().getPlayersAtTable();
        Srv_Server server = serviceLocator.getServer();
        logger.info("sending hasBombStatus to Clients");

        for(int i = 0; i < players.size(); i++){
            Message msgOut = null;
            int clientThreadID = server.searchIndexOfClientThreadByID(players.get(i).getClientID());
            try {

                msgOut = new Message("boolean/hasBomb", players.get(i).isHasBomb());
                server.getClientThreads().get(clientThreadID).send(msgOut);
                logger.info("active Bomb status? "+players.get(i)+ " " +players.get(i).isHasBomb());
                logger.info("Sent bomb status to Client!");
            } catch (Exception e){
                logger.severe("cant send status to client!");
            }

        }

    }

    //@author thomas
    public void openClientWishView(int clientThreadID){
        Message msgOutMJWish = null;
        Srv_Server server = serviceLocator.getServer();
        try {
            msgOutMJWish = new Message("string/wishView", "open"); //send info to open clients wish view because of played MJ
            server.getClientThreads().get(server.searchIndexOfClientThreadByID(clientThreadID)).send(msgOutMJWish);
            logger.info("Sent info to open Wish View to Client: " +clientThreadID);
        } catch (Exception e){
            logger.severe("cant send message to client");
        }

    }

    //@author thomas
    public void sendWishedCardToClients(){
        Message msgOutMJWish = null;
        Srv_Server server = serviceLocator.getServer();
            msgOutMJWish = new Message("card/wishedCard", game.getTable().getMahJongWishCard()); //send info to open clients wish view because of played MJ
            server.broadcast(msgOutMJWish);
            logger.info("Sent wished card to Client ");

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
                logger.info("Active Player: "+ players.get(i));
            } catch (Exception e){
                logger.severe("cant send activity status to client");
            }

        }

    }

    //@author Fabio
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

    public void sendStingNotification(){
        Srv_Server server = serviceLocator.getServer();
        Player playerThatStung = null;
        for(Player p : game.getTable().getPlayersAtTable()){
            if(p.isActive()){
                playerThatStung = p;
            }
        }
        for(int i = 0; i < game.getTable().getPlayersAtTable().size(); i++){
            Message msgOut = null;
            int clientThreadIndex = server.searchIndexOfClientThreadByID(game.getTable().getPlayersAtTable().get(i).getPLAYER_ID());


            try {
                msgOut = new Message("string/stingNotification", playerThatStung.getPLAYER_ID() + ";" + "player.sting.notification");
                server.getClientThreads().get(clientThreadIndex).send(msgOut);
                logger.info("Sent sting notification to clients");
            } catch (Exception e){
                logger.severe("Can't send Stingnotification to clients");
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
        logger.info("removePlayedCardsFromPlayerHand: ID "+senderID);
        for(Player p : game.getTable().getPlayersAtTable()){
            if(p.getClientID() == senderID){
                p.getHandCards().removeAll(playedCards);
            }
        }

    }

    //@author thomas
    public boolean checkIfWishedCardIsInPlayedCards(ArrayList<Card> playedCards){
        ArrayList<Card> clonedCards = (ArrayList<Card>) playedCards.clone();
        boolean playsWishedCard = false;
        for(Card wish: clonedCards){
            if(wish.getRank() == this.getGame().getTable().getMahJongWishCard().getRank()){
                playsWishedCard = true;
            }
        }
        return playsWishedCard;
    }

    //@author Fabio
    public void roundFinished(){
        logger.info("Round finished");
        Srv_Round currentRound = game.getRounds().get(game.getRounds().size()-1);
        Srv_Team winningTeam = null;
        boolean teamWins = false;
        for(Srv_Team t : game.getTeams()){ //calculate roundScore of Teams
            t.calcGameScore();
            logger.info("Team: "+t.getTEAM_ID() + " Score:"+ t.getGameScore());
        }

        winningTeam = game.evaluateWinner();

        if(winningTeam == null){
            logger.info("No Team achieved 1000 Points");
            Srv_Round nextRound = new Srv_Round();
            for(Srv_Team t : game.getTeams()){
                t.setRoundScore(0);
            }
            game.resetTable();
            game.getRounds().add(nextRound);
            logger.info("Resetted table");

            this.sendTableCardsToClients();
            this.sendScoresToClients();

            for(Player p : game.getTable().getPlayersAtTable()){
                p.getHandCards().clear();
                p.getWonCards().clear();
            }

            this.sendPlayerHandsToClient();
            this.sendNextPlayerIdToClients();
            this.sendPlayersToClients();
            this.sendWishedCardToClients();
            this.startNewRound();



        } else {
            this.gameFinished();
        }





    }

    private void startNewRound(){
        logger.info("Starting new Round");
        game.getTable().dealCards();
        logger.info("dealed first 8 cards");
        this.sendPlayerHandsToClient();
        Countdown countdown = new Countdown();
        countdown.startCountdown();
        logger.info("waiting for players to announce big tichu");
        while (countdown.isAlive()){
        }
        game.getTable().dealRestOfCards();
        game.getRounds().get(game.getRounds().size()-1).checkBeginner(game.getTeams());
        this.sendPlayerHandsToClient();
        logger.info("dealed all cards");
        this.sendPlayersToClients();
        this.sendHasBombStatusToClients();
        this.sendActivePlayerToClients();
    }

    private void gameFinished(){

    }

    //@author Fabio
    private void sendScoresToClients(){
        Srv_Server server = serviceLocator.getServer();

        for(int i = 0; i < game.getTeams().size(); i++){ //get each Team
            int teamScore = game.getTeams().get(i).getGameScore();
            for(int j = 0; j < game.getTeams().get(i).getMembers().size(); j++){
                Message msgOut = null;
                int clientThreadIndex = server.searchIndexOfClientThreadByID(game.getTeams().get(i).getMembers().get(i).getPLAYER_ID());

                try{
                    msgOut = new Message("string/score", teamScore);
                    server.getClientThreads().get(clientThreadIndex).send(msgOut);
                    logger.info("Sen't GameScore to client");
                } catch (Exception e){
                    logger.severe("Can't send GameScore to client");
                }
            }
        }


    }

    public Srv_Game getGame() {
        return game;
    }
}
