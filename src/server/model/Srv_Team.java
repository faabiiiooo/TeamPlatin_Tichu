package server.model;

import java.util.ArrayList;
//@author Pascal
public  class Srv_Team implements Comparable<Srv_Team> {

    private final int TEAM_ID ;
    private final int MAX_TEAM_MEMBERS =2;
    private final String EVEN_ODD;
    private ArrayList<Srv_Player>members;
    private ArrayList<Srv_Seat>teamSeat;
    private Srv_Game game;
    private int gameScore;
    private int roundScore;





        //Constructor
    public Srv_Team(int TEAM_ID, Srv_Game game, String evenOdd){
        this.TEAM_ID=TEAM_ID;
        this.members=new ArrayList<Srv_Player>();
        this.teamSeat=new ArrayList<Srv_Seat>();
        this.gameScore= 0;
        this.roundScore= 0;
        this.game=game;
        this.EVEN_ODD = evenOdd;


    }



        // Add seats to the teamSeats. Add the Player to the right seat and return it
    private void bookSeat() {
        while (this.teamSeat.size() < this.getMAX_TEAM_MEMBERS()) {

            ArrayList<Srv_Seat> seats = game.getTable().getSeats();
            for(Srv_Seat s : seats){
                if(s.getSEAT_ID() % 2 > 0 && EVEN_ODD.equals("odd")){
                    teamSeat.add(s);
                    members.add(s.getPlayer());
                } else {
                    if(s.getSEAT_ID() % 2 == 0 && EVEN_ODD.equals("even")){
                        teamSeat.add(s);
                        members.add(s.getPlayer());
                    }
                }
            }
        }
    }


    //calculate game score
    public void calcGameScore () {
            this.gameScore += this.calcRoundScore();
    }

    //compareTo method
    public int compareTo(Srv_Team t) {
        return t.gameScore-this.gameScore;
    }


   public int calcRoundScore () {

       //A team that is the first and second to loos all the cards gets 200 points.
       Srv_Round lastRound = game.getRounds().get(game.getRounds().size() - 1);//letzte runde zugewiesen
       ArrayList<Srv_Player> finisher = lastRound.getFinisher();//finisher aus der letzt gespielten runde

       if (finisher.get(0).getTeamId() == finisher.get(1).getTeamId() && this.TEAM_ID == finisher.get(0).getTeamId()) {
            roundScore = 200;
            return roundScore;
       }

      for(int i = 0; i < members.size(); i++) {
          Srv_Player p = members.get(i);
          if (p.isSaidSmallTichu()) {
              if (p.equals(finisher.get(0))) {
                  this.roundScore += 100;
              } else {
                  this.roundScore -= 100;
              }
          } else {
              if (p.isSaidBigTichu()) {
                  if (p.equals(finisher.get(0))) {
                      this.roundScore += 200;
                  } else {
                      this.roundScore -= 200;
                  }
              }
          }
      }

      for(Srv_Player p : this.members){
          this.roundScore += p.calculateScore();
      }

      return roundScore;

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

