package server.model;

import resources.Player;
import resources.Rank;

import java.util.ArrayList;

public class Srv_Round extends Srv_Game { //@author Thomas

    private ArrayList<Player> finisher;

    public Srv_Round(){
        this.finisher = new ArrayList<Player>();
    }



    protected void checkBeginner(){
        //go through all players and their cards
        for(int i = 0; i < this.getTeams().size(); i++ ){
            for(int j = 0; j < this.getTeams().get(i).getMembers().size(); j++ ){
                for(int z = 0; z < this.getTeams().get(i).getMembers().get(j).getHandCards().size(); z++){
                    //if the card from a player has the rank mahjong, set it to the active player
                    if(this.getTeams().get(i).getMembers().get(j).getHandCards().get(z).getRank() == Rank.Mahjong){
                        this.getTeams().get(i).getMembers().get(j).setActive(true);
                    }
                }
            }
        }
    }

    public ArrayList<Player> getFinisher(){
        return this.finisher;
    }

}
