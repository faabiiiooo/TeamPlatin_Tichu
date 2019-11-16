package server.model;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Collections;

public class Srv_Deck { //@author Sandro

    private final int NUM_OF_CARDS = 56;
    private ArrayList<Srv_Card> cards = new ArrayList<>();
    private final SimpleIntegerProperty remainingCards = new SimpleIntegerProperty();

    public Srv_Deck() {
        createCards();
        shuffleCards();
    }

    public ArrayList<Srv_Card> createCards() { //Create all 56 cards
        for (Srv_Suit suit : Srv_Suit.values()) {
            for (Srv_Rank rank : Srv_Rank.values()) {
                if (suit == Srv_Suit.SpecialCards && rank == Srv_Rank.Phoenix) { //create specialCards
                    Srv_Card card = new Srv_Card(suit, rank, -25); //Phoenix as firstCard = 1.5
                    card.setPhoenixRank(1.5);
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
        remainingCards.set(cards.size());
        return this.cards;
    }

    //@author copy from the poker project from the second semester
    //return 1 card so that it can be dealt to the player (as long as there are cards, there will be returned a card.
    //also set the IntegerProperty to the remaining cards.
    public Srv_Card cardToDeal() {
        Srv_Card card = (cards.size() > 0) ? cards.remove(cards.size()-1) : null;
        remainingCards.setValue(cards.size());
        return card;
    }

    public void shuffleCards() {
        Collections.shuffle(this.cards);
    }

    public ArrayList<Srv_Card> getCards() {
        return this.cards;
    }

    public SimpleIntegerProperty getRemainingCardsProperty() { return remainingCards; }

    public int getRemainingCards() { return remainingCards.get(); }
}
