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

    private void mahJongPlayed(){

    }

    private void dogPlayed(){

    }

    private void dragonPlayed(){

    }

    private void phoenixPlayed(){

    }

}
