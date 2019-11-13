package server.model;

import java.util.ArrayList;

public class Srv_Table {

    private final ArrayList<Srv_Player> playersAtTable = new ArrayList<>();
    private final ArrayList<Srv_Seat> seats = new ArrayList<>();
    private final ArrayList<Srv_Card> lastPlayedCards = new ArrayList<>();
    private final ArrayList<Srv_Card> allPlayedCards = new ArrayList<>();

    private int timeTillNextPlayer;
    private Srv_Player activePlayer;

    public Srv_Table(){

    }


    private Srv_Deck createDeck(){




        return null;
    }

    private void dealCards(){

    }

    public boolean playCards(ArrayList<Srv_Card> playerCards){

        return false;
    }

    public void skip(){

    }
    //@author/ Pascal
    public void smallTichu(){
        if(this.activePlayer.getHandCards().size()<=8){
            this.activePlayer.setSaidSmallTichu(true);

        }else{
            this.activePlayer.isSaidSmallTichu();
        }

    }
    //@author/ Pascal
    public void bigTichu(){
        if(this.activePlayer.getHandCards().size()>8){
            this.activePlayer.setSaidBigTichu(true);

        }else{
            this.activePlayer.isSaidBigTichu();
        }


    }

    public void skipToTeamMember(){

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

    public void transferCards(Srv_Player player){

    }

    private void mahJongPlayed(){

    }

    private void dogPlayed(){

    }

    private void dragonPlayed(){

    }
    //@author Pascal
    private void phoenixPlayed(){
            for(int i=0;i<this.lastPlayedCards.size();i++){
                if(lastPlayedCards.get(i).equals(Srv_Rank.Phoenix)){
                    //not finish



                }
            }



    }

}
