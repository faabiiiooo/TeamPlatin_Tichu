package server.model;

import resources.Player;

public class Srv_Seat {

    private final  int SEAT_ID ;
    private Player player;

    public Srv_Seat(int SEAT_ID) {
        this.SEAT_ID = SEAT_ID;

    }


    public int getSEAT_ID() {
        return SEAT_ID;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

