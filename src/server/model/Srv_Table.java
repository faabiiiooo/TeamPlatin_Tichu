package server.model;

import javafx.application.Platform;
import resources.Card;
import resources.Player;
import resources.Rank;
import resources.ServiceLocator;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Srv_Table {

    private final ArrayList<Player> playersAtTable = new ArrayList<>();
    private final ArrayList<Card> lastPlayedCards = new ArrayList<>();
    private final ArrayList<Card> allPlayedCards = new ArrayList<>();
    private final ArrayList<Player> playersThatSkipped = new ArrayList<>();
    private Srv_Deck deck;
    private Srv_Game game;
    private Player beginner, pWhichGetsDragon;

    private Card mahJongWishCard;
    private boolean dragonPlayed, wishCardPlayedOut = false;


    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();

    public Srv_Table(Srv_Game game){
        this.game = game;
        this.createDeck();


    }



    //@author thomas
    protected Srv_Deck createDeck(){
        this.deck = new Srv_Deck();
        return deck;

    }
    //@author thomas
    protected void dealCards() {
        //deal 1 card to each player until everyone got 8 cards
        do {
            for (int i = 0; i < playersAtTable.size(); i++) {
                playersAtTable.get(i).getHandCards().add(deck.cardToDeal());
            }
        } while (deck.getRemainingCards() != 24);
        logger.info("Remaining Cards in deck: "+deck.getRemainingCards());
    }

    //when every player decided, or the countdown has ended, deal the rest od the cards.
    //@author thomas
    protected void dealRestOfCards(){
        do {
            for (int i = 0; i < playersAtTable.size(); i++) {
                playersAtTable.get(i).getHandCards().add(deck.cardToDeal());
            }
        }while (deck.getRemainingCards() != 0);

        for(int i = 0; i < playersAtTable.size(); i++){ //setting the playerID of each card which a player has on his hand
            for(int j = 0; j < playersAtTable.get(i).getHandCards().size();j++){
                playersAtTable.get(i).getHandCards().get(j).setPlayerId(playersAtTable.get(i).getPLAYER_ID());
            }
        }

        checkPlayerHandsOnBomb();
    }
    //@author thomas
    // method to play out the cards
    public boolean playCards(ArrayList<Card> playerCards){
        logger.info("Going to play a card");
        boolean canPlay = false;
        //if the cards which are chosen from the player have the same handtype and are higher than the last played cards:
        logger.info("card to play -->: "+ playerCards);
        logger.info("Last played card in playCards() "+lastPlayedCards);
        if(Srv_HandType.evaluateHand(lastPlayedCards, playerCards)) {
            logger.info("HandType successfuly evaluated");
            //add the last played cards to the allPlayedCards list and clear the lastPlayedCards list for the next cards
            allPlayedCards.addAll(lastPlayedCards);
            lastPlayedCards.clear();
            //add the new played cards to the list
            lastPlayedCards.addAll(playerCards);
            canPlay = true;
            logger.info("canPlay? "+canPlay);
        }


        return canPlay;
    }
    //@author thomas
    //check player hands on bombs
    public void checkPlayerHandsOnBomb(){

       logger.info(playersAtTable+ " Amount of players when checking bomb status");
        for(int i = 0; i < playersAtTable.size(); i++){
            logger.info(Srv_HandType.isBombOnHand(playersAtTable.get(i).getHandCards(), this.lastPlayedCards) + " has a bomb" +playersAtTable.get(i));
            if(Srv_HandType.isBombOnHand(playersAtTable.get(i).getHandCards(), this.getLastPlayedCards())){
                logger.info(playersAtTable.get(i).toString() + " has a bomb");
                playersAtTable.get(i).setHasBomb(true);

            }else{
                playersAtTable.get(i).setHasBomb(false);
            }

        }
    }

    public void skip() { //@author Sandro
        logger.info("Table_Skip_Process started");
        boolean foundNextPlayer = false;

        int playersInGame = 0;
        for(Player p : playersAtTable){
            if(p.getHandCards().size() > 0){
                playersInGame++;
            }
        }

       if(playersThatSkipped.size() >= playersInGame && !lastPlayedCards.isEmpty()){
           logger.info(playersThatSkipped.size()+ "size from skipped list");
            int playerIDOfLastPlayedCards = lastPlayedCards.get(0).getPlayerId();
            for(Player p : playersAtTable){
                if(p.getPLAYER_ID() == playerIDOfLastPlayedCards){
                    this.sting(p);
                    logger.info("P that stung: "+p.getPLAYER_ID());
                    return;
                }
            }
        }

        for (int i = 0; i < playersAtTable.size() && !foundNextPlayer; i++) { //looking for IsActive player
            if (playersAtTable.get(i).isActive()) { //found isActive player
                logger.info("Old active Player: "+playersAtTable.get(i));
                

                switch (playersAtTable.get(i).getPLAYER_ID()) {
                    case 1: //Case ActivePlayer have Player_ID = 1
                        for (int j = 1; j < playersAtTable.size() && !foundNextPlayer; j++) { //Check Player with Player_ID 2,3 and 4
                            if (playersAtTable.get(j).getHandCards().size() > 0) { //Find next player which is still in the game
                                playersAtTable.get(i).setActive(false); //Set old player on not active
                                playersAtTable.get(j).setActive(true); //Set new player on active
                                foundNextPlayer = true;
                            }
                        }
                        break;
                    case 2: //Case ActivePlayer have Player_ID = 2
                        for (int j = 2; j < playersAtTable.size() && !foundNextPlayer; j++) { //Check Player with Player_ID 3 and 4
                            if (playersAtTable.get(j).getHandCards().size() > 0) { //Find next player which is still in the game
                                playersAtTable.get(i).setActive(false); //Set old player on not active
                                playersAtTable.get(j).setActive(true); //Set new player on active
                                foundNextPlayer = true;
                            }
                        }
                        if(playersAtTable.get(0).getHandCards().size() > 0 && !foundNextPlayer){
                            playersAtTable.get(i).setActive(false); //Set old player on not active
                            playersAtTable.get(0).setActive(true); //Set new player on active
                        }
                        break;
                    case 3: //Case ActivePlayer have Player_ID = 3
                        if (playersAtTable.get(3).getHandCards().size() > 0) { //Player with Player_ID 4 is still in the Game
                            playersAtTable.get(i).setActive(false); //Set old player on not active
                            playersAtTable.get(3).setActive(true); //Set new player on active
                            foundNextPlayer = true;
                        } else {
                            for (int j = 0; j < playersAtTable.size() - 2 && !foundNextPlayer; j++) { //Check Player with Player_ID 1 and 2
                                if (playersAtTable.get(j).getHandCards().size() > 0) { //Find next player which is still in the game
                                    playersAtTable.get(i).setActive(false); //Set old player on not active
                                    playersAtTable.get(j).setActive(true); //Set new player on active
                                    foundNextPlayer = true;
                                }
                            }
                        }
                        break;
                    case 4: //Case ActivePlayer have Player_ID = 4
                        for (int j = 0; j < playersAtTable.size()-1 && !foundNextPlayer; j++) { //Check Player with Player_ID 1,2 and 3
                            if (playersAtTable.get(j).getHandCards().size() > 0) { //Find next player which is still in the game
                                playersAtTable.get(i).setActive(false); //Set old player on not active
                                playersAtTable.get(j).setActive(true); //Set new player on active
                                foundNextPlayer = true;
                            }
                        }
                        break;
                }
            }
        }
        logger.info("Table_Skip_Process Ended");
    }

    private void sting(Player winner){
        this.transferCards(winner); //transfer Cards to the player which stung

        for(Player p : playersAtTable){
            if(p.isActive()){
                p.setActive(false);
            }
            p.setStung(false);
        }

        playersAtTable.get(playersAtTable.indexOf(winner)).setStung(true);

        if(winner.getHandCards().size() != 0){ //if it wasn't the last card of player who stung
            int indexOfWinner = playersAtTable.indexOf(winner);
            playersAtTable.get(indexOfWinner).setActive(true);
            logger.info("Winner: " + playersAtTable.get(indexOfWinner).toString());
        } else { //if player stung with his last card
            int idOfNextPlayer = winner.getNextPlayerID();
            for(Player p : playersAtTable){
                if(idOfNextPlayer == p.getPLAYER_ID() && p.getHandCards().size() == 0){
                    idOfNextPlayer = p.getNextPlayerID();
                    if(idOfNextPlayer == p.getPLAYER_ID()){
                        p.setActive(true);
                    }
                } else {
                    if(idOfNextPlayer == p.getPLAYER_ID() && p.getHandCards().size() > 0){
                        p.setActive(true);
                    }
                }
            }

        }

        this.playersThatSkipped.clear();

        serviceLocator.getSrvModel().sendActivePlayerToClients();
        serviceLocator.getSrvModel().sendTableCardsToClients();
        serviceLocator.getSrvModel().sendPlayersToClients();
        serviceLocator.getSrvModel().sendStingNotification();


    }

    //@author Fabio
    public void transferCards(Player player){ //at game end, method has to be called twice, once with @param plaxer, once with @param null.

        ArrayList<Player> finisher = game.getRounds().get(game.getRounds().size()-1).getFinisher(); //getting finishers


        if(finisher.size() < 3 && player != null && !dragonPlayed){ //if true, game is still playing only transfer table cards
            allPlayedCards.addAll(lastPlayedCards);
            lastPlayedCards.clear();
            player.getWonCards().addAll(allPlayedCards);
            allPlayedCards.clear();
            
        } else { //else check if dragon got played
            if(dragonPlayed){
                logger.info("Transfer dragon sting to: "+pWhichGetsDragon.getPLAYER_ID());
                allPlayedCards.addAll(lastPlayedCards);
                lastPlayedCards.clear();
                pWhichGetsDragon.getWonCards().addAll(allPlayedCards);
                allPlayedCards.clear();
                dragonPlayed = false;

            } else { //else transfer cards from looser to winner
                Player winner = finisher.get(0); //get winner of the round
                Player looser = null;
                for(Player p : playersAtTable){
                    if(p.getHandCards().size() > 0){ //get looser, the only player with cards on his hand.
                        looser = p;
                    }
                }


                for(Card c : looser.getWonCards()){ //always add the wonCards of looser to the wonCards of winner
                    winner.getWonCards().add(c);
                }

                if(looser.getTeamID() == winner.getTeamID()){ //if winner and looser are in same team

                    Player plrFromOtherTeam = null;
                    for(Player p : finisher){   //get a player from the other team
                        if(p.getTeamID() != looser.getTeamID()){
                            plrFromOtherTeam = p;
                            break;
                        }
                    }

                    for(Card c : looser.getHandCards()){ //add the hand cards of the looser to the player from the rival team
                        plrFromOtherTeam.getWonCards().add(c);
                    }
                }

                if(looser.getTeamID() != winner.getTeamID()){ //if winner and looser are not in same team, transfer hand cards also to winner
                    for(Card c : looser.getHandCards()){
                        winner.getWonCards().add(c);
                    }
                }

                looser.getWonCards().clear(); //reset wonCards of looser.
            }

        }

    }


    //@author thomas
    public void mahJongPlayed(){
    ArrayList<Player> playersWithWishedCard = new ArrayList<>();

    //add all the players who have the wished card from mahjong
    for(int i = 0; i < playersAtTable.size(); i++){
        for (int j = 0; j < playersAtTable.get(i).getHandCards().size();j++){
            logger.info(mahJongWishCard+ "The wished card");
            if(playersAtTable.get(i).getHandCards().get(j).getRank() == mahJongWishCard.getRank()){
                playersWithWishedCard.add(playersAtTable.get(i));
            }
        }
    }
    //if the last played handtype was a singlecard and the player has the wished card on the hand set hasWishedCard to true
    if(lastPlayedCards.size() == 1 ){
        for(Player p: playersWithWishedCard){
            logger.info("Players with wished card: "+playersWithWishedCard);
            p.setHasWishedCard(true);
        }
        // if the last played cards were a street with mahjong card in it, check if a player has the wished card on the hand and could play it
    }else if (Srv_HandType.isStreet(lastPlayedCards) && lastPlayedCards.size() >=5){
        for(Player p: playersWithWishedCard){
            if(Srv_HandType.mahJongWishStreet(p.getHandCards(),lastPlayedCards,mahJongWishCard) ){
                logger.info("Players with wished card: "+playersWithWishedCard);
                p.setHasWishedCard(true); //if the player has the card set the variable to true
            }
        }

    }

    }
    //@author thomas
    //method checks if the MJ wish card is already played or cant be played anymore
    public void checkIfMJWishIsActive(){
        if(!wishCardPlayedOut && mahJongWishCard != null) { // only if the wished card has not been played out and is not null
            for (Player mj : playersAtTable) {
                logger.info("going to check if the mj wish is still active");
                // if the wished card is already played or it cant be played anymore set every player to false
                if (lastPlayedCards.get(lastPlayedCards.size() - 1).getRank().ordinal() >= mahJongWishCard.getRank().ordinal() && lastPlayedCards.get(lastPlayedCards.size() - 1).getRank()
                        != Rank.Mahjong||
                        lastPlayedCards.size() >= 5 && !Srv_HandType.mahJongWishStreet(mj.getHandCards(), lastPlayedCards, mahJongWishCard) ) {
                    for (Player p : playersAtTable) {
                        p.setHasWishedCard(false);
                        logger.info("player :"+p+" can no more play the wished card. HasWIshedCardStatus: "+p.isHasWishedCard());
                    }
                } else {
                    if (lastPlayedCards.contains(mahJongWishCard.getRank()) && !wishCardPlayedOut) {
                        wishCardPlayedOut=true;
                        for (Player p : playersAtTable) {
                            p.setHasWishedCard(false);
                        }

                    }
                }
            }
        }
    }


    protected void dogPlayed(){ //SkiptoTeamMember @author Sandro
        logger.info("Table_DogPlayed_Process started");
        boolean foundNextPlayer = false;
        for(int i = 0; i < playersAtTable.size() && !foundNextPlayer; i++) { //looking for IsActive player
            if (playersAtTable.get(i).isActive() == true) { //found isActive player
                logger.info("Old active Player: "+playersAtTable.get(i));

                switch (playersAtTable.get(i).getPLAYER_ID()) { //Check Player_ID of isActivePlayer
                    case 1: //Case ActivePlayer have Player_ID = 1
                        if (playersAtTable.get(i + 2).getHandCards().size() > 0) { //teamMember with Player_ID=3 is still in the game
                            playersAtTable.get(i).setActive(false); //Set old player on not active
                            playersAtTable.get(i + 2).setActive(true); //Set new player with Player_ID=3 on active
                            foundNextPlayer = true;
                        } else { //teamMember with Player_ID=3 not anymore in the game (already finish)
                            if (playersAtTable.get(i + 3).getHandCards().size() > 0) { //right player of the teammate with Player_ID=4 still in the game
                                playersAtTable.get(i).setActive(false); //Set old player on not active
                                playersAtTable.get(i + 3).setActive(true); //Set new player with Seat_ID=4 on active
                                foundNextPlayer = true;
                            }  //Player with Player_ID=4 not anymore in the game (already finish)? -> Same Player who played the dog is still active
                        } //If Player who played dog already finish? Round is over (3 players are finish)
                        break;
                    case 2: //Case ActivePlayer have Player_ID = 2
                        if (playersAtTable.get(i + 2).getHandCards().size() > 0) { //teamMember with Player_ID=4 is still in the game
                            playersAtTable.get(i).setActive(false); //Set old player on not active
                            playersAtTable.get(i + 2).setActive(true); //Set new player with Player_ID=4 on active
                            foundNextPlayer = true;
                        } else { //teamMember with Player_ID=4 not anymore in the game (already finish)
                            if (playersAtTable.get(i - 1).getHandCards().size() > 0) { //right player of the teammate with Player_ID=1 still in the game
                                playersAtTable.get(i).setActive(false); //Set old player on not active
                                playersAtTable.get(i - 1).setActive(true); //Set new player with Player_ID=1 on active
                                foundNextPlayer = true;
                            }  //right player of the teammate with Player_ID=1 not anymore in the game (already finish) -> Same Player who played the dog is still active
                        } //If Player who played dog already finish? Round is over (3 players are finish)
                        break;
                    case 3: case 4: //Case ActivePlayer have Player_ID = 3/4
                        if (playersAtTable.get(i - 2).getHandCards().size() > 0) { //teamMember with Player_ID=1/2 is still in the game
                            playersAtTable.get(i).setActive(false); //Set old player on not active
                            playersAtTable.get(i - 2).setActive(true); //Set new player with Player_ID=1/2 on active
                            foundNextPlayer = true;
                        } else { //teamMember with Player_ID=1/2 not anymore in the game (already finish)
                            if (playersAtTable.get(i - 1).getHandCards().size() > 0) { //right player of the teammate with Player_ID=2/3 still in the game
                                playersAtTable.get(i).setActive(false); //Set old player on not active
                                playersAtTable.get(i - 1).setActive(true); //Set new player with Player_ID=2/3 on active
                                foundNextPlayer = true;
                            } //right player of the teammate with Player_ID=2/3 not anymore in the game (already finish) -> Same Player who played the dog is still active
                        } //If Player who played dog already finish? Round is over (3 players are finish)
                        break;
                }
                foundNextPlayer = true; //for case old isActive Player == new isActivePlayer
            }
        }
        logger.info("Table_DogPlayed_Process ended");
    }

    //@author Fabio
    protected void dragonPlayed(){

        logger.info("Dragon played!");

        Player activePlayer = null;
        Player rival = null;

        for(Player p : playersAtTable){ //check which player is active
            if(p.isActive()){
                activePlayer = p;
            }
            if(activePlayer != null && p.getTeamID() != activePlayer.getTeamID() && !activePlayer.equals(p)){ //find a rival
                rival = p;
            }
        }

        if(rival != null){ //transfer cards on table to rival
            this.pWhichGetsDragon = rival;
            this.dragonPlayed = true;
            logger.info("Player:"+pWhichGetsDragon.getPLAYER_ID()+"is going to recieve dragon sting");
        }

    }

    //@author Pascal
    protected void phoenixPlayed(Card phoenix){

        if(!lastPlayedCards.isEmpty()) {
            logger.info("Phoenix:"+lastPlayedCards.size());
            if (lastPlayedCards.size() == 1) { //If it a single Card
                logger.info("Phoenix:"+lastPlayedCards.toString());
                //Set the PhoenixRank 0.5 higher than the rank of the last played card. Ordinal + 2.5 because ordinal begins with 0
                phoenix.setPhoenixRank(lastPlayedCards.get(0).getRank().ordinal() + 2.5);


            } else {
                //If Dog played is the rank of the Phonix 1.5;
                if (lastPlayedCards.get(0).getRank() == Rank.Dog) {
                    logger.info("Phoenix:"+lastPlayedCards.toString());
                    phoenix.setPhoenixRank(1.5);
                }
            }
        }


    }

    //@author Fabio
    public void addPlayerToTable(Player player){
        this.playersAtTable.add(player);
    }





    //getter setter
    public Card getMahJongWishCard(){ return this.mahJongWishCard; }

    public void setMahJongWishCard(Card wishCard){ this.mahJongWishCard = wishCard; }

    public ArrayList<Player> getPlayersAtTable() {
        return playersAtTable;
    }

    public ArrayList<Card> getLastPlayedCards() {
        return lastPlayedCards;
    }

    public ArrayList<Player> getPlayersThatSkipped() {
        return playersThatSkipped;
    }

    public Player getBeginner() {
        return beginner;
    }

    public void setBeginner(Player beginner) {
        this.beginner = beginner;
    }

    public ArrayList<Card> getAllPlayedCards() {
        return allPlayedCards;
    }

    public boolean isWishCardPlayedOut() {
        return wishCardPlayedOut;
    }

    public void setWishCardPlayedOut(boolean wishCardPlayedOut) {
        this.wishCardPlayedOut = wishCardPlayedOut;
    }

    public boolean isDragonPlayed() {
        return dragonPlayed;
    }

    public void setDragonPlayed(boolean dragonPlayed) {
        this.dragonPlayed = dragonPlayed;
    }

    public Player getpWhichGetsDragon() {
        return pWhichGetsDragon;
    }

    public void setpWhichGetsDragon(Player pWhichGetsDragon) {
        this.pWhichGetsDragon = pWhichGetsDragon;
    }
}
