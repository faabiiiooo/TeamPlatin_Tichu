package server.model;

import java.util.ArrayList;
//@author Pascal
public class Srv_Team {

    private final int TEAM_ID ;
    private final int MAX_TEAM_MEMBERS=2;
    private ArrayList<Srv_Player>members;

    private int gameScore;
    private int roundScore=0;






    public Srv_Team(int TEAM_ID){
        this.TEAM_ID=TEAM_ID;
        this.members=new ArrayList<Srv_Player>();
        this.gameScore=gameScore;
        this.roundScore=roundScore;

    }

    private Srv_Seat bookSeat() {

return seat;

    }
        public void calcGameScore () {

        }

        public void resetRoundScore () {

        }

        public void calcRoundScore () {
        Srv_Player player=new Srv_Player();


        }




    public int getTEAM_ID() {
        return TEAM_ID;
    }

    public int getMAX_TEAM_MEMBERS() {
        return MAX_TEAM_MEMBERS;
    }

    public ArrayList<Srv_Player> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Srv_Player> members) {
        this.members = members;
    }

    public int getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }

    public int getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(int roundScore) {
        this.roundScore = roundScore;
    }
}
