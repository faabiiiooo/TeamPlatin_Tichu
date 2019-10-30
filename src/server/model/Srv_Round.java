package server.model;

import java.util.ArrayList;

public class Srv_Round extends Srv_Game {
    private ArrayList<Player> finisher;

    public Srv_Round(){
        super();
        this.finisher = new ArrayList<Player>();
    }

    private void resetTable(){
    this.setTable(null);
    }

    private void checkBeginner(){

    }

}
