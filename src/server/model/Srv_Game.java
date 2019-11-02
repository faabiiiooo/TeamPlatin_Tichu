package server.model;

import java.util.ArrayList;
import java.util.Collections;

public class Srv_Game {

    protected ArrayList<Srv_Team> teams;
    protected final int G_MIN_SCORE = 1000;
    protected Srv_Team winner;
    private ArrayList<Srv_Round> rounds;
    private Srv_Table table;

    public Srv_Game(){
        this.teams = new ArrayList<Srv_Team>();
        this.rounds = new ArrayList<Srv_Round>();
        this.winner = null;

    }

    public ArrayList<Srv_Team> createTeams(){
        for(int i = 0; i < 2; i++){
            Srv_Team t = new Srv_Team();
            this.teams.add(t);

        }

        return teams;
    }
    //create a new round, add it to the list and return it.
    public Srv_Round newRound(){
        Srv_Round round = new Srv_Round();
        this.rounds.add(round);
        return round;
    }

    public Srv_Team evaluateWinner(){
    //create an ArrayList for the teams who reached 1000 points or more
        ArrayList<Srv_Team> winningRangeTeams = new ArrayList<Srv_Team>();
        //check both teams if they reached 1000 points or more - if yes, add them to the new ArrayList
        if((teams.get(0).getGameScore() > teams.get(1).getGameScore()) && teams.get(0).getGameScore() >= G_MIN_SCORE ) {
                winningRangeTeams.add(teams.get(0));
        }else{
            if((teams.get(1).getGameScore() > teams.get(0).getGameScore()) && teams.get(1).getGameScore() >= G_MIN_SCORE ){
                winningRangeTeams.add(teams.get(1));
            }
        }
        /* check the size of the list to see if there is a team who reached 1000 points or more.
        for the case that both teams reached the 1000 points, sort the list and get the team with more points.
        The instance variable winner will be set to the winner team and returned*/
        if(winningRangeTeams.size() == 1){
            Collections.sort(winningRangeTeams);
            this.winner = winningRangeTeams.get(0);
        }else{ // if both teams have the same amount of points, set the winner to null
            if(winningRangeTeams.get(0).getGameScore() == winningRangeTeams.get(1).getGameScore() )
            this.winner = null;
        }
        return winner;
    }


    protected Srv_Table createTable(){
    this.table = new Srv_Table();
        return table;
    }

    //getters and setters
    public ArrayList<Srv_Team> getTeams() { return teams; }

    public Srv_Table getTable(){ return this.table; }

    public void setTable(Srv_Table t){ this.table = t; }

}
