package server.model;

import resources.Player;
import resources.Rank;
import resources.ServiceLocator;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Srv_Round extends Srv_Game { //@author Thomas

    private ArrayList<Player> finisher;
    private ServiceLocator sl = ServiceLocator.getServiceLocator();
    private Logger logger = sl.getLogger();
    //protected ArrayList<Srv_Team> teams;

    public Srv_Round(){
        super();
        this.finisher = new ArrayList<Player>();
    }



    protected void checkBeginner(ArrayList<Srv_Team> teams){
        //go through all players and their cards
        for(int i = 0; i < this.getTeams().size(); i++ ){
            logger.info("Teams Schleife MJ "+this.getTeams().size());
            logger.info("Player Schleife MJ " + this.getTeams().get(i).getMembers().size());
            for(int j = 0; j < this.getTeams().get(i).getMembers().size(); j++ ){
                for(int z = 0; z < this.getTeams().get(i).getMembers().get(j).getHandCards().size(); z++){
                    //if the card from a player has the rank mahjong, set it to the active player
                    logger.info("searching the beginner");

                    if(this.getTeams().get(i).getMembers().get(j).getHandCards().get(z).getRank() == Rank.Mahjong){
                        logger.info("Mahjong found");
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
