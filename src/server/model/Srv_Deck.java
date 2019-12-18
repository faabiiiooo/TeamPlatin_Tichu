package server.model;

import javafx.beans.property.SimpleIntegerProperty;
import resources.Card;
import resources.Rank;
import resources.Suit;

import java.util.ArrayList;
import java.util.Collections;

//@author Sandro
public class Srv_Deck {

    private final int NUM_OF_CARDS = 56;
    private ArrayList<Card> cards = new ArrayList<>();
    private final SimpleIntegerProperty remainingCards = new SimpleIntegerProperty();

    public Srv_Deck() {
        createCards();
        shuffleCards();
    }

    public ArrayList<Card> createCards() { //Create all 56 cards
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                if (suit == Suit.SpecialCards && rank == Rank.Phoenix) { //create specialCards
                    Card card = new Card(suit, rank, -25);
                    card.setPhoenixRank(1.5); //Phoenix as firstCard = 1.5
                    this.cards.add(card);
                }
                if (suit == Suit.SpecialCards && rank == Rank.Dragon) {
                    Card card = new Card(suit, rank, 25);
                    this.cards.add(card);
                }
                if (suit == Suit.SpecialCards && rank == Rank.Dog) {
                    Card card = new Card(suit, rank, 0);
                    this.cards.add(card);
                }
                if (suit == Suit.SpecialCards && rank == Rank.Mahjong) {
                    Card card = new Card(suit, rank, 0);
                    this.cards.add(card);
                }
                if (suit != Suit.SpecialCards && rank != Rank.Phoenix && rank != Rank.Dragon
                        && rank != Rank.Dog && rank != Rank.Mahjong) { //Create regular cards
                    int value = 0;
                    if (rank == Rank.Five) {
                        value = 5;
                    }
                    if (rank == Rank.Ten || rank == Rank.King) {
                        value = 10;
                    }
                    Card card = new Card(suit, rank, value);
                    this.cards.add(card);
                }
            }
        }
        remainingCards.set(cards.size());
        return this.cards;
    }

    // copy from the poker project from the second semester
    //return 1 card so that it can be dealt to the player (as long as there are cards, there will be returned a card.
    //also set the IntegerProperty to the remaining cards.
    public Card cardToDeal() {
        Card card = (cards.size() > 0) ? cards.remove(cards.size()-1) : null;
        remainingCards.setValue(cards.size());
        return card;
    }

    public void shuffleCards() {
        Collections.shuffle(this.cards);
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public SimpleIntegerProperty getRemainingCardsProperty() { return remainingCards; }

    public int getRemainingCards() { return remainingCards.get(); }
}
