package server.model;

import resources.ServiceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ServiceConfigurationError;
import java.util.logging.Logger;

public enum Srv_HandType {
    SingleCard, OnePair, XPair, Tripple, Street, FullHouse, Bomb;

    private static ServiceLocator sl = ServiceLocator.getServiceLocator();
    private static Logger logger = sl.getLogger();

    public static Srv_HandType evaluateHand(ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards) { //@author Sandro, Thomas
        Srv_HandType handType = null;

        if (isSingleCard(playerCards)) handType = SingleCard;
        if (isOnePair(playerCards)) handType = OnePair;
        if (isXPair(playerCards)) handType = XPair;
        if (isTripple(playerCards)) handType = Tripple;
        if (isStreet(playerCards)) handType = Street;
        if (isFullHouse(playerCards)) handType = FullHouse;
        if (isBomb(playerCards)) handType = Bomb;

        isHigher(tableCards,playerCards, handType);

        return handType;
    }

    public static boolean isSingleCard(ArrayList<Srv_Card> cards) { //@author Sandro, Thomas
        boolean found = false;
        if (cards.size() == 1 && includesSpecialCards(cards) == false) { //normal SingleCard?
            found = true;
        } else {
            if (cards.size() == 1 && includesSpecialCards(cards) == true) { //specialCard?
                callSpecialCard(cards);
                found = true;
            }
        }
        return found;
    }

    public static boolean isOnePair(ArrayList<Srv_Card> cards) { //@author Sandro, Thomas
        boolean found = false;
        Collections.sort(cards);
        if (cards.size() == 2) { //only 2 cards allowed
            if (includesSpecialCards(cards) == false) { //No special card included? -> regular process
                for (int i = 0; i < cards.size() - 1 && !found; i++) {
                    for (int j = i + 1; j < cards.size() && !found; j++) {
                        if (cards.get(i).getRank() == cards.get(j).getRank()) { //2 cards with same rank = onePair
                            found = true;
                        }
                    }
                }
            } else { // special card included?
                if (cards.get(0).getRank() == Srv_Rank.Phoenix) { //Is it a phoenix? Case 2 specialCard: Phoenix has not the highest rank
                    callSpecialCard(cards);
                    found = true; // 1 card and phoenix as joker = onePair
                }
            }
        }
        return found;
    }

    public static boolean isXPair(ArrayList<Srv_Card> tableCards) {
        return false;
    }

    public static boolean isTripple(ArrayList<Srv_Card> cards) { //@author Sandro
        boolean found = false;
        Collections.sort(cards);
        if (cards.size() == 3) { //only 3 cards allowed
            if (includesSpecialCards(cards) == false) { //No special card included? -> regular process
                if (cards.get(0).getRank() == cards.get(1).getRank() && cards.get(1).getRank() == cards.get(2).getRank()) {
                    found = true; //3 cards with same rank = Tripple
                }
            } else { // special card included?
                if (cards.get(0).getRank() == Srv_Rank.Phoenix) { //Is it a phoenix? Case 2 specialCard: Phoenix has not the highest rank
                    if (cards.get(1).getRank() == cards.get(2).getRank()) { //Other 2 cards same rank?
                        callSpecialCard(cards);
                        found = true; //2 cards with same rank and Phoenix as joker = Tripple
                    }
                }
            }
        }
        return found;
    }

    public static boolean isStreet(ArrayList<Srv_Card> cards) {
        boolean found = false;
        int counter = 1; // Counter of the correct cards
        Collections.sort(cards); // Sort the cards from high to low ordinal
        logger.info("CASE isStreet");
        logger.info("Cards: "+cards);

        if (cards.size() >= 5) { //only 5 cards or more allowed
            logger.info("5 or more cards? "+cards.size());
            if (includesSpecialCards(cards) == false) { //No special card included? -> regular process
                logger.info("No specialCard");
                for(int i = 0; i < cards.size()-1; i++) {
                    if(cards.get(i).getRank().ordinal() == cards.get(i+1).getRank().ordinal()+1) {
                        counter++;
                        logger.info("Counter: "+counter);
                    }
                    if (counter >= 5) { // -> 5 Ordinals or more in succesion = Street
                        found = true;
                    }
                }
            } else { // special card included?
                if (cards.get(0).getRank() == Srv_Rank.Mahjong && cards.get(1).getRank() != Srv_Rank.Phoenix) { //Is it a mahjong?
                    callSpecialCard(cards);
                }
                if (cards.get(0).getRank() == Srv_Rank.Phoenix) { //Is it a phoenix?
                    callSpecialCard(cards);
                }
                if (cards.get(0).getRank() == Srv_Rank.Mahjong && cards.get(1).getRank() == Srv_Rank.Phoenix) { //Is it a phoenix and a mahjong?
                    callSpecialCard(cards);
                }

            }
        }
        return found;
    }

    public static boolean isFullHouse(ArrayList<Srv_Card> cards) {
        return false;
    }

    public static boolean isBomb(ArrayList<Srv_Card> cards) {
        return false;
    }

    public static boolean includesSpecialCards(ArrayList<Srv_Card> cards) { //specialCard played? @author Sandro, Thomas
        boolean found = false;
        Collections.sort(cards);
        if (cards.get(0).getSuit() == Srv_Suit.SpecialCards) { //SpecialCards would be always the highest card
            found = true;
        }
        return found;
    }

    public static void callSpecialCard(ArrayList<Srv_Card> cards) { //Call the right specialCard method in Table @author Sandro, Thomas
        Collections.sort(cards);
        logger.info("SpecialCardPlayed");
        switch (cards.get(0).getRank()) { //SpecialCards would be always the highest card
            case Phoenix:
                //Srv_Table.phoenixPlayed();
                logger.info("PhoenixPlayed");
                break;
            case Dragon:
                //Srv_Table.dragonPlayed();
                logger.info("DragonPlayed");
                break;
            case Dog:
                //Srv_Table.dogPlayed();
                logger.info("DogPlayed");
                break;
            case Mahjong:
                //Srv_Table.mahjongPlayed();
                logger.info("MahjongPlayed");
                break;
        }
    }

        public static boolean isHigher (ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards, Srv_HandType handType) { //@author Sandro, Thomas
            boolean isHigher = false;

            switch (handType) {
                case SingleCard:
                    if (tableCards.size() == 0) { // No card on the table -> Player has automatically the higher SingleCard
                        isHigher = true;
                    } else {
                        if (tableCards.size() == 1) { //One Card is on the table
                            if (tableCards.get(0).getRank().ordinal() < playerCards.get(0).getRank().ordinal()) { //Which card is higher?
                                isHigher = true;
                            }
                        }
                    }
                    break;
                case OnePair:
                    if (tableCards.size() == 0) { //No card on the table -> Player has automatically the higher OnePair
                        isHigher = true;
                    } else {
                        if (tableCards.size() == 2) { //Two cards are on the table
                            Collections.sort(tableCards);
                            Collections.sort(playerCards);
                            if (tableCards.get(1).getRank().ordinal() < playerCards.get(1).getRank().ordinal()) { //Which second card is higher? Maybe first Card is a Phoenix
                                isHigher = true;
                            }
                        }
                    }
                    break;
                case Tripple:
                    if (tableCards.size() == 0) { //No card on the table -> Player has automatically the higher Tripple
                        isHigher = true;
                    } else {
                        if (tableCards.size() == 3) { //Three cards are on the table
                            Collections.sort(tableCards);
                            Collections.sort(playerCards);
                            if (tableCards.get(1).getRank().ordinal() < playerCards.get(1).getRank().ordinal()) { //Which second card is higher? Maybe first Card is a Phoenix
                                isHigher = true;
                            }
                        }
                    }
                    break;
            }
            return isHigher;
    }
}
