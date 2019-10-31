package server.model;

import java.util.ArrayList;

public class Srv_Team {

    private final int TEAM_ID = 0;
    private final int MAX_TEAM_MEMBERS=2;
    private ArrayList<Srv_Player>members;
    private int gameScore;
    private int roundScore;






    public Srv_Team(){
        this.members=new ArrayList<Srv_Player>();





    }

    private Srv_Seat bookSeat(){
        Srv_Seat seat=new Srv_Seat(int SEAT_ID);
        return seat;


    }

    public void calcGameScore(){

    }

    public void resetRoundScore(){

    }

    public void calcRoundScore(){

    }

}
