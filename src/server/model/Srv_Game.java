package server.model;

import resources.ServiceLocator;

import java.util.ArrayList;
import java.util.Collections;

//@author Thomas
public class Srv_Game {

    protected ArrayList<Srv_Team> teams = new ArrayList<Srv_Team>();
    protected final static int G_MIN_SCORE = 1000;
    protected Srv_Team winner;
    private ArrayList<Srv_Round> rounds;
    private Srv_Table table;

    private final ServiceLocator sl = ServiceLocator.getServiceLocator();

    public Srv_Game(){
        this.teams = createTeams();
        this.rounds = new ArrayList<Srv_Round>();
        this.winner = null;
        this.table = new Srv_Table(this);

    }
    //create two teams and add them to the a list
    public ArrayList<Srv_Team> createTeams(){
        int teamID = 0;
        String even = "even"; String odd = "odd";
        ArrayList<String> evenOddList = new ArrayList<>();
        evenOddList.add(even); evenOddList.add(odd);
        Collections.shuffle(evenOddList);
        for(int i = 0; i < 2; i++){
            teamID++;
            Srv_Team t = new Srv_Team(teamID,this, evenOddList.get(i) );
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
                teams.get(i).getMembers().get(j).setHasWishedCard(false); // set the MJ wishcard boolean to false for every new round

            }
        }
        this.rounds.add(round); // add the round to the list and return it
        table.setMahJongWishCard(null);//reset the wished card from mahjong


        //create a new Deck deal it and then check the beginner
        table.createDeck();

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

        if(winningRangeTeams.isEmpty()){
            return null;
        }

        /* check the size of the list to see if there is a team who reached 1000 points or more.
        for the case that both teams reached the 1000 points, sort the list and get the team with more points.
        The instance variable winner will be set to the winner team and returned*/
        if(winningRangeTeams.size() == 1){
            Collections.sort(winningRangeTeams);
            this.winner = winningRangeTeams.get(0);
        }else{ // if both teams have the same amount of points, set the winner to null
            if(winningRangeTeams.get(0).getGameScore() == winningRangeTeams.get(1).getGameScore()){
                this.winner = null;
            } else {
                if(winningRangeTeams.isEmpty()){
                    winner = null;
                }
            }
        }
        return winner;
    }

    //create a new table
    protected Srv_Table createTable(){
        this.table = new Srv_Table(this);
        sl.setTable(table);
        return table;
    }


    public void resetTable(){
        table.createDeck();
        table.getPlayersThatSkipped().clear();
        table.getLastPlayedCards().clear();
        table.setMahJongWishCard(null);
        table.getAllPlayedCards().clear();
        table.setBeginner(null);
        table.setWishCardPlayedOut(false);
        table.setDragonPlayed(false);
        table.setpWhichGetsDragon(null);

    }

    //getters and setters
    public ArrayList<Srv_Team> getTeams() { return teams; }

    public Srv_Table getTable(){ return this.table; }

    public void setTable(Srv_Table t){ this.table = t; }

    public ArrayList<Srv_Round> getRounds() {
        return rounds;
    }
}
