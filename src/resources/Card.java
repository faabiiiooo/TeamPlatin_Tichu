package resources;

import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable { //@author Sandro ; Import Comparable to compare Cards
    private Suit suit;
    private Rank rank;
    private int playerId;
    private int value;
    private double phoenixRank;


    public Card(Suit suit, Rank rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value; //value for calculateScore
    }

    public int compareTo (Card card) { //new compareTo method to compare Cards
        return card.getRank().ordinal() - this.rank.ordinal();
    }




    public String toString() {
        return this.suit.toString() + " " + this.rank.toString() + " Value:" + this.value;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
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
