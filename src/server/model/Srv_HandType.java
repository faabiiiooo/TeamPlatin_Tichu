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
        logger.info("CASE isTripple");
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

    public static boolean isStreet(ArrayList<Srv_Card> cards) { //@author Sandro
        boolean found = false;
        int counter = 1; // Counter of the correct cards
        Collections.sort(cards); // Sort the cards from high to low ordinal
        logger.info("CASE isStreet");

        if (cards.size() >= 5) { //only 5 cards or more allowed
            logger.info("5 or more cards? "+cards.size()+" cards");
            if (includesSpecialCards(cards) == false) { //No special card included? -> regular process
                logger.info("No specialCard");
                for(int i = 0; i < cards.size()-1; i++) {
                    if(cards.get(i).getRank().ordinal() == cards.get(i+1).getRank().ordinal()+1) {
                        counter++;
                        logger.info("Counter: "+counter);
                    }
                }
                if (counter >= 5) { // -> 5 Ordinals or more in succesion = Street
                    found = true;
                }
            } else { // special card included?
                if (cards.get(0).getRank() == Srv_Rank.Mahjong && cards.get(1).getRank() != Srv_Rank.Phoenix) { //Is it a mahjong?
                    logger.info("Check isStreet with Mahjong");
                    if (cards.get(cards.size()-1).getRank() == Srv_Rank.Two) { //Lowest card must be a 2
                        logger.info("Lowest Card is a 2");
                        for(int i = 1; i < cards.size()-1; i++) {
                            if (cards.get(i).getRank().ordinal() == cards.get(i + 1).getRank().ordinal() + 1) {
                                counter++;
                                logger.info("Counter: " + counter);
                            }
                        }
                    }
                    if (counter >= 4) { // -> 4 Ordinals or more in succesion + Mahjong = Street
                        callSpecialCard(cards);
                        found = true;
                    }
                }
                if (cards.get(0).getRank() == Srv_Rank.Phoenix) { //Is it a phoenix?
                    logger.info("Check isStreet with Phoenix");
                    boolean phoenixUsed = false;
                    for(int i = 1; i < cards.size()-1; i++) {
                        if(cards.get(i).getRank().ordinal() == cards.get(i+1).getRank().ordinal()+1) {
                            counter++;
                            logger.info("Counter: "+counter);
                        } else {
                            if(phoenixUsed == false && cards.get(i).getRank().ordinal() == cards.get(i+1).getRank().ordinal()+2) { //Use Phoenix as Joker
                                counter++;
                                phoenixUsed = true; //phoenix only one time available
                                logger.info("Counter: "+counter);
                            }
                        }
                    }
                    if (counter >= 4) { // -> 4 Ordinals or more in succesion + Phoenix = Street
                        callSpecialCard(cards);
                        found = true;
                    }
                }
                if (cards.get(0).getRank() == Srv_Rank.Mahjong && cards.get(1).getRank() == Srv_Rank.Phoenix) { //Is it a phoenix and a mahjong?
                    logger.info("Check isStreet with Mahjong & Phoenix");
                    boolean phoenixUsed = false;

                    if (cards.get(cards.size()-1).getRank() == Srv_Rank.Two || cards.get(cards.size()-1).getRank() == Srv_Rank.Three) { //Lowest card must be a 2 or a 3
                        logger.info("Lowest Card is a 2 or a 3");
                        for(int i = 2; i < cards.size()-1; i++) {
                            if (cards.get(i).getRank().ordinal() == cards.get(i + 1).getRank().ordinal() + 1) {
                                counter++;
                                logger.info("Counter: " + counter);
                            } else {
                                if(phoenixUsed == false && cards.get(i).getRank().ordinal() == cards.get(i+1).getRank().ordinal()+2) { //Use Phoenix as Joker
                                    counter++;
                                    phoenixUsed = true; //phoenix only one time available
                                    logger.info("Counter: "+counter);
                                }
                            }
                        }
                    }
                    if (counter >= 3) { // -> 3 Ordinals or more in succesion + Mahjong + Phoenix = Street
                        callSpecialCard(cards);
                        found = true;
                    }
                }
            }
        }
        return found;
    }

    public static boolean isFullHouse(ArrayList<Srv_Card> cards) { //@author Sandro
        boolean foundFullHouse = false;
        boolean foundTripple = false;
        boolean foundOnePair = false;
        ArrayList<Srv_Card> clonedCards = (ArrayList<Srv_Card>) cards.clone();
        Collections.sort(clonedCards); // Sort the cards from high to low ordinal
        logger.info("CASE isFullHouse");
        logger.info("Cards: "+clonedCards);

        if (clonedCards.size() == 5) { //only 5 cards allowed
            if (includesSpecialCards(clonedCards) == false) { //No special card included? -> regular process
                logger.info("No specialCard");
                if (clonedCards.get(0).getRank() == clonedCards.get(1).getRank() && clonedCards.get(1).getRank() == clonedCards.get(2).getRank()) { //First three cards same rank?
                    clonedCards.remove(2);
                    clonedCards.remove(1);
                    clonedCards.remove(0);
                    foundTripple = true; //3 cards with same rank = Tripple
                    logger.info("foundTripple");
                } else {
                    if (clonedCards.get(2).getRank() == clonedCards.get(3).getRank() && clonedCards.get(3).getRank() == clonedCards.get(4).getRank()) { //Last three cards same rank?
                        clonedCards.remove(4);
                        clonedCards.remove(3);
                        clonedCards.remove(2);
                        foundTripple = true; //3 cards with same rank = Tripple
                        logger.info("foundTripple");
                    }
                }
                if (clonedCards.get(0).getRank() == clonedCards.get(1).getRank()) { // Are the remaining two cards a pair?
                    foundOnePair = true;
                    logger.info("foundOnePair");
                }
            } else { // special card included?
                if (clonedCards.get(0).getRank() == Srv_Rank.Phoenix) { //Is it a phoenix? Case 2 specialCard: Phoenix has not the highest rank
                    logger.info("Check isFullHouse with Phoenix");
                    clonedCards.remove(0); //delete phoenix from clonedCards
                    if (clonedCards.get(0).getRank() == clonedCards.get(1).getRank() && clonedCards.get(1).getRank() == clonedCards.get(2).getRank()) { //First three cards same rank?
                        foundTripple = true; //3 cards with same rank = Tripple
                        foundOnePair = true; //last card + phoenix = OnePair
                        logger.info("found Tripple + SingleCard + Phoenix");
                    } else {
                        if (clonedCards.get(1).getRank() == clonedCards.get(2).getRank() && clonedCards.get(2).getRank() == clonedCards.get(3).getRank()) { //Last three cards same rank?
                            foundTripple = true; //3 cards with same rank = Tripple
                            foundOnePair = true; //last card + phoenix = OnePair
                            logger.info("found Tripple + SingleCard + Phoenix");
                        }
                    }
                    if (clonedCards.get(0).getRank() == clonedCards.get(1).getRank() && clonedCards.get(2).getRank() == clonedCards.get(3).getRank()) { //Case 2x OnePair + Phoenix
                        foundOnePair = true; //2 cards with same rank = OnePair
                        foundTripple = true; //2 cards with same rank + phoenix = Tripple
                        logger.info("found TwoPair + Phoenix");
                    }
                }
            }
            if (foundTripple == true && foundOnePair == true) {
                foundFullHouse = true;
                logger.info("found fullHouse");
            }
        }
        return foundFullHouse;
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
        for (int i =0; i < cards.size(); i++) {
            switch (cards.get(i).getRank()) { //Check each card
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
    }

        public static boolean isHigher (ArrayList<Srv_Card> tableCards, ArrayList<Srv_Card> playerCards, Srv_HandType handType) { //@author Sandro, Thomas
            boolean isHigher = false;

            switch (handType) {
                case SingleCard:
                    if (tableCards.size() == 0) { // No card on the table -> Player has automatically the higher SingleCard
                        isHigher = true;
                        logger.info("Player SingleCard isHigher");
                    } else {
                        if (tableCards.size() == 1) { //One Card is on the table
                            if (tableCards.get(0).getRank().ordinal() < playerCards.get(0).getRank().ordinal()) { //Which card is higher?
                                isHigher = true;
                                logger.info("Player SingleCard isHigher");
                            }
                        }
                    }
                    break;
                case OnePair:
                    if (tableCards.size() == 0) { //No card on the table -> Player has automatically the higher OnePair
                        isHigher = true;
                        logger.info("Player OnePair isHigher");
                    } else {
                        if (tableCards.size() == 2) { //Two cards are on the table
                            Collections.sort(tableCards);
                            Collections.sort(playerCards);
                            if (tableCards.get(1).getRank().ordinal() < playerCards.get(1).getRank().ordinal()) { //Which second card is higher? Maybe first Card is a Phoenix
                                isHigher = true;
                                logger.info("Player OnePair isHigher");
                            }
                        }
                    }
                    break;
                case Tripple:
                    if (tableCards.size() == 0) { //No card on the table -> Player has automatically the higher Tripple
                        isHigher = true;
                        logger.info("Player Tripple isHigher");
                    } else {
                        if (tableCards.size() == 3) { //Three cards are on the table
                            Collections.sort(tableCards);
                            Collections.sort(playerCards);
                            if (tableCards.get(1).getRank().ordinal() < playerCards.get(1).getRank().ordinal()) { //Which second card is higher? Maybe first Card is a Phoenix
                                isHigher = true;
                                logger.info("Player Tripple isHigher");
                            }
                        }
                    }
                    break;
                case Street:
                    if (tableCards.size() == 0) { //No card on the table -> Player has automatically the higher Street
                        isHigher = true;
                        logger.info("Player Street isHigher");
                    } else {
                        if (tableCards.size() >= 5 && tableCards.size() == playerCards.size()) { //Five cards ore more are on the table && Same number of cards
                            Collections.sort(tableCards);
                            Collections.sort(playerCards);
                            if (tableCards.get(2).getRank().ordinal() < playerCards.get(2).getRank().ordinal()) { // Which third card is higher? First 2 cards could be specialCards
                                isHigher = true;
                                logger.info("Player Street isHigher");
                            }
                        }
                    }
                    break;
                case FullHouse:
                    if (tableCards.size() == 0) { //No card on the table -> Player has automatically the higher FullHouse
                        isHigher = true;
                        logger.info("Player FullHouse isHigher");
                    } else {
                        if (tableCards.size() == 5) { //Five cards are on the table
                            Collections.sort(tableCards);
                            Collections.sort(playerCards);
                            if (tableCards.get(2).getRank().ordinal() < playerCards.get(2).getRank().ordinal()) { // Which third card is higher? Same three cards not possible
                                isHigher = true;
                                logger.info("Player FullHouse isHigher");
                            }
                        }
                    }
                    break;
            }
            return isHigher;
    }
}
