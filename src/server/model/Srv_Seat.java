package server.model;

public class Srv_Seat {

    private final  int SEAT_ID ;
    private Srv_Player player;

    public Srv_Seat(int SEAT_ID) {
        this.SEAT_ID = SEAT_ID;

    }


    public int getSEAT_ID() {
        return SEAT_ID;
    }


    public Srv_Player getPlayer() {
        return player;
    }

    public void setPlayer(Srv_Player player) {
        this.player = player;
    }
}

