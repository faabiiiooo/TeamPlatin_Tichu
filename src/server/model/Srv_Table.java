package server.model;

import resources.ServiceLocator;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Srv_Table {

    private final ArrayList<Srv_Player> playersAtTable = new ArrayList<>();
    private final ArrayList<Srv_Seat> seats = new ArrayList<>();
    private final ArrayList<Srv_Card> lastPlayedCards = new ArrayList<>();
    private final ArrayList<Srv_Card> allPlayedCards = new ArrayList<>();
    private Srv_Deck deck;
    private Srv_Game game;

    private Srv_Card mahJongWishCard;


    private int timeTillNextPlayer;

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();

    public Srv_Table(Srv_Game game){
        this.game = game;


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

    }

    //when every player decided, or the countdown has ended, deal the rest od the cards.
    //@author thomas
    protected void dealRestOfCards(){
        do {
            for (int i = 0; i < playersAtTable.size(); i++) {
                playersAtTable.get(i).getHandCards().add(deck.cardToDeal());
            }
        }while (deck.getRemainingCards() != 0);

    }
    //@author thomas
    // method to play out the cards from the active player
    protected boolean playCards(ArrayList<Srv_Card> playerCards){
        boolean canPlay = false;
        //if the cards which are chosen from the player have the same handtype and are higher than the last played cards:
        if(Srv_HandType.evaluateHand(lastPlayedCards, playerCards)){
            //add the last played cards to the allPlayedCards list and clear the lastPlayedCards list for the next cards
            allPlayedCards.addAll(lastPlayedCards); lastPlayedCards.clear();
            //add the new played cards to the list
            lastPlayedCards.addAll(playerCards);
            canPlay = true;

        }

        return canPlay;
    }

    public void skip(){ //@author Sandro
        for(int i = 0; i < seats.size(); i++) { //looking for IsActive player
            if (seats.get(i).getPlayer().isActive() == true) { //found isActive player
                boolean foundNextPlayer = false;
                switch (seats.get(i).getSEAT_ID()) { //Check seat of isActivePlayer
                    case 1: //Case ActivePlayer have Seat_ID = 1
                        for (int j = 1; j < seats.size() && !foundNextPlayer; j++) { //Check Player with Seat_ID 2,3 and 4
                            if (seats.get(j).getPlayer().getHandCards().size() > 0) { //Find next player which is still in the game
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(j).getPlayer().setActive(true); //Set new player on active
                                foundNextPlayer = true;
                            }
                        }
                        break;
                    case 2: //Case ActivePlayer have Seat_ID = 2
                        for (int j = 2; j < seats.size() && !foundNextPlayer; j++) { //Check Player with Seat_ID 3 and 4
                            if (seats.get(j).getPlayer().getHandCards().size() > 0) { //Find next player which is still in the game
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(j).getPlayer().setActive(true); //Set new player on active
                                foundNextPlayer = true;
                            } else { //Player with Seat_ID 3&4 already finished -> Player with Seat_ID 1 is next
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(0).getPlayer().setActive(true); //Set new player on active
                                foundNextPlayer = true;
                            }
                        }
                        break;
                    case 3: //Case ActivePlayer have Seat_ID = 3
                        if (seats.get(3).getPlayer().getHandCards().size() > 0) { //Player with Seat_ID 4 is still in the Game
                            seats.get(i).getPlayer().setActive(false); //Set old player on not active
                            seats.get(3).getPlayer().setActive(true); //Set new player on active
                            foundNextPlayer = true;
                        } else {
                            for (int j = 0; j < seats.size() - 2 && !foundNextPlayer; j++) { //Check Player with Seat_ID 1 and 2
                                if (seats.get(j).getPlayer().getHandCards().size() > 0) { //Find next player which is still in the game
                                    seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                    seats.get(j).getPlayer().setActive(true); //Set new player on active
                                    foundNextPlayer = true;
                                }
                            }
                        }
                        break;
                    case 4: //Case ActivePlayer have Seat_ID = 4
                        for (int j = 0; j < seats.size()-1 && !foundNextPlayer; j++) { //Check Player with Seat_ID 1,2 and 3
                            if (seats.get(j).getPlayer().getHandCards().size() > 0) { //Find next player which is still in the game
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(j).getPlayer().setActive(true); //Set new player on active
                                foundNextPlayer = true;
                            }
                        }
                        break;
                }
            }
        }
    }

    //@author Pascal
    //Create new Seats with ID 1-4 and add it to the List
    private Srv_Seat createSeat(){
        for(int i=0;i<4;i++){
            Srv_Seat s=new Srv_Seat(i+1);
            this.seats.add(s);

        }


        return null;
    }

    //@author Fabio
    public void transferCards(Srv_Player player){ //at game end, method has to be called twice, once with @param plaxer, once with @param null.

        ArrayList<Srv_Player> finisher = game.getRounds().get(game.getRounds().size()-1).getFinisher(); //getting finishers

        if(finisher.size() < 3 && player != null){ //if true, game is still playing only transfer table cards
            for (Srv_Card c : allPlayedCards){
                player.getWonCards().add(c);
                allPlayedCards.clear();
            }
        } else { //else transfer cards from looser to winner and rival team
            Srv_Player winner = finisher.get(0); //get winner of the round
            Srv_Player looser = null;
            for(Srv_Player p : playersAtTable){
                if(p.getHandCards().size() > 0){ //get looser, the only player with cards on his hand.
                    looser = p;
                }
            }


            for(Srv_Card c : looser.getWonCards()){ //always add the wonCards of looser to the wonCards of winner
                winner.getWonCards().add(c);
            }

            if(looser.getTeamID() == winner.getTeamID()){ //if winner and looser are in same team

                Srv_Player plrFromOtherTeam = null;
                for(Srv_Player p : finisher){   //get a player from the other team
                    if(p.getTeamID() != looser.getTeamID()){
                        plrFromOtherTeam = p;
                        break;
                    }
                }

                for(Srv_Card c : looser.getHandCards()){ //add the hand cards of the looser to the player from the rival team
                    plrFromOtherTeam.getWonCards().add(c);
                }
            }

            if(looser.getTeamID() != winner.getTeamID()){ //if winner and looser are not in same team, transfer hand cards also to winner
                for(Srv_Card c : looser.getHandCards()){
                    winner.getWonCards().add(c);
                }
            }


        }

    }

    //@author thomas
    protected void mahJongPlayed(){
    ArrayList<Srv_Player> playersWithWishedCard = new ArrayList<>();

    //add all the players who have the wished card from mahjong
    for(int i = 0; i < playersAtTable.size(); i++){
        for (int j = 0; j < playersAtTable.get(i).getHandCards().size();j++){
            if(playersAtTable.get(i).getHandCards().get(j).getRank() == mahJongWishCard.getRank()){
                playersWithWishedCard.add(playersAtTable.get(i));
            }
        }
    }
    //if the last played handtype was a singlecard and the player has the wished card on the hand set hasWishedCard to true
    if(lastPlayedCards.size() == 1 ){
        for(Srv_Player p: playersWithWishedCard){
            p.setHasWishedCard(true);
        }
        // if the last played cards where a street with mahjong card in it, check if a player has the wished card on the hand and could play it
    }else if (Srv_HandType.isStreet(lastPlayedCards)){
        for(Srv_Player p: playersWithWishedCard){
            if(Srv_HandType.mahJongWishStreet(p.getHandCards(),lastPlayedCards,mahJongWishCard) ){
                p.setHasWishedCard(true); //if the player has the card set the variable to true
            }
        }

    }

    }
    //@author thomas
    //method checks if the MJ wish card is already played or cant be played anymore
    protected void checkIfMJWishIsActive(){
        for(int i = 0; i < lastPlayedCards.size(); i++){
            // if the wished card is already played or it cant be played anymore set every player to false
            if(lastPlayedCards.get(i).getRank() == mahJongWishCard.getRank() || lastPlayedCards.get(lastPlayedCards.size()-1).getRank().ordinal() < mahJongWishCard.getRank().ordinal() ){
                for(Srv_Player p: playersAtTable){
                    p.setHasWishedCard(false);
                }
            }
        }

    }


    protected void dogPlayed(){ //SkiptoTeamMember @author Sandro
        for(int i = 0; i < seats.size(); i++) { //looking for IsActive player
            if (seats.get(i).getPlayer().isActive() == true) { //found isActive player
                switch (seats.get(i).getSEAT_ID()) { //Check seat of isActivePlayer
                    case 1: //Case ActivePlayer have Seat_ID = 1
                        if (seats.get(i + 2).getPlayer().getHandCards().size() > 0) { //teamMember with Seat_ID=3 is still in the game
                            seats.get(i).getPlayer().setActive(false); //Set old player on not active
                            seats.get(i + 2).getPlayer().setActive(true); //Set new player with Seat_ID=3 on active
                        } else { //teamMember with Seat_ID=3 not anymore in the game (already finish)
                            if (seats.get(i + 3).getPlayer().getHandCards().size() > 0) { //right player of the teammate with Seat_ID=4 still in the game
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(i + 3).getPlayer().setActive(true); //Set new player with Seat_ID=4 on active
                            } else { //right player of the teammate with Seat_ID=4 not anymore in the game (already finish)
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(i + 1).getPlayer().setActive(true); //Set new player with Seat_ID=2 on active
                            }
                        }
                        break;
                    case 2: //Case ActivePlayer have Seat_ID = 2
                        if (seats.get(i + 2).getPlayer().getHandCards().size() > 0) { //teamMember with Seat_ID=4 is still in the game
                            seats.get(i).getPlayer().setActive(false); //Set old player on not active
                            seats.get(i + 2).getPlayer().setActive(true); //Set new player with Seat_ID=4 on active
                        } else { //teamMember with Seat_ID=4 not anymore in the game (already finish)
                            if (seats.get(i - 1).getPlayer().getHandCards().size() > 0) { //right player of the teammate with Seat_ID=1 still in the game
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(i - 1).getPlayer().setActive(true); //Set new player with Seat_ID=1 on active
                            } else { //right player of the teammate with Seat_ID=1 not anymore in the game (already finish)
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(i + 1).getPlayer().setActive(true); //Set new player with Seat_ID=3 on active
                            }
                        }
                        break;
                    case 3: //Case ActivePlayer have Seat_ID = 3
                        if (seats.get(i - 2).getPlayer().getHandCards().size() > 0) { //teamMember with Seat_ID=1 is still in the game
                            seats.get(i).getPlayer().setActive(false); //Set old player on not active
                            seats.get(i - 2).getPlayer().setActive(true); //Set new player with Seat_ID=1 on active
                        } else { //teamMember with Seat_ID=1 not anymore in the game (already finish)
                            if (seats.get(i - 1).getPlayer().getHandCards().size() > 0) { //right player of the teammate with Seat_ID=2 still in the game
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(i - 1).getPlayer().setActive(true); //Set new player with Seat_ID=2 on active
                            } else { //right player of the teammate with Seat_ID=2 not anymore in the game (already finish)
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(i + 1).getPlayer().setActive(true); //Set new player with Seat_ID=4 on active
                            }
                        }
                        break;
                    case 4: //Case ActivePlayer have Seat_ID = 4
                        if (seats.get(i - 2).getPlayer().getHandCards().size() > 0) { //teamMember with Seat_ID=2 is still in the game
                            seats.get(i).getPlayer().setActive(false); //Set old player on not active
                            seats.get(i - 2).getPlayer().setActive(true); //Set new player with Seat_ID=2 on active
                        } else { //teamMember with Seat_ID=2 not anymore in the game (already finish)
                            if (seats.get(i - 1).getPlayer().getHandCards().size() > 0) { //right player of the teammate with Seat_ID=3 still in the game
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(i - 1).getPlayer().setActive(true); //Set new player with Seat_ID=3 on active
                            } else { //right player of the teammate with Seat_ID=3 not anymore in the game (already finish)
                                seats.get(i).getPlayer().setActive(false); //Set old player on not active
                                seats.get(i - 3).getPlayer().setActive(true); //Set new player with Seat_ID=1 on active
                            }
                        }
                        break;
                }
            }
        }
    }

    //@author Fabio
    protected void dragonPlayed(){

        Srv_Player activePlayer = null;
        Srv_Player rival = null;

        for(Srv_Player p : playersAtTable){ //check which player is active
            if(p.isActive()){
                activePlayer = p;
            }
            if(activePlayer != null && p.getTeamID() != activePlayer.getTeamID() && !activePlayer.equals(p)){ //find a rival
                rival = p;
            }
        }

        if(rival != null){ //transfer cards on table to rival
            transferCards(rival);
        }

    }

    //@author Pascal
    protected void phoenixPlayed(Srv_Card phoenix){


        if (lastPlayedCards.size() == 1) { //If it a single Card
            //Set the PhoenixRank 0.5 higher than the rank of the last played card. Ordinal + 2.5 because ordinal begins with 0
            phoenix.setPhoenixRank(lastPlayedCards.get(0).getRank().ordinal()+2.5);

        }else {
            //If Dog played is the rank of the Phonix 1.5;
             if (lastPlayedCards.get(0).getRank() == Srv_Rank.Dog) {
              phoenix.setPhoenixRank(1.5);
             }
        }


    }




    public ArrayList<Srv_Seat> getSeats() {
        return seats;
    }

    //getter setter
    public Srv_Card getMahJongWishCard(){ return this.mahJongWishCard; }

    public void setMahJongWishCard(Srv_Card wishCard){ this.mahJongWishCard = wishCard; }

}
