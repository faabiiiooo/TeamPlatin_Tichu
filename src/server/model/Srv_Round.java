package server.model;

import java.util.ArrayList;

public class Srv_Round extends Srv_Game {

    private ArrayList<Srv_Player> finisher;

    public Srv_Round(){
        super();
        this.finisher = new ArrayList<Srv_Player>();
    }

    //set table to null
    private void resetTable(){
    this.setTable(null);
    }

    private void checkBeginner(){
        //go through all players and their cards
        for(int i = 0; i < this.getTeams().size(); i++ ){
            for(int j = 0; j < this.getTeams().get(i).getMembers().size(); j++ ){
                for(int z = 0; z < this.getTeams().get(i).getMembers().get(j).getHandCards().size; z++){
                    //if the card from a player has the rank mahjong, set it to the active player
                    if(this.getTeams().get(i).getMembers().get(j).getHandCards().get(z).getRank() == Srv_Rank.mahjong){
                        this.getTeams().get(i).getMembers().get(j).isActive = true;
                    }
                }
            }
        }
    }

    public ArrayList<Srv_Player> getFinisher(){
        return this.finisher;
    }

}
