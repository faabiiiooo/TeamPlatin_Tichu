package server.model;

import java.util.ArrayList;
import java.util.Collections;

public class Srv_Deck { //@author Sandro

    private final int NUM_OF_CARDS = 56;
    private ArrayList<Srv_Card> cards = new ArrayList<>();

    public Srv_Deck() {
        createCards();
        shuffleCards();
    }

    public ArrayList<Srv_Card> createCards() { //Create all 56 cards
        for (Srv_Suit suit : Srv_Suit.values()) {
            for (Srv_Rank rank : Srv_Rank.values()) {
                if (suit == Srv_Suit.SpecialCards && rank == Srv_Rank.Phoenix) { //create specialCards
                    Srv_Card card = new Srv_Card(suit, rank, -25);
                    this.cards.add(card);
                }
                if (suit == Srv_Suit.SpecialCards && rank == Srv_Rank.Dragon) {
                    Srv_Card card = new Srv_Card(suit, rank, 25);
                    this.cards.add(card);
                }
                if (suit == Srv_Suit.SpecialCards && rank == Srv_Rank.Dog) {
                    Srv_Card card = new Srv_Card(suit, rank, 0);
                    this.cards.add(card);
                }
                if (suit == Srv_Suit.SpecialCards && rank == Srv_Rank.Mahjong) {
                    Srv_Card card = new Srv_Card(suit, rank, 0);
                    this.cards.add(card);
                }
                if (suit != Srv_Suit.SpecialCards && rank != Srv_Rank.Phoenix && rank != Srv_Rank.Dragon
                        && rank != Srv_Rank.Dog && rank != Srv_Rank.Mahjong) { //Create regular cards
                    int value = 0;
                    if (rank == Srv_Rank.Five) {
                        value = 5;
                    }
                    if (rank == Srv_Rank.Ten || rank == Srv_Rank.King) {
                        value = 10;
                    }
                    Srv_Card card = new Srv_Card(suit, rank, value);
                    this.cards.add(card);
                }
            }
        }
        return this.cards;
    }

    public void shuffleCards() {
        Collections.shuffle(this.cards);
    }

    public ArrayList<Srv_Card> getCards() {
        return this.cards;
    }
}
