package server.model;

import java.util.ArrayList;

public class Srv_Game {
    protected ArrayList<Team> teams;
    protected final int G_MIN_SCORE = 1000;
    protected Team winner;
    private ArrayList<Round> rounds;
    private Table table;

    public Srv_Game(){
        this.rounds = new ArrayList<Round>();
        this.winner = null;

    }

    public ArrayList<Team> createTeams(){

        return null;
    }
    //create a new round, add it to the list and return it.
    public Round newRound(){
        Round round = new Round();
        this.rounds.add(round);
        return round;
    }

    public Team evaluateWinner(){

        return winner;
    }

    protected Table createTable(){
    this.table = new Table();
        return table;
    }


}
