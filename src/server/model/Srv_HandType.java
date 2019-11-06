package server.model;

import resources.ServiceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ServiceConfigurationError;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
                        if (cards.get(i).getRank() == cards.get(j).getRank()) {
                            found = true;
                        }
                    }
                }
            } else { // special card included?
                if (cards.get(0).getRank() == Srv_Rank.Phoenix) { //Is it a phoenix? Case 2 specialCard: Phoenix has not the highest rank
                    callSpecialCard(cards);
                    found = true;
                }
            }
        }
        return found;
    }

    public static boolean isXPair(ArrayList<Srv_Card> cards) { //@author Thomas
        boolean pairsFound = true;
        boolean found = false;
        int correctCalculation = 0;
        ArrayList<Srv_Card> clonedCards = (ArrayList<Srv_Card>) cards.clone();
        ArrayList<Srv_Card> pairCards = new ArrayList<>();
        ArrayList<Srv_Card> uniqueList = new ArrayList<>();

        Collections.sort(clonedCards); //sort our lists with the cards
        Collections.sort(cards);
        //case with no played specialcard
        if(!includesSpecialCards(cards)){
            for(int i = 0; i < clonedCards.size() && pairsFound; i++){ //separately put 2 cards in a new list to send them to the isOnePair Method
                  pairCards.add(clonedCards.get(i)); pairCards.add(clonedCards.get(i+1));
                  if (isOnePair(pairCards)){ // if the sent 2 cards are one pair add 1 card to our uniqueList which we need later
                      uniqueList.add(pairCards.get(1));
                      pairCards.clear(); // to not fail the isOnePair-test clear our list and make space for the next two cards to check
                      i++; // to check two different cards in every round we need to increase our variable in the loop
                  }else{
                      pairsFound = false; // in the case there are no special cards, the method stops when there is one wrong feedback from the isOnePair Method
                  }
            }
            if(pairsFound) { //if all the sent cards are pairs, we need to check if the pairs are successively
                System.out.println("pairsFound");
                for (int c = 1; c < uniqueList.size(); c++){
                    /*we iterate through our unique list and subtract always
                    the first coming card value from the next coming card value in the list.
                    If the pairs are subsequent the result should always be 1*/
                    correctCalculation = uniqueList.get(c-1).getRank().ordinal() - uniqueList.get(c).getRank().ordinal();
                }
                if(correctCalculation == 1)
                    found = true;
            }


        }else {

            //the case if a phoenix is included
            if (includesSpecialCards(clonedCards)){
                callSpecialCard(clonedCards); //call the method for the special card
                for(int i = cards.size()-1; i-1 >= 0; i--){
                    pairCards.add(cards.get(i)); pairCards.add(cards.get(i-1)); //add 2 cards to the paircards list to check them in the isOnePair method
                    if (isOnePair(pairCards)){ // if the cards are a pair add the second to the unique list (we dont want the phoenix to be in the unique list)
                        uniqueList.add(pairCards.get(1));
                        /*remove them from the list, in case the phoenix is used with the highest card in the list so
                        we can check this pair at the end of our iteration and we can be sure the phoenix is assigned to the right card
                         */
                        clonedCards.remove(clonedCards.get(i)); clonedCards.remove(clonedCards.get(i-1));
                        pairCards.clear(); // clear the pair cards after every iteration
                        pairsFound = true;
                    }else{//if the first checked cards are no pair, there is a chance that the highest card is used with the phoenix
                        pairCards.clear();
                        pairsFound = false;
                    }

                }

            }
            if(pairsFound){
                /*
                if there are only pairs we also need to check if they are subsequent.
                We do not use the first card of the list for our calculation because it would adulterate the result
                 */
                Collections.sort(uniqueList); //sort the list
                for (int c = 2; c < uniqueList.size(); c++){
                    correctCalculation = uniqueList.get(c-1).getRank().ordinal() - uniqueList.get(c).getRank().ordinal();
                }
                //if the result is 1 the pairs are subsequent and the xPair is true
                if(correctCalculation == 1)
                    found = true;
            }
        }
        return found;


    }

    public static boolean isTripple(ArrayList<Srv_Card> tableCards) {
        return false;
    }

    public static boolean isStreet(ArrayList<Srv_Card> tableCards) {
        return false;
    }

    public static boolean isFullHouse(ArrayList<Srv_Card> tableCards) {
        return false;
    }

    public static boolean isBomb(ArrayList<Srv_Card> tableCards) {
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
                case XPair:
                    Collections.sort(tableCards); Collections.sort(playerCards); // sort the cards on table and from the player
                    if(tableCards.size() == 0){ // if there are no cards played before the played xpair is ok
                        isHigher = true;
                    }else{
                        //if both lists have the same size and the second card from the playerlist is higher, the cards can be played
                        if(tableCards.size() == playerCards.size() && tableCards.get(1).getRank().ordinal() < playerCards.get(1).getRank().ordinal() ){
                            isHigher = true;
                        }
                    }
                    break;
            }
            return isHigher;
    }
}
