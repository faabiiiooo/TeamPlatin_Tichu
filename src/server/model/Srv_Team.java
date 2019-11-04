package server.model;

import java.util.ArrayList;

public class Srv_Team {

    private final int TEAM_ID ;
    private final int MAX_TEAM_MEMBERS=2;
    private ArrayList<Srv_Player>members;

    private int gameScore;
    private int roundScore;






    public Srv_Team(int TEAM_ID){
        this.TEAM_ID=TEAM_ID;
        this.members=new ArrayList<Srv_Player>();
        this.gameScore=gameScore;
        this.roundScore=roundScore;

    }

    private Srv_Seat bookSeat() { // A new seat is created, assigned an ID, and added to the list/ Pascal


return seat;

    }
        public void calcGameScore () {

        }

        public void resetRoundScore () {

        }

        public void calcRoundScore () {

        }

    }
