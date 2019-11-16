package server.model;

public class Srv_Card implements Comparable<Srv_Card> { //@author Sandro ; Import Comparable to compare Cards
    private Srv_Suit suit;
    private Srv_Rank rank;
    private int playerId;
    private int value;
    private double phoenixRank;


    public Srv_Card (Srv_Suit suit, Srv_Rank rank, int value, double phoenixRank) {
        this.suit = suit;
        this.rank = rank;
        this.value = value; //value for calculateScore
        this.phoenixRank = phoenixRank; //New given rank of the phoenix (+0.5 of the last card)
    }

    public int compareTo (Srv_Card card) { //new compareTo method to compare Cards
        return card.getRank().ordinal() - this.rank.ordinal();
    }




    public String toString() {
        return this.suit.toString() + " " + this.rank.toString() + " Value:" + this.value;
    }

    public Srv_Suit getSuit() {
        return suit;
    }

    public Srv_Rank getRank() {
        return rank;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getPhoenixRank() {
        return phoenixRank;
    }

    public void setPhoenixRank(double phoenixRank) {
        this.phoenixRank = phoenixRank;
    }
}
