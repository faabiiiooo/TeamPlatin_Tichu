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

    public void smallTichu(){

    }

    public void bigTichu(){

    }

    public void skipToTeamMember() { //@author Sandro
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

    private Srv_Seat createSeat(){

        return null;
    }

    public void transferCards(Srv_Player player){

    }
    //@author thomas
    private void mahJongPlayed(){
   //controller sollte hier Popup anzeigen damit der SPieler den Wunsch angeben kann. Danach sollte die Karte hinterlegt werden.




    }

    private void dogPlayed(){ //@author Sandro
        skipToTeamMember();
    }

    private void dragonPlayed(){

    }

    private void phoenixPlayed(){

    }

    public ArrayList<Srv_Seat> getSeats() {
        return seats;
    }

    //getter setter
    public Srv_Card getMahJongWishCard(){ return this.mahJongWishCard; }

    public void setMahJongWishCard(Srv_Card wishCard){ this.mahJongWishCard = wishCard; }

}
