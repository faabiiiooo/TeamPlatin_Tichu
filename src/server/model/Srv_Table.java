package server.model;

import sun.applet.Main;

import java.util.ArrayList;

public class Srv_Table {

    private final ArrayList<Srv_Player> playersAtTable = new ArrayList<>();
    private final ArrayList<Srv_Seat> seats = new ArrayList<>();
    private final ArrayList<Srv_Card> lastPlayedCards = new ArrayList<>();
    private final ArrayList<Srv_Card> allPlayedCards = new ArrayList<>();
    private Srv_Deck deck;

    private Srv_Card mahJongWishCard;

    private int timeTillNextPlayer;
    private Srv_Player activePlayer;

    public Srv_Table(){


    }


    //@author thomas
    protected Srv_Deck createDeck(){
        this.deck = new Srv_Deck();
        return deck;

    }

    protected void dealCards() {
        //deal 1 card to each player until everyone got 8 cards
        do {
            for (int i = 0; i < playersAtTable.size(); i++) {
                playersAtTable.get(i).getHandCards().add(deck.cardToDeal());
            }
        } while (deck.getRemainingCards() != 24);

    }

    //when every player decided, or the countdown has ended, deal the rest od the cards.
    protected void dealRestOfCards(){
        do {
            for (int i = 0; i < playersAtTable.size(); i++) {
                playersAtTable.get(i).getHandCards().add(deck.cardToDeal());
            }
        }while (deck.getRemainingCards() != 0);

    }

    public boolean playCards(ArrayList<Srv_Card> playerCards){

        return false;
    }

    public void skip(){

    }

    public void smallTichu(){

    }

    public void bigTichu(){

    }

    public void skipToTeamMember(){

    }

    private Srv_Seat createSeat(){

        return null;
    }

    public void transferCards(Srv_Player player){

    }
    //@author thomas
    private void mahJongPlayed(){
   //controller sollte hier Popup anzeigen damit der SPieler den Wunsch angeben kann. Danach sollte die Karte hinterlegt werden.




    }

    private void dogPlayed(){

    }

    private void dragonPlayed(){

    }

    private void phoenixPlayed(){

    }

    //getter setter
    public Srv_Card getMahJongWishCard(){ return this.mahJongWishCard; }

    public void setMahJongWishCard(Srv_Card wishCard){ this.mahJongWishCard = wishCard; }

}
