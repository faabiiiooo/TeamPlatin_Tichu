package server.model;

import org.junit.Before;
import org.junit.Test;
import resources.Card;
import resources.Rank;
import resources.Suit;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Srv_HandTypeTest { //@author Sandro, Thomas

    private static String[][] singleCardCardsTable = {
            { "2S" },
    };

    private static String[][] singleCardCardsPlayer = {
            { "DE" },
    };

    private static String[][] onePairCardsTable = {
            { "5S", "5K" },
    };

    private static String[][] onePairCardsPlayer = {
            { "6S", "PE" },
    };

    private static String[][] XPairCardsTable = {
            { "6S", "6K", "7S", "7K", "8K", "8S"  },
    };

    private static String[][] XPairCardsPlayer = {
            //{ "8S", "9K", "9S", "8K", "PE", "7S"  }, //with phoenix
            { "8S","8P","9K", "9S",  }, //no special card
    };


    private static String[][] trippleCardsTable = {
            { "5S", "5K", "5P" },
    };

    private static String[][] trippleCardsPlayer = {
          //  { "6S", "6K", "6P" }, // case true regular
            { "6S", "PE", "6P" }, //case true phoenix
            //{ "6S", "6K", "4P" }, //case false
    };

    private static String[][] streetCardsTable = {
            { "4S", "5S", "6K", "7P", "8P" },
            //{ "4S", "5S", "6K", "7P", "8P", "9P" },
    };

    private static String[][] streetCardsPlayer = {
          //  { "5K", "6K", "7P", "8P", "9P" }, //case true 5 cards
           // { "TK", "JK", "QP", "KP", "AP" }, //case true 5 cards
          //  { "9K", "TK", "JK", "QP", "KP", "AP" }, //case true 6 cards
          //  { "ME", "3K", "2P", "4P", "5P" }, //case true with mahjong
           // { "PE", "5K", "7P", "8P", "6P" }, //case true with phoenix
          //  { "PE", "ME", "2P", "3P", "4P", "5K" }, //case true with mahjong + phoenix
          //  { "PE", "ME", "5P", "3P", "4P" }, //case true with mahjong + phoenix
          //  { "6P", "7P", "8P", "9P", "JP" }, //case false
          //  { "ME", "3K", "2P", "4P", "6P" }, //case false with mahjong
          //  { "PE", "9K", "TP", "KP", "AP" }, //case false with phoenix
          //  { "PE", "ME", "5P", "6P", "4P" }, //case false with mahjong + phoenix
          //    { "4P", "8K", "5P", "6P", "7P", "9K" }, //case false longer street than table
           // { "4P", "8K", "5P", "6P", "7P", "9K", "JK", "KK" }, //case false 2 cards not in ordinal
           // { "ME", "3K", "2P", "4P", "5P", "6K", "8K" }, //case false with mahjong 2 cards not in ordinal
           //   { "PE", "9K", "TP", "KP", "KP", "AP" }, //case false with phoenix 2 cards not in ordinal
           //  { "PE", "ME", "5P", "3P", "4P", "7P" }, //case false with mahjong + phoenix
    };

    private static String[][] fullHouseCardsTable = {
            { "2S", "2S", "2K", "3P", "3P" },
    };

    private static String[][] fullHouseCardsPlayer = {
         //   { "2S", "2S", "3K", "3P", "3P" }, //case true
         //   { "2S", "2S", "3K", "3P", "3P" }, //case true
         //   { "2S", "3S", "3K", "3P", "PE" }, //case true Tripple + SingleCard + Phoenix
         //   { "5S", "3S", "3K", "3P", "PE" }, //case true Tripple + SingleCard + Phoenix
            { "2S", "2S", "3K", "3P", "PE" }, //case true TwoPair + Phoenix
          //  { "2S", "2S", "3K", "3P", "4K" }, //case false
          //    { "8S", "2S", "3K", "3P", "PE" }, //case false
    };

    private static String[][] bombCardsTable = {
           { "4S", "4K", "4P"},
           // { "8S", "9S", "7S", "6S", "5S", }

    };

    private static String[][] bombCardsPlayer = {
            { "5S", "5K", "5P", "5J"},
            //{ "8S", "9S", "7S", "6S", "5S","4S" }


    };

    private static String[][] bombCardsTableFullHand = {
           // { "4S", "4K", "4P", "4J"},
           //{ "2S", "3S", "4S", "5S","6S", "7S", "8S", "9S", "TS", "ME"},

    };

    private static String[][] bombCardsPlayerFullHand = {
           // { "5S", "5K", "5P", "5J"},
            //{ "4S", "4K", "4P", "4J","8S", "9K", "9S", "8K", "7K", "7S"},
           //{ "5K", "4K", "6K", "7K","8J", "9J", "5S", "8S", "6S", "8K", "PE"},
           // { "4S", "4K", "4P", "4J","8S", "9K", "9S", "8K", "7K","PE"}, //4er + straight + special card
            //{ "ME", "AJ", "AP","AS", "6P", "4K", "2S", "7K","KK", "TK", "9K", "7S","QP" },
            { "2K", "3K", "4K", "5K", "ME"}

    };

    private static String[][] mJStreetWishCardsHandPlayer = {
             //{ "2P", "3P", "4P","5K", "6K", "6P","7K", "8P","TK","AP","AS" },
            // { "2P","3P","5K","6S","TK","JK","AK","KS","PE" },
            { "3P","5K","6S","7K","8K","TK","JK","QP", "KS","PE","WE" },


    };

    private static String[][] mJStreetWishCardsHandsTable = {
            //{ "2P", "3P", "4P","5K", "6K", "6P","7K", "8P","TK","AP","AS" },
            {  "ME", "2S", "3P", "4P","5K", }, //case true 5 cards


    };

    // This is where we store the translated hands
    ArrayList<ArrayList<Card>> singleCardHandsTable;
    ArrayList<ArrayList<Card>> singleCardHandsPlayer;
    ArrayList<ArrayList<Card>> onePairHandsTable;
    ArrayList<ArrayList<Card>> onePairHandsPlayer;
    ArrayList<ArrayList<Card>> XPairHandsTable;
    ArrayList<ArrayList<Card>> XPairHandsPlayer;

    ArrayList<ArrayList<Card>> trippleHandsTable;
    ArrayList<ArrayList<Card>> trippleHandsPlayer;
    ArrayList<ArrayList<Card>> streetHandsTable;
    ArrayList<ArrayList<Card>> streetHandsPlayer;
    ArrayList<ArrayList<Card>> fullHouseHandsTable;
    ArrayList<ArrayList<Card>> fullHouseHandsPlayer;
    ArrayList<ArrayList<Card>> bombHandsTable;
    ArrayList<ArrayList<Card>> bombHandsPlayer;
    ArrayList<ArrayList<Card>> bombHandsTableFullHand;
    ArrayList<ArrayList<Card>> bombHandsPlayerFullHand;
    ArrayList<ArrayList<Card>> mJStreetWishHandPlayer;
    ArrayList<ArrayList<Card>> mJStreetWishHandTable;

    /**
     * The makeHands method is called before each test method,
     * and prepares the translated hands. We recreate these for
     * each test method, in case the test method damages the data. */

    @Before
    public void makeHands() {
        singleCardHandsTable = makeHands(singleCardCardsTable);
        singleCardHandsPlayer = makeHands(singleCardCardsPlayer);
        onePairHandsTable = makeHands(onePairCardsTable);
        onePairHandsPlayer = makeHands(onePairCardsPlayer);
        XPairHandsTable = makeHands(XPairCardsTable);
        XPairHandsPlayer = makeHands(XPairCardsPlayer);

        trippleHandsTable = makeHands(trippleCardsTable);
        trippleHandsPlayer = makeHands(trippleCardsPlayer);
        streetHandsTable = makeHands(streetCardsTable);
        streetHandsPlayer = makeHands(streetCardsPlayer);
        fullHouseHandsTable = makeHands(fullHouseCardsTable);
        fullHouseHandsPlayer = makeHands(fullHouseCardsPlayer);
        bombHandsTable = makeHands(bombCardsTable);
        bombHandsPlayer = makeHands(bombCardsPlayer);
        bombHandsTableFullHand = makeHands(bombCardsTableFullHand);
        bombHandsPlayerFullHand = makeHands(bombCardsPlayerFullHand);
        mJStreetWishHandPlayer = makeHands(mJStreetWishCardsHandPlayer);
        mJStreetWishHandTable = makeHands(mJStreetWishCardsHandsTable);
    }

    @Test // This is the test method for isHigher in HandType.
    public void testIsHigherSingleCard() {

        ArrayList<Card> handTableZero = new ArrayList<Card>();
        Srv_HandType ht = Srv_HandType.SingleCard;

        //Case SingleCard Table has 0 Cards
        for (ArrayList<Card> handPlayer : singleCardHandsPlayer) {
            assertTrue(Srv_HandType.SingleCard.isHigher(handTableZero, handPlayer, ht));
        }

        //Case SingleCard Table has 1 Card
        for (ArrayList<Card> handTable : singleCardHandsTable) {
            for (ArrayList<Card> handPlayer : singleCardHandsPlayer) {
                assertTrue(Srv_HandType.SingleCard.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherOnePair() {

        ArrayList<Card> handTableZero = new ArrayList<Card>();
        Srv_HandType ht = Srv_HandType.OnePair;

        //Case OnePair Table has 0 Cards
        for (ArrayList<Card> handPlayer : onePairHandsPlayer) {
            assertTrue(Srv_HandType.OnePair.isHigher(handTableZero, handPlayer, ht));
        }

        //Case OnePair Table has 2 Cards
        for (ArrayList<Card> handTable : onePairHandsTable) {
            for (ArrayList<Card> handPlayer : onePairHandsPlayer) {
                assertTrue(Srv_HandType.OnePair.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherXPair() {

        ArrayList<Card> handTableZero = new ArrayList<Card>();
        Srv_HandType ht = Srv_HandType.XPair;

        //Case OnePair Table has 0 Cards
        for (ArrayList<Card> handPlayer : XPairHandsPlayer) {
            assertTrue(Srv_HandType.XPair.isHigher(handTableZero, handPlayer, ht));
        }

        //Case XPair cards on table
        for (ArrayList<Card> handTable : XPairHandsTable) {
            for (ArrayList<Card> handPlayer : XPairHandsPlayer) {
                assertTrue(Srv_HandType.XPair.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherTripple() {

        ArrayList<Card> handTableZero = new ArrayList<Card>();
        Srv_HandType ht = Srv_HandType.Tripple;

        //Case Tripple Table has 0 Cards
        for (ArrayList<Card> handPlayer : trippleHandsPlayer) {
            assertTrue(Srv_HandType.Tripple.isHigher(handTableZero, handPlayer, ht));
        }

        //Case Tripple Table has 2 Cards
        for (ArrayList<Card> handTable : trippleHandsTable) {
            for (ArrayList<Card> handPlayer : trippleHandsPlayer) {
                assertTrue(Srv_HandType.Tripple.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherStreet() {

        ArrayList<Card> handTableZero = new ArrayList<Card>();
        Srv_HandType ht = Srv_HandType.Street;

        //Case Street Table has 0 Cards
        for (ArrayList<Card> handPlayer : streetHandsPlayer) {
            assertTrue(Srv_HandType.Street.isHigher(handTableZero, handPlayer, ht));
        }

        //Case Street Table has 5 Cards or more
        for (ArrayList<Card> handTable : streetHandsTable) {
            for (ArrayList<Card> handPlayer : streetHandsPlayer) {
                assertTrue(Srv_HandType.Street.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherFullHouse() {

        ArrayList<Card> handTableZero = new ArrayList<Card>();
        Srv_HandType ht = Srv_HandType.FullHouse;

        //Case FullHouse Table has 0 Cards
        for (ArrayList<Card> handPlayer : fullHouseHandsPlayer) {
            assertTrue(Srv_HandType.FullHouse.isHigher(handTableZero, handPlayer, ht));
        }

        //Case FullHouse Table has 5 Cards
        for (ArrayList<Card> handTable : fullHouseHandsTable) {
            for (ArrayList<Card> handPlayer : fullHouseHandsPlayer) {
                assertTrue(Srv_HandType.FullHouse.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherBomb() {

        ArrayList<Card> handTableZero = new ArrayList<Card>();
        Srv_HandType ht = Srv_HandType.Bomb;

        //Case Bomb Table has 0 Cards
        for (ArrayList<Card> handPlayer : bombHandsPlayer) {
            assertTrue(Srv_HandType.Bomb.isHigher(handTableZero, handPlayer, ht));
        }

        //Case Bomb Table has Bomb also
        for (ArrayList<Card> handTable : bombHandsTable) {
            for (ArrayList<Card> handPlayer : bombHandsPlayer) {
                assertTrue(Srv_HandType.Bomb.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isOnePair in HandType.
    public void testIsSingleCard() {
        for (ArrayList<Card> hand : singleCardHandsPlayer) {
            assertTrue(Srv_HandType.isSingleCard(hand));
        }
    }

    @Test // This is the test method for isOnePair in HandType.
    public void testIsOnePair() {
        for (ArrayList<Card> hand : onePairHandsPlayer) {
                assertTrue(Srv_HandType.isOnePair(hand));
        }
    }

    @Test // This is the test method for isOnePair in HandType.
    public void testIsXPair() {
        for (ArrayList<Card> hand : XPairHandsPlayer) {
            assertTrue(Srv_HandType.isXPair(hand));
        }
    }



    @Test // This is the test method for isTripple in HandType.
    public void testIsTripple() {
        for (ArrayList<Card> hand : trippleHandsPlayer) {
            assertTrue(Srv_HandType.isTripple(hand));
        }
    }

    @Test // This is the test method for isStreet in HandType.
    public void testIsStreet() {
        for (ArrayList<Card> hand : streetHandsPlayer) {
            assertTrue(Srv_HandType.isStreet(hand));
        }
    }

    @Test // This is the test method for checking if some players has the wished card and the lastplayed card was a street with mahjong
    public void testIsMjWishStreetOnHand() {
        for (ArrayList<Card> hand : mJStreetWishHandPlayer) {
            for (ArrayList<Card> handTable : mJStreetWishHandTable) {
                assertTrue(Srv_HandType.mahJongWishStreet(hand, handTable, hand.get(3)));
            }
        }
    }

    @Test // This is the test method for isStreet in HandType.
    public void testIsFullHouse() {
        for (ArrayList<Card> hand : fullHouseHandsPlayer) {
            assertTrue(Srv_HandType.isFullHouse(hand));
        }
    }

    @Test // This is the test method for isStreet in HandType.
    public void testIsBomb() {
        for (ArrayList<Card> hand : bombHandsPlayer) {
            assertTrue(Srv_HandType.isBomb(hand));
        }
    }

    @Test // This is the test method for isStreet in HandType.
   /* public void testIsBombOnHand() {
        for (ArrayList<Card> hand : bombHandsPlayerFullHand) {
            assertTrue(Srv_HandType.isBombOnHand(hand,));
        }
    }*/

    /**
     * Make an ArrayList of hands from an array of string-arrays
     */
    private ArrayList<ArrayList<Card>> makeHands(String[][] handsIn) {
        ArrayList<ArrayList<Card>> handsOut = new ArrayList<>();
        for (String[] hand : handsIn) {
            handsOut.add(makeHand(hand));
        }
        return handsOut;
    }

    /**
     * Make a hand (ArrayList<Card>) from an array of 5 strings
     */
    private ArrayList<Card> makeHand(String[] inStrings) {
        ArrayList<Card> hand = new ArrayList<>();
        for (String in : inStrings) {
            hand.add(makeCard(in));
        }
        return hand;
    }

    private Card makeCard(String in) {
        char r = in.charAt(0);
        Rank rank = null;
        if (r <= '9') rank = Rank.values()[r-'0' - 2];
        else if (r == 'T') rank = Rank.Ten;
        else if (r == 'J') rank = Rank.Jack;
        else if (r == 'Q') rank = Rank.Queen;
        else if (r == 'K') rank = Rank.King;
        else if (r == 'A') rank = Rank.Ace;
        else if (r == 'P') rank = Rank.Phoenix;
        else if (r == 'D') rank = Rank.Dragon;
        else if (r == 'W') rank = Rank.Dog; //Dog = Wuff
        else if (r == 'M') rank = Rank.Mahjong;

        char s = in.charAt(1);
        Suit suit = null;
        if (s == 'J') suit = Suit.Jade;
        if (s == 'P') suit = Suit.Pagodas;
        if (s == 'S') suit = Suit.Stars;
        if (s == 'K') suit = Suit.Swords; //Swords = Knife
        if (s == 'E') suit = Suit.SpecialCards; //SpecialCards = Extra

        return new Card(suit, rank, 0); //For HandType-testing: value not relevant
    }

}
