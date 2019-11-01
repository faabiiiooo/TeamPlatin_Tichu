package server.model;

import java.util.ArrayList;
import java.util.Collections;

public enum Srv_HandType {
    HighCard, OnePair, XPair, Tripple, Street, FullHouse, Bomb;

    public static boolean evaluateHand(ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards, Srv_Rank rank) {
        boolean isHigher = false;

        if (tableCards.size() == 0 || isHighCard(tableCards) && isHighCard(playerCards) && playerCards.size() == 1) { //Case HighCard (Table&Player have 1 card or no cards on the table)

        }
        if (tableCards.size() == 0 || isOnePair(tableCards) && isOnePair(playerCards) && playerCards.size() == 2) { //Case OnePair (Table&Player have 2 cards or no cards on the table)
            Collections.sort(tableCards);
            Collections.sort(playerCards);
            if (tableCards.get(1).getRank().ordinal()< playerCards.get(1).getRank().ordinal()) { //Which second card is higher? Maybe first Card is a Phoenix
                isHigher = true;
            }
        }

        return isHigher;
    }

    public static boolean isHighCard(ArrayList<Srv_Card> cards) {
        return false;
    }

    public static boolean isOnePair(ArrayList<Srv_Card> cards) {
        boolean found = false;
        Collections.sort(cards);
        if (includesSpecialCards(cards) == false) { //No special card included? -> regular process
            for (int i = 0; i < cards.size() - 1 && !found; i++) {
                for (int j = i+1; j < cards.size() && !found; j++) {
                    if (cards.get(i).getRank() == cards.get(j).getRank()) {
                        found = true;
                    }
                }
            }
        } else { // special card included?
            if (cards.get(0).getRank() == Srv_Rank.Phoenix) { //Is it a phoenix? Case 2 specialCard: Phoenix has not the highest rank
                found = true;
            }
        }
        return found;
    }

    public static boolean isXPair(ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards, Srv_Rank rank) {
        return false;
    }

    public static boolean isTripple(ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards, Srv_Rank rank) {
        return false;
    }

    public static boolean isStreet(ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards, Srv_Rank rank) {
        return false;
    }

    public static boolean isFullHouse(ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards, Srv_Rank rank) {
        return false;
    }

    public static boolean isBomb(ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards, Srv_Rank rank) {
        return false;
    }

    public static boolean includesSpecialCards(ArrayList<Srv_Card> cards) {
        boolean found = false;
        Collections.sort(cards);
        if (cards.get(0).getSuit() == Srv_Suit.SpecialCards) { //SpecialCards would be always the highest card
            found = true;
        }
        return found;
    }
}
