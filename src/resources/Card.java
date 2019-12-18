package resources;

import java.io.Serializable;
import java.util.Objects;

//@author Sandro
public class Card implements Comparable<Card>, Serializable { //Import Comparable to compare Cards, serializable for sending cards around

    protected static final long serialVersionUID= 1;

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

    @Override
    public boolean equals(Object o) { //autogenerated by intellij
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return getSuit() == card.getSuit() &&
                getRank() == card.getRank();
    }

    @Override
    public int hashCode() { //autogenerated by inellij
        return Objects.hash(getSuit(), getRank());
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
