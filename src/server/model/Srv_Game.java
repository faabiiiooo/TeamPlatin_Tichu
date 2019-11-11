package server.model;

import java.util.ArrayList;
import java.util.Collections;

public class Srv_Game { //@author Thomas

    protected ArrayList<Srv_Team> teams = new ArrayList<Srv_Team>();;
    protected final int G_MIN_SCORE = 1000;
    protected Srv_Team winner;
    private ArrayList<Srv_Round> rounds;
    private Srv_Table table;

    public Srv_Game(){
        this.teams = createTeams();
        this.rounds = new ArrayList<Srv_Round>();
        this.winner = null;

    }
    //create two teams and add them to the a list
    public ArrayList<Srv_Team> createTeams(){
        for(int i = 0; i < 2; i++){
            Srv_Team t = new Srv_Team();
            this.teams.add(t);

        }

        return teams;
    }
    //create a new round
    public Srv_Round newRound(){
        Srv_Round round = new Srv_Round();
        for(int i = 0; i < teams.size(); i++){//reset all the important lists from the players and variables to start a new round
            teams.get(i).setRoundScore(0); //reset the roundScore from each team

            for(int j = 0; j < teams.get(0).getMembers().size(); j++){ //reset all the important lists from the players and variables to start a new round
                teams.get(i).getMembers().get(j).getWonCards().clear(); //delete all won cards from the player
                teams.get(i).getMembers().get(j).setScore(0); // set the players score to 0
                teams.get(i).getMembers().get(j).setActive(false); // set everybody to not active

            }
        }
        this.rounds.add(round); // add the round to the list and return it

        //create a new Deck, shuffle it, deal it and then check the beginner
        Srv_Deck deck = table.createDeck(); deck.shuffle(); table.dealCards(); round.checkBeginner();

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

    //create a new table
    protected Srv_Table createTable(){
    this.table = new Srv_Table();
        return table;
    }

    // reset the table Object
    private void resetTable(){
        this.table = null;
    }

    //getters and setters
    public ArrayList<Srv_Team> getTeams() { return teams; }

    public Srv_Table getTable(){ return this.table; }

    public void setTable(Srv_Table t){ this.table = t; }

    public ArrayList<Srv_Round> getRounds() {
        return rounds;
    }
}
