package server.model;

import java.util.ArrayList;

public enum Srv_HandType {
    HighCard, OnePair, XPair, Tripple, Street, FullHouse, Bomb;

    public static Srv_HandType evaluateHand(ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards, Srv_Rank rank) {
        Srv_HandType currentEval = HighCard;

        if (isOnePair(tableCards, playerCards, rank)) currentEval = OnePair;

        return currentEval;
    }

    public static boolean isHigher(ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards, Srv_Rank rank) {

    }

    public static boolean isOnePair(ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards, Srv_Rank rank) {

    }
}
