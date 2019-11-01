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

    }

}
