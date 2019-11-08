package server.model;

import resources.ServiceLocator;

import java.util.*;
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

    public static boolean isBombOnHand(ArrayList<Srv_Card> cards) { //@author thomas
        //method it is  written to check the whole deck from the players for bombs
        //create all the variables we need for the checks
        boolean found = false;
        int counterA = 1;
        int counterB = 1;
        int countCards= 0;
        List<Srv_Card> pagodaCards = new ArrayList<>();
        List<Srv_Card> jadeCards = new ArrayList<>();
        List<Srv_Card> starsCards = new ArrayList<>();
        List<Srv_Card> swordsCards = new ArrayList<>();
        ArrayList<List<Srv_Card>> listOfSuitLists = new ArrayList<>();

        ArrayList<Srv_Card> clonedCards = (ArrayList<Srv_Card>) cards.clone();
        Collections.sort(clonedCards);

        //first we need to remove the special cards from the deck of the players (no special cards can be played with bomb)
        for(int g = 0; g < clonedCards.size()-1; g++){
            if(clonedCards.get(g).getSuit() == Srv_Suit.SpecialCards) {
                clonedCards.remove(clonedCards.get(g));

            }
        }
        // case if the player has 4 cards of the same Rank
        if(clonedCards.size() >= 4){
            for(int i = 0; i < clonedCards.size();i++){
                counterA = 1;
                for(int j = clonedCards.size()-1 ; j >= 0; j--){// check if four cards ae of the same ordinal --> if not reset the counter and check the next
                    if(clonedCards.get(i).getRank().ordinal() == clonedCards.get(j).getRank().ordinal() && counterA != 4 ){
                        counterA ++;
                    }
                }
            }
            if(counterA == 4){ // if the counter reaches 4 there set found to true
                found = true;
            }else{
                //Case if the player has a minimum of 5 in a straight with the same suit
                if(clonedCards.size() >=5){
                    /*
                    put all the different suits in different list, to make them easier to check.
                    then put these lists in a 2D List to iterate trough every list in our loop
                     */
                    starsCards = clonedCards.stream().filter(t -> t.getSuit() == Srv_Suit.Stars).sorted().collect(Collectors.toList());
                    pagodaCards = clonedCards.stream().filter(t -> t.getSuit() == Srv_Suit.Pagodas).sorted().collect(Collectors.toList());
                    swordsCards = clonedCards.stream().filter(t -> t.getSuit() == Srv_Suit.Swords).sorted().collect(Collectors.toList());
                    jadeCards = clonedCards.stream().filter(t -> t.getSuit() == Srv_Suit.Jade).sorted().collect(Collectors.toList());
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
                    if(counterB == 5){
                        found = true;
                    }
                }
            }

        }

        return found;
    }

    public static boolean isBomb(ArrayList<Srv_Card> cards){//@author thomas
        //method to check out the played bomb cards
        boolean found = false;
        int counterA = 1;

        ArrayList<Srv_Card> clonedCards = (ArrayList<Srv_Card>) cards.clone();
        Collections.sort(clonedCards);

        // case if the player has 4 cards of the same Rank
        if(clonedCards.size() == 4 && !includesSpecialCards(clonedCards)) {
            System.out.println(clonedCards);
            for (int i = 0; i < clonedCards.size() - 1; i++) {
                // check if four cards ae of the same ordinal --> if not reset the counter and check the next
                if (clonedCards.get(i + 1).getRank().ordinal() == clonedCards.get(i).getRank().ordinal()) {
                    System.out.println(clonedCards.get(i + 1).getRank().ordinal() == clonedCards.get(i).getRank().ordinal());
                    counterA++;
                }
            }
            if (counterA == 4) { // if the counter reaches 4 there set found to true
                found = true;

            }
        }else{
                System.out.println("else");
                //Case if the player has a minimum of 5 in a straight with the same suit
                if(clonedCards.size() >=5 && !includesSpecialCards(clonedCards)){
                    for (int k = 0; k < clonedCards.size()-1; k++){
                            if ((clonedCards.get(k).getRank().ordinal() - 1 == clonedCards.get(k+1).getRank().ordinal()) &&
                                    (clonedCards.get(k).getSuit() == clonedCards.get(k+1).getSuit())) {
                                found = true;
                                System.out.println(found);
                            }else{
                                found = false;
                            }
                    }
                }
            }
        return found;
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
            boolean bombFourPlayer = false;
            boolean bombStreetPlayer = false;
            boolean bombFourTable = false;
            boolean bombStreetTable = false;

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
            }
            return isHigher;
    }
}
