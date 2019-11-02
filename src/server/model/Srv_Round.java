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
        boolean mahJongFound = false;
        for(int i = 0; i < this.getTeams().size(); i++ ){
            for(int j = 0; j < this.getTeams().get(i).getMembers().size(); j++ ){
                for(int z = 0; z < this.getTeams().get(i).getMembers().get(j).getHandCards().size; z++){

                    if(this.getTeams().get(i).getMembers().get(j).getHandCards().get(z).getRank() == Srv_Rank.mahjong){
                        this.getTeams().get(i).getMembers().get(j).isActive = true;
                    }

                }

            }

        }


    }

}
