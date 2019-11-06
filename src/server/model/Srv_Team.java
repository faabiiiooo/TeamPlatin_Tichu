package server.model;

import java.util.ArrayList;
//@author Pascal
public  class Srv_Team implements Comparable<Srv_Team> {

    private final int TEAM_ID ;
    private final int MAX_TEAM_MEMBERS;
    private ArrayList<Srv_Player>members;
    private ArrayList<Srv_Seat>teamSeat;
    private Srv_Player player;
    private Srv_Seat seat;
    private Srv_Game game;
    private int gameScore;
    private int roundScore=0;





        //Constructor
    public Srv_Team(int TEAM_ID, Srv_Game game){
        this.TEAM_ID=TEAM_ID;
        MAX_TEAM_MEMBERS =2;
        this.members=new ArrayList<Srv_Player>();
        this.teamSeat=new ArrayList<Srv_Seat>();
        this.player=player;
        this.gameScore=gameScore;
        this.roundScore=roundScore;
        this.game=game;







    }
        // Add seats to the teamSeats. Add the Player to the right seat and return it.
    private Srv_Seat bookSeat() {
        while (this.teamSeat.size() < this.getMAX_TEAM_MEMBERS()) {
            this.teamSeat.add(this.seat);
            this.members.add(seat.getPlayer());

        }
        return seat;;
    }
        //The gameScore is calculated depending on the rounds played.
        public void calcGameScore () {
            for(int i=1;i<=this.game.getRounds().size();i++) {
            this.gameScore +=this.roundScore;
            i++;
        }
        }
            //Compare the gameScore of the team
            public int compareTo(Srv_Team t) {
                return t.gameScore-this.gameScore;
    }



   /* // Resets all used variables and lists
        public void resetRoundScore () {
            this.roundScore=0;
        }
*/
        public void calcRoundScore () {

            //A team that is the first and second to loos all the cards gets 200 points.
            Srv_Round lastRound = game.getRounds().get(game.getRounds().size() - 1);//letzte runde zugewiesen
            ArrayList<Player> finisher = lastRound.getFinisher();//finisher aus der letzt gespielten runde
            if (finisher.get(0).getTeamId() == finisher.get(1).getTeamId() && this.TEAM_ID == finisher.get(0).getTeamId()) {
                roundScore = 200;
            }


        /*If a small Tichu has been called, it is checked whether
            the player has finished the round first. Then he gets + 100 points, otherwise -100 points.*/

            if (player.isSaidSmallTichu() == true) {
                if (player.getPLAYER_ID() == finisher.getFinischer().get(0) && this.TEAM_ID == finisher.getTeamId()) {
                    this.roundScore += 100;
                } else {
                    this.roundScore -= 100;
                }
                /*If a big Tichu has been called, it is checked whether
            the player has finished the round first. Then he gets + 200 points, otherwise -200 points.*/
                if (player.isSaidBigTichu() == true) {
                    if (player.getPLAYER_ID() == finisher.getFinischer().get(0) && this.TEAM_ID == finisher.getTeamId()) {
                        this.roundScore += 200;
                    } else {
                        this.roundScore -= 200;


                    }

                    //Check the score of each member from a team and add it to the roundScore
                    for (Srv_Player p : this.members) {
                        this.roundScore += p.getScore();

                        //calculateScore() rückgabe wert int?
                    }


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

