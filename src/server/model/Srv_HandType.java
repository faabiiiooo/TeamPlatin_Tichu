package server.model;

import resources.Card;
import resources.Rank;
import resources.ServiceLocator;
import resources.Suit;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public enum Srv_HandType {
    SingleCard, OnePair, XPair, Tripple, Street, FullHouse, Bomb;

    private static ServiceLocator sl = ServiceLocator.getServiceLocator();
    private static Logger logger = sl.getLogger();
    private static Srv_Table table = sl.getTable();

    public static boolean evaluateHand(ArrayList<Card> tableCards, ArrayList<Card> playerCards) { //@author Sandro, Thomas
        Srv_HandType handType = null;

        boolean canPlay = false;
        // evaluate handtype from player and last played cards
        if (isSingleCard(playerCards) && isSingleCard(tableCards) || tableCards.size() == 0 && isSingleCard(playerCards))
            handType = SingleCard;
        if (isOnePair(playerCards) && isOnePair(tableCards) || tableCards.size() == 0 && isOnePair(playerCards))
            handType = OnePair;
        if (isXPair(playerCards) && isXPair(tableCards) || tableCards.size() == 0 && isXPair(playerCards))
            handType = XPair;
        if (isTripple(playerCards) && isTripple(tableCards) || tableCards.size() == 0 && isTripple(playerCards))
            handType = Tripple;
        if (isStreet(playerCards) && isStreet(tableCards) || tableCards.size() == 0 && isStreet(playerCards))
            handType = Street;
        if (isFullHouse(playerCards) && isFullHouse(tableCards) || tableCards.size() == 0 && isFullHouse(playerCards))
            handType = FullHouse;
        if (isBomb(playerCards) && isBomb(tableCards) || isBomb(playerCards) && tableCards.size() == 0)
            handType = Bomb;
        // if same handtype evualte which is higher
        canPlay = isHigher(tableCards, playerCards, handType);

        //special case --> special card dog cannot be bombed
        if(isBomb(playerCards) && !isBomb(tableCards) && tableCards.get(0).getRank() != Rank.Dog  ){
            canPlay = true;

            }
        return canPlay;
    }

    public static boolean isSingleCard(ArrayList<Card> cards) { //@author Sandro, Thomas
        boolean found = false;
        if (cards.size() == 1) {
            found = true;
        }
        return found;
    }

    public static boolean isOnePair(ArrayList<Card> cards) { //@author Sandro, Thomas
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
                if (cards.get(0).getRank() == Rank.Phoenix) { //Is it a phoenix? Case 2 specialCard: Phoenix has not the highest rank
                    callSpecialCard(cards);
                    found = true; // 1 card and phoenix as joker = onePair
                }
            }
        }
        return found;
    }

    public static boolean isXPair(ArrayList<Card> cards) { //@author Thomas
        boolean pairsFound = true;
        boolean found = false;
        int correctCalculation = 0;
        ArrayList<Card> clonedCards = (ArrayList<Card>) cards.clone();
        ArrayList<Card> pairCards = new ArrayList<>();
        ArrayList<Card> uniqueList = new ArrayList<>();

        Collections.sort(clonedCards); //sort our lists with the cards
        Collections.sort(cards);
        //case with no played specialcard
        if(!includesSpecialCards(cards) && cards.size() >= 4){
            for(int i = 0; i < clonedCards.size() && pairsFound; i++){ //separately put 2 cards in a new list to send them to the isOnePair Method
                logger.info("clonedCards size: "+ clonedCards.size());
                logger.info("i: "+ i);
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
            if (includesSpecialCards(clonedCards) && cards.size() >= 4){
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

    public static boolean isTripple(ArrayList<Card> cards) { //@author Sandro
        logger.info("CASE isTripple");
        boolean found = false;
        Collections.sort(cards);
        if (cards.size() == 3) { //only 3 cards allowed
            if (includesSpecialCards(cards) == false) { //No special card included? -> regular process
                if (cards.get(0).getRank() == cards.get(1).getRank() && cards.get(1).getRank() == cards.get(2).getRank()) {
                    found = true; //3 cards with same rank = Tripple
                }
            } else { // special card included?
                if (cards.get(0).getRank() == Rank.Phoenix) { //Is it a phoenix? Case 2 specialCard: Phoenix has not the highest rank
                    if (cards.get(1).getRank() == cards.get(2).getRank()) { //Other 2 cards same rank?
                        callSpecialCard(cards);
                        found = true; //2 cards with same rank and Phoenix as joker = Tripple
                    }
                }
            }
        }
        return found;

    }

    public static boolean isStreet(ArrayList<Card> cards) { //@author Sandro
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
                if (counter == cards.size()) { // -> All Ordinals in succesion = Street
                    found = true;
                }
            } else { // special card included?
                if (cards.get(0).getRank() == Rank.Mahjong && cards.get(1).getRank() != Rank.Phoenix) { //Is it a mahjong?
                    logger.info("Check isStreet with Mahjong");
                    if (cards.get(cards.size()-1).getRank() == Rank.Two) { //Lowest card must be a 2
                        logger.info("Lowest Card is a 2");
                        counter++; //Two and Mahjong = Ordinals in succesion
                        for(int i = 1; i < cards.size()-1; i++) { //Check rest of cards
                            if (cards.get(i).getRank().ordinal() == cards.get(i + 1).getRank().ordinal() + 1) {
                                counter++;
                                logger.info("Counter: " + counter);
                            }
                        }
                    }
                    if (counter == cards.size()) { // -> All Ordinals in succesion + Mahjong = Street
                        callSpecialCard(cards);
                        found = true;
                    }
                }
                if (cards.get(0).getRank() == Rank.Phoenix) { //Is it a phoenix?
                    logger.info("Check isStreet with Phoenix");
                    boolean phoenixUsed = false;
                    for(int i = 1; i < cards.size()-1; i++) {
                        if(cards.get(i).getRank().ordinal() == cards.get(i+1).getRank().ordinal()+1) {
                            counter++;
                            logger.info(cards.get(i).getRank()+" and "+cards.get(i+1).getRank());
                            logger.info("Counter: "+counter);
                        } else {
                            if(phoenixUsed == false && cards.get(i).getRank().ordinal() == cards.get(i+1).getRank().ordinal()+2) { //Use Phoenix as Joker
                                counter+=2; //phoenix as joker -> 2 ordinals in succesion
                                phoenixUsed = true; //phoenix only one time available
                                logger.info("Use Phoenix as Joker");
                                logger.info(cards.get(i).getRank()+" and "+cards.get(i+1).getRank());
                                logger.info("Counter: "+counter);
                            }
                        }
                    }

                    if (phoenixUsed == false) { //If phoenix not used -> Use Phoenix as highest Card in the street
                        counter++;
                        logger.info("Use Phoenix as Joker for the highest Card");
                        logger.info("Counter: " + counter);
                    }

                    if (counter == cards.size()) { // -> All Ordinals in succesion + Phoenix = Street
                        callSpecialCard(cards);
                        found = true;
                    }
                }
                if (cards.get(0).getRank() == Rank.Mahjong && cards.get(1).getRank() == Rank.Phoenix) { //Is it a phoenix and a mahjong?
                    logger.info("Check isStreet with Mahjong & Phoenix");
                    boolean phoenixUsed = false;

                    if (cards.get(cards.size()-1).getRank() == Rank.Two) { //Lowest card is a 2
                        counter++; //Mahjong + 2 = Ordinals in succesion
                        logger.info("Lowest Card is a 2");
                        for(int i = 2; i < cards.size()-1; i++) { //check rest of cards
                            logger.info(cards.get(i).getRank()+" and "+cards.get(i+1).getRank());
                            if (cards.get(i).getRank().ordinal() == cards.get(i + 1).getRank().ordinal() + 1) {
                                counter++;
                                logger.info("Counter: " + counter);
                            } else {
                                logger.info(cards.get(i).getRank()+" and "+cards.get(i+1).getRank());
                                if(phoenixUsed == false && (cards.get(i).getRank().ordinal() == cards.get(i+1).getRank().ordinal()+2)) { //Use Phoenix as Joker
                                    counter+=2; //phoenix as joker -> 2 ordinals in succesion
                                    phoenixUsed = true; //phoenix only one time available
                                    logger.info("Use Phoenix as Joker");
                                    logger.info("Counter: "+counter);
                                }
                            }
                        }
                        if (phoenixUsed == false) { //If phoenix not used -> Use Phoenix as highest Card in the street
                            counter++;
                            logger.info("Use Phoenix as Joker for the highest Card");
                            logger.info("Counter: " + counter);
                        }
                    }
                    if (cards.get(cards.size()-1).getRank() == Rank.Three) { //Lowest card is a 3
                        counter+=2; //Mahjong + Phoenix + 3 -> 2 ordinals in succesion
                        logger.info("Lowest Card is a 2 or a 3");
                        logger.info("Use Phoenix as Joker for a Two");
                        for(int i = 2; i < cards.size()-1; i++) {
                            logger.info(cards.get(i).getRank()+" and "+cards.get(i+1).getRank());
                            if (cards.get(i).getRank().ordinal() == cards.get(i + 1).getRank().ordinal() + 1) {
                                counter++;
                                logger.info("Counter: " + counter);
                            }
                        }
                    }

                    if (counter == cards.size()) { // -> All Ordinals in succesion + Mahjong + Phoenix = Street
                        callSpecialCard(cards);
                        found = true;
                    }
                }
            }
        }
        return found;
    }
    //@author Thomas
    // hands need to be checked when some player played the mahjong card and wishes a card
    public static boolean mahJongWishStreet(ArrayList<Card> cards, ArrayList<Card> lastPlayedCards, Card mahJongWishCard) {
        ArrayList<Card> possibleStreetWithWish = new ArrayList<>();
        ArrayList<Card> testedStreetWithWish = new ArrayList<>();
        ArrayList<Card> cardsClone = (ArrayList<Card>) cards.clone();
        boolean found = false;
        boolean phoenixFound = false;

        Collections.sort(cardsClone); // Sort the cards from high to low ordinal

        //remove all the duplicates
        for(int k = cardsClone.size()-1; k > 0; k--){
            if(cardsClone.get(k).getRank().ordinal() == cardsClone.get(k-1).getRank().ordinal()){
                cardsClone.remove(cardsClone.get(k-1));
            }
        }
        //check if there is a phoenix in the cards list (if yes set true) and remove it. After that check the list on other special cards and remove all of them from the list from the hand
        for(int o = cardsClone.size()-1 ; o >= 0; o--){
            if(cardsClone.get(o).getRank() == Rank.Phoenix){
                phoenixFound = true;
                cardsClone.remove(cardsClone.get(o));
            }else {
                if (cardsClone.get(o).getRank() == Rank.Dog || cardsClone.get(o).getRank() == Rank.Dragon) {
                    logger.info("Dog or Dragon? "+cardsClone.get(o));
                    cardsClone.remove(cardsClone.get(o));
                }
            }
        }
        logger.info("list after filtering: "+cardsClone);
        //case if no phoenix in hand
        if (cards.size() >= 5) { //only 5 cards or more allowed
            if (!phoenixFound) { //No special card included? -> regular process
                logger.info("No specialCard");
                for (int i = 0; i < cardsClone.size() - 1; i++) {//add all the cards which have 1 difference to the next card to possibleStreetWihCard if they are not already in the list
                    if (cardsClone.get(i).getRank().ordinal() == cardsClone.get(i + 1).getRank().ordinal() + 1) {
                        if (!possibleStreetWithWish.contains(cardsClone.get(i))) {
                            possibleStreetWithWish.add(cardsClone.get(i));
                        }
                        if (!possibleStreetWithWish.contains(cardsClone.get(i+1))) {
                            possibleStreetWithWish.add(cardsClone.get(i + 1));
                        }
                        logger.info("List: "+ possibleStreetWithWish);
                    } else {
                        //if there are not at least 5 cards in the list and 1 check is false clear the list
                        if (possibleStreetWithWish.size() < 5) {
                            logger.info("not true possibleStreetWish: " + possibleStreetWithWish.size());
                            possibleStreetWithWish.clear();
                        }
                    }
                }
                logger.info("1. Check " + possibleStreetWithWish.size() + "cards: " +possibleStreetWithWish );
                /*
                if the street is in the list but there are also some other cards which were true in the last check --> make the same check by starting from the other side of the list
                this could be the case when  there are some other subsequent cards in the list, but not enough for a street
                                                                                                                                                                                       */
                    for (int i = possibleStreetWithWish.size() - 1; i > 0; i--) {//add all the cards which have 1 difference to the next card to possibleStreetWihCard
                        logger.info("testedStreetWish in Schlaufe:" + testedStreetWithWish);
                        if (possibleStreetWithWish.get(i).getRank().ordinal() == possibleStreetWithWish.get(i - 1).getRank().ordinal() - 1) {
                            if (!testedStreetWithWish.contains(possibleStreetWithWish.get(i))) {
                                testedStreetWithWish.add(possibleStreetWithWish.get(i));
                            }
                            if (!testedStreetWithWish.contains(possibleStreetWithWish.get(i - 1))) {
                                testedStreetWithWish.add(possibleStreetWithWish.get(i - 1));
                            }
                        } else if (testedStreetWithWish.size() < 5) {
                            logger.info("TESTEDStreetWish: " + testedStreetWithWish);
                            testedStreetWithWish.clear();
                        }
                    }
                    logger.info("Leer?" + testedStreetWithWish);
                }
            Collections.sort(testedStreetWithWish);
            //check if the street contains the wished card from Mah Jong and check if the third card has the higher ordinal than the last played cards (first and second card could be special cards)
            for(Card c : testedStreetWithWish){
                logger.info("check if wish card is inside: "+ (c.getRank().ordinal() == mahJongWishCard.getRank().ordinal() ) + "Card: "+ c );
                logger.info("streetWish "+ testedStreetWithWish);
                logger.info("lastplayedCards "+ lastPlayedCards);
                if(c.getRank().ordinal() == mahJongWishCard.getRank().ordinal() &&

                        testedStreetWithWish.get(2).getRank().ordinal() > lastPlayedCards.get(2).getRank().ordinal()){

                    found = true;
                }
            }
        }
        //case if there was a phoenix on the hand from the player
        if(cards.size()>=5 && phoenixFound) {
            logger.info("Phoenix");

            for (int i = cardsClone.size() - 1; i > 0; i--) {//add all the cards which have 1 difference to the next card to possibleStreetWihCard
                int j = i - 1;
                if (cardsClone.get(i).getRank().ordinal() == cardsClone.get(i - 1).getRank().ordinal() - 1 ||
                        cardsClone.get(i).getRank().ordinal() == cardsClone.get(i - 1).getRank().ordinal() - 2) {
                    if (j != 0) {
                        j--;
                    }
                    if (!possibleStreetWithWish.contains(cardsClone.get(i))) {
                        possibleStreetWithWish.add(cardsClone.get(i));
                    }
                    if (!possibleStreetWithWish.contains(cardsClone.get(i - 1))) {
                        possibleStreetWithWish.add(cardsClone.get(i - 1));
                    }
                    //because there was a phoenix on the hand -> if the cards are not subsequent, check if the card after next would be subsequent
                } else if (cardsClone.get(i).getRank().ordinal() == cardsClone.get(j).getRank().ordinal() - 2) {

                    if (!possibleStreetWithWish.contains(cardsClone.get(i))) {
                        possibleStreetWithWish.add(cardsClone.get(i));
                    }
                    if (!possibleStreetWithWish.contains(cardsClone.get(j))) {
                        possibleStreetWithWish.add(cardsClone.get(j));
                    }
                    //if nothing from the above is true and the list is smaller than 4 cards clear it for the next test
                } else if (possibleStreetWithWish.size() < 4) {
                    possibleStreetWithWish.clear();
                    //if there are not at least 3 cards left to check stop the check
                } else if (cardsClone.size() - i <= 3) {
                    i = 0;
                }

            }
            // do the same checks like above but start from the other side of the list.
            Collections.sort(possibleStreetWithWish);
            for (int i = 0; i < possibleStreetWithWish.size() - 1; i++) {//add all the cards which have 1 difference to the next card to possibleStreetWihCard
                int j = i + 1;
                if (possibleStreetWithWish.get(i).getRank().ordinal() == possibleStreetWithWish.get(i + 1).getRank().ordinal() + 1 ||
                        possibleStreetWithWish.get(i).getRank().ordinal() == possibleStreetWithWish.get(i + 1).getRank().ordinal() + 2) {
                    if (j != possibleStreetWithWish.size() - 1) {
                        j++;
                    }
                    if (!testedStreetWithWish.contains(possibleStreetWithWish.get(i))) {
                        testedStreetWithWish.add(possibleStreetWithWish.get(i));
                    }
                    if (!testedStreetWithWish.contains(possibleStreetWithWish.get(i + 1))) {
                        testedStreetWithWish.add(possibleStreetWithWish.get(i + 1));
                    }

                } else if (possibleStreetWithWish.get(i).getRank().ordinal() == possibleStreetWithWish.get(j).getRank().ordinal() + 2) {

                    if (!testedStreetWithWish.contains(possibleStreetWithWish.get(i))) {
                        testedStreetWithWish.add(possibleStreetWithWish.get(i));
                    }
                    if (!testedStreetWithWish.contains(possibleStreetWithWish.get(j))) {
                        testedStreetWithWish.add(possibleStreetWithWish.get(j));
                    }
                    logger.info("not true possibleStreetWish: " + possibleStreetWithWish.size());
                } else if (testedStreetWithWish.size() < 4) {
                    logger.info("OCCURED: size smaller than 4");
                    testedStreetWithWish.clear();
                } else if (possibleStreetWithWish.size() - i <= 3) {
                    logger.info("occured" + (testedStreetWithWish.size() - i));
                    i = 0;
                }
            }

            Collections.sort(testedStreetWithWish);
            Collections.sort(lastPlayedCards);
            //check if the street contains the wished card from Mah Jong and check if the third card has the higher ordinal than the last played cards
            for (Card c : testedStreetWithWish) {
                if (c.getRank().ordinal() == mahJongWishCard.getRank().ordinal() &&

                        testedStreetWithWish.get(2).getRank().ordinal() > lastPlayedCards.get(2).getRank().ordinal()) {

                    found = true;
                }

            }
        }

        return found;
    }

    public static boolean isFullHouse(ArrayList<Card> cards) { //@author Sandro
        boolean foundFullHouse = false;
        boolean foundTripple = false;
        boolean foundOnePair = false;
        ArrayList<Card> clonedCards = (ArrayList<Card>) cards.clone();
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
            } else { // special card included
                if (clonedCards.get(0).getRank() == Rank.Phoenix) { //Is it a phoenix? Case 2 specialCard: Phoenix has not the highest rank
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

    public static boolean isBombOnHand(ArrayList<Card> cards) { //@author thomas
        //method it is  written to check the whole deck from the players for bombs
        //create all the variables we need for the checks
        boolean found = false;
        int counterA = 0;
        int counterB = 1;
        int countCards= 0;
        List<Card> pagodaCards = new ArrayList<>();
        List<Card> jadeCards = new ArrayList<>();
        List<Card> starsCards = new ArrayList<>();
        List<Card> swordsCards = new ArrayList<>();
        ArrayList<List<Card>> listOfSuitLists = new ArrayList<>();

        ArrayList<Card> clonedCards = (ArrayList<Card>) cards.clone();
        Collections.sort(clonedCards);

        //first we need to remove the special cards from the deck of the players (no special cards can be played with bomb)
        for(int g = 0; g < clonedCards.size()-1; g++){
            if(clonedCards.get(g).getSuit() == Suit.SpecialCards) {
                clonedCards.remove(clonedCards.get(g));

            }
        }
        logger.info(clonedCards+"");
            for(int i = 0; i < clonedCards.size() && counterA != 4 ;i++){
                logger.info("i: "+i );
                counterA = 0;
                for(int j = clonedCards.size()-1 ; j >= 0 && counterA != 4 ; j--){// check if four cards are of the same ordinal --> if not reset the counter and check the next
                    logger.info(" j:"+j);
                    if(clonedCards.get(i).getRank().ordinal() == clonedCards.get(j).getRank().ordinal() ){
                        logger.info("4 of the same Rank to check ");
                        logger.info("Cards: " +clonedCards.get(i)+ " "+ clonedCards.get(j)+" is it true? "+ (clonedCards.get(i).getRank().ordinal() == clonedCards.get(j).getRank().ordinal() && counterA != 4 ) );
                        counterA ++;
                        logger.info("counterA: "+counterA);
                    }
                }
            }
            logger.info("counterA: "+counterA);
            if(counterA == 4){ // if the counter reaches 4 there set found to true
                found = true;
            }else{
                //Case if the player has a minimum of 5 in a straight with the same suit
                    /*
                    put all the different suits in different list, to make them easier to check.
                    then put these lists in a 2D List to iterate trough every list in our loop
                     */
                    starsCards = clonedCards.stream().filter(t -> t.getSuit() == Suit.Stars).sorted().collect(Collectors.toList());
                    pagodaCards = clonedCards.stream().filter(t -> t.getSuit() == Suit.Pagodas).sorted().collect(Collectors.toList());
                    swordsCards = clonedCards.stream().filter(t -> t.getSuit() == Suit.Swords).sorted().collect(Collectors.toList());
                    jadeCards = clonedCards.stream().filter(t -> t.getSuit() == Suit.Jade).sorted().collect(Collectors.toList());
                    listOfSuitLists.add(starsCards); listOfSuitLists.add(pagodaCards); listOfSuitLists.add(swordsCards); listOfSuitLists.add(jadeCards);

                    /*
                    check every suit separately, if the first checked number is only 1 higher than the next.
                    if 5 cards are found set found to true.
                     */
                    for (int k = 0; k < listOfSuitLists.size()-1 && counterB!=5; k++){
                        counterB = 1;
                        countCards=0;

                        for(int u = countCards; u < listOfSuitLists.get(k).size()-1; u++) {

                            if ((listOfSuitLists.get(k).get(u).getRank().ordinal() - 1 == listOfSuitLists.get(k).get(u+1).getRank().ordinal())) {
                                counterB++;
                            }
                        }
                    }
                    if(counterB >= 5){
                        logger.info("bomb on hands -> Straight flush " + counterB);
                        found = true;
                    }
            }


        logger.info("Checked hand on bomb, value: "+found +"Playerhands: "+cards );
        return found;
    }

    public static boolean isBomb(ArrayList<Card> cards){//@author thomas
        //method to check out the played bomb cards
        boolean found = false;
        int counterA = 1;

        ArrayList<Card> clonedCards = (ArrayList<Card>) cards.clone();
        Collections.sort(clonedCards);

        // case if the player has 4 cards of the same Rank
        if(clonedCards.size() == 4 && !includesSpecialCards(clonedCards)) {
            for (int i = 0; i < clonedCards.size() - 1; i++) {
                // check if four cards ae of the same ordinal --> if not reset the counter and check the next
                if (clonedCards.get(i + 1).getRank().ordinal() == clonedCards.get(i).getRank().ordinal()) {
                    counterA++;
                }
            }
            if (counterA == 4) { // if the counter reaches 4 there set found to true
                found = true;

            }
        }else{
                //Case if the player has a minimum of 5 in a straight with the same suit
                if(clonedCards.size() >=5 && !includesSpecialCards(clonedCards)){
                    for (int k = 0; k < clonedCards.size()-1; k++){
                            if ((clonedCards.get(k).getRank().ordinal() - 1 == clonedCards.get(k+1).getRank().ordinal()) &&
                                    (clonedCards.get(k).getSuit() == clonedCards.get(k+1).getSuit())) {
                                found = true;
                            }else{
                                found = false;
                            }
                    }
                }
            }
        return found;
        }


    public static boolean includesSpecialCards(ArrayList<Card> cards) { //specialCard played? @author Sandro, Thomas
        boolean found = false;
        Collections.sort(cards);
        if (cards.get(0).getSuit() == Suit.SpecialCards) { //SpecialCards would be always the highest card
            found = true;
        }
        return found;
    }

    public static void callSpecialCard(ArrayList<Card> cards) { //Call the right specialCard method in Table @author Sandro, Thomas
        Collections.sort(cards);
        logger.info("SpecialCardPlayed");
        for (int i =0; i < cards.size(); i++) {
            switch (cards.get(i).getRank()) { //Check each card
                case Phoenix:
                    table.phoenixPlayed(cards.get(i));
                    logger.info("PhoenixPlayed");
                    break;
                case Dragon:
                    table.dragonPlayed();
                    logger.info("DragonPlayed");
                    break;
                case Dog:
                    table.dogPlayed();
                    logger.info("DogPlayed");
                    break;
               /*case Mahjong:
                    table.mahJongPlayed();
                    logger.info("MahjongPlayed");
                    break;*/
            }
        }
    }

        public static boolean isHigher (ArrayList<Card> tableCards, ArrayList<Card> playerCards, Srv_HandType handType) { //@author Sandro, Thomas
            boolean isHigher = false;

            if(handType  == null){ //if no handtype got evaluated, only return false
                return false;
            }


            switch (handType) {
                case SingleCard:
                    if (tableCards.size() == 0) { // No card on the table -> Player has automatically the higher SingleCard
                        isHigher = true;
                        if (includesSpecialCards(playerCards) == true) { //specialCard?
                            callSpecialCard(playerCards);
                        }
                        logger.info("Player SingleCard isHigher");
                    } else {
                        if (tableCards.size() == 1 && playerCards.get(0).getRank() != Rank.Dog) { //One Card is on the table / Dog not allowed to play in a running game
                            if (tableCards.get(0).getRank() == Rank.Phoenix) { //on the Table a Phoenix?
                                if (tableCards.get(0).getPhoenixRank() < playerCards.get(0).getRank().ordinal()+2) { //compare with phoenixRank
                                    isHigher = true;
                                }
                            } else {
                                if (tableCards.get(0).getRank().ordinal() < playerCards.get(0).getRank().ordinal()) { //compare with normal rank
                                    isHigher = true;
                                    logger.info("Player SingleCard isHigher");
                                }else{
                                    if(tableCards.get(0).getRank()== Rank.Mahjong){//if mahjong is single card, all other cards are higher
                                        isHigher = true;
                                    }
                                }
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
                case Bomb:
                    Collections.sort(tableCards); Collections.sort(playerCards);
                    if(!includesSpecialCards(playerCards)) {


                        //if the playercards are the first cards played/no special cards are included/ size of playercards is 4 set isHigher to true
                        // is also true when the "street" from the player is higher than that on the table
                        if (tableCards.size() < playerCards.size() ) {
                            isHigher = true;
                        } else {
                            // if both players played the same amount of cards check if the highest card from the player is higher than the one played last
                            if (tableCards.size() == playerCards.size() && tableCards.get(0).getRank().ordinal() < playerCards.get(0).getRank().ordinal()){
                                isHigher = true;
                            }
                        }
                    }
                    break;
                default:
                    isHigher = false;
                    break;
            }
            return isHigher;
    }


}
