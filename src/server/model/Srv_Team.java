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

    private Srv_Seat bookSeat() { // Create a Seat, add it to the List and return the Seat /Pascal
        Srv_Seat seat=new Srv_Seat(SEAT_ID);
         seats.add(seat);

            return seat;

        }


        public void calcGameScore () {

        }

        public void resetRoundScore () {

        }

        public void calcRoundScore () {

        }

    }
