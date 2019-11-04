package server.model;

import javax.swing.text.Style;
import java.util.ArrayList;
//@author Pascal
public class Srv_Team {

    private final int TEAM_ID ;
    private final int MAX_TEAM_MEMBERS=2;
    private ArrayList<Srv_Player>members;
    private Srv_Round round;
    private Srv_Player player;
    private Srv_Seat seat;
    private Srv_Game game;
    private int gameScore;
    private int roundScore=0;






    public Srv_Team(int TEAM_ID){
        this.TEAM_ID=TEAM_ID;
        this.members=new ArrayList<Srv_Player>();
        this.round=new Srv_Round();
        this.player=new Player();
        this.gameScore=gameScore;
        this.roundScore=roundScore;





    }

    private Srv_Seat bookSeat() {

        return seat;

    }
        //The GameScore is calculated depending on the rounds played.
        public void calcGameScore () {
        for(int i=0;i<Srv_Round.getRounds().size();i++) {
            this.gameScore +=(this.roundScore*=i);
        }
        }


        // Resets all used variables and lists
        public void resetRoundScore () {
        this.roundScore=0;
        player.setSaidSmallTichu(false);
        player.setSaidBigTichu(false);
        round.getFinisher().clear();


        }

        public void calcRoundScore () {

        /*If a small Tichu has been called, it is checked whether
            the player has finished the round first. Then he gets + 100 points, otherwise -100 points.*/
            if (player.isSaidSmallTichu() == true) {
                if (player.getPLAYER_ID() == round.getFinischer().get(0)) {
                    this.roundScore += 100;
                } else {
                    this.roundScore -= 100;
                }
                /*If a big Tichu has been called, it is checked whether
            the player has finished the round first. Then he gets + 100 points, otherwise -2 00 points.*/
                if (player.isSaidBigTichu() == true) {
                    if (player.getPLAYER_ID() == round.getFinischer().get(0)) {
                        this.roundScore += 200;
                    } else {
                        this.roundScore -= 200;


                    }
                }
                /*Check the values of the cards in the array list wonCards.
                 If the cards listed below occur, the value is assigned to the roundScore */
                for (Card c : player.getWonCards()) {

                    switch (Srv_Rank) {
                        case Srv_Rank.king:
                            this.roundScore += c.getValue();

                        case Srv_Rank.Ten:
                            this.roundScore += c.getValue();

                        case Srv_Rank.Five:
                            this.roundScore += c.getValue();

                        case Srv_Rank.Dragon:
                            this.roundScore += c.getValue();

                        case Srv_Rank.Phoenix:
                            this.roundScore += c.getValue();
                    }
                    //Doppelsieg fehlt noch


                    }


                }

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
