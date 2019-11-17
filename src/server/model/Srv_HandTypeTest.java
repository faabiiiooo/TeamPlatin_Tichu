package server.model;

import org.junit.Before;
import org.junit.Test;

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
            { "8S", "9K", "9S", "8K", "7K", "7S"  }, //no special card
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
            { "4S", "4K", "4P", "4J"},
            { "4S", "4K", "4P", "4J","8S", "9K", "9S", "8K", "7K", "7S"},

    };

    private static String[][] bombCardsPlayerFullHand = {
            { "5S", "5K", "5P", "5J"},
            //{ "4S", "4K", "4P", "4J","8S", "9K", "9S", "8K", "7K", "7S"},
            //{ "5K", "4K", "6K", "7K","8J", "9J", "5S", "8S", "6S", "8K", "PE"},
            { "4S", "4K", "4P", "4J","8S", "9K", "9S", "8K", "7K","PE"}, //4er + straight + special card


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
    ArrayList<ArrayList<Srv_Card>> singleCardHandsTable;
    ArrayList<ArrayList<Srv_Card>> singleCardHandsPlayer;
    ArrayList<ArrayList<Srv_Card>> onePairHandsTable;
    ArrayList<ArrayList<Srv_Card>> onePairHandsPlayer;
    ArrayList<ArrayList<Srv_Card>> XPairHandsTable;
    ArrayList<ArrayList<Srv_Card>> XPairHandsPlayer;

    ArrayList<ArrayList<Srv_Card>> trippleHandsTable;
    ArrayList<ArrayList<Srv_Card>> trippleHandsPlayer;
    ArrayList<ArrayList<Srv_Card>> streetHandsTable;
    ArrayList<ArrayList<Srv_Card>> streetHandsPlayer;
    ArrayList<ArrayList<Srv_Card>> fullHouseHandsTable;
    ArrayList<ArrayList<Srv_Card>> fullHouseHandsPlayer;
    ArrayList<ArrayList<Srv_Card>> bombHandsTable;
    ArrayList<ArrayList<Srv_Card>> bombHandsPlayer;
    ArrayList<ArrayList<Srv_Card>> bombHandsTableFullHand;
    ArrayList<ArrayList<Srv_Card>> bombHandsPlayerFullHand;
    ArrayList<ArrayList<Srv_Card>> mJStreetWishHandPlayer;
    ArrayList<ArrayList<Srv_Card>> mJStreetWishHandTable;

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

        ArrayList<Srv_Card> handTableZero = new ArrayList<Srv_Card>();
        Srv_HandType ht = Srv_HandType.SingleCard;

        //Case SingleCard Table has 0 Cards
        for (ArrayList<Srv_Card> handPlayer : singleCardHandsPlayer) {
            assertTrue(Srv_HandType.SingleCard.isHigher(handTableZero, handPlayer, ht));
        }

        //Case SingleCard Table has 1 Card
        for (ArrayList<Srv_Card> handTable : singleCardHandsTable) {
            for (ArrayList<Srv_Card> handPlayer : singleCardHandsPlayer) {
                assertTrue(Srv_HandType.SingleCard.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherOnePair() {

        ArrayList<Srv_Card> handTableZero = new ArrayList<Srv_Card>();
        Srv_HandType ht = Srv_HandType.OnePair;

        //Case OnePair Table has 0 Cards
        for (ArrayList<Srv_Card> handPlayer : onePairHandsPlayer) {
            assertTrue(Srv_HandType.OnePair.isHigher(handTableZero, handPlayer, ht));
        }

        //Case OnePair Table has 2 Cards
        for (ArrayList<Srv_Card> handTable : onePairHandsTable) {
            for (ArrayList<Srv_Card> handPlayer : onePairHandsPlayer) {
                assertTrue(Srv_HandType.OnePair.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherXPair() {

        ArrayList<Srv_Card> handTableZero = new ArrayList<Srv_Card>();
        Srv_HandType ht = Srv_HandType.XPair;

        //Case OnePair Table has 0 Cards
        for (ArrayList<Srv_Card> handPlayer : XPairHandsPlayer) {
            assertTrue(Srv_HandType.XPair.isHigher(handTableZero, handPlayer, ht));
        }

        //Case XPair cards on table
        for (ArrayList<Srv_Card> handTable : XPairHandsTable) {
            for (ArrayList<Srv_Card> handPlayer : XPairHandsPlayer) {
                assertTrue(Srv_HandType.XPair.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherTripple() {

        ArrayList<Srv_Card> handTableZero = new ArrayList<Srv_Card>();
        Srv_HandType ht = Srv_HandType.Tripple;

        //Case Tripple Table has 0 Cards
        for (ArrayList<Srv_Card> handPlayer : trippleHandsPlayer) {
            assertTrue(Srv_HandType.Tripple.isHigher(handTableZero, handPlayer, ht));
        }

        //Case Tripple Table has 2 Cards
        for (ArrayList<Srv_Card> handTable : trippleHandsTable) {
            for (ArrayList<Srv_Card> handPlayer : trippleHandsPlayer) {
                assertTrue(Srv_HandType.Tripple.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherStreet() {

        ArrayList<Srv_Card> handTableZero = new ArrayList<Srv_Card>();
        Srv_HandType ht = Srv_HandType.Street;

        //Case Street Table has 0 Cards
        for (ArrayList<Srv_Card> handPlayer : streetHandsPlayer) {
            assertTrue(Srv_HandType.Street.isHigher(handTableZero, handPlayer, ht));
        }

        //Case Street Table has 5 Cards or more
        for (ArrayList<Srv_Card> handTable : streetHandsTable) {
            for (ArrayList<Srv_Card> handPlayer : streetHandsPlayer) {
                assertTrue(Srv_HandType.Street.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherFullHouse() {

        ArrayList<Srv_Card> handTableZero = new ArrayList<Srv_Card>();
        Srv_HandType ht = Srv_HandType.FullHouse;

        //Case FullHouse Table has 0 Cards
        for (ArrayList<Srv_Card> handPlayer : fullHouseHandsPlayer) {
            assertTrue(Srv_HandType.FullHouse.isHigher(handTableZero, handPlayer, ht));
        }

        //Case FullHouse Table has 5 Cards
        for (ArrayList<Srv_Card> handTable : fullHouseHandsTable) {
            for (ArrayList<Srv_Card> handPlayer : fullHouseHandsPlayer) {
                assertTrue(Srv_HandType.FullHouse.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isHigher in HandType.
    public void testisHigherBomb() {

        ArrayList<Srv_Card> handTableZero = new ArrayList<Srv_Card>();
        Srv_HandType ht = Srv_HandType.Bomb;

        //Case Bomb Table has 0 Cards
        for (ArrayList<Srv_Card> handPlayer : bombHandsPlayer) {
            assertTrue(Srv_HandType.Bomb.isHigher(handTableZero, handPlayer, ht));
        }

        //Case Bomb Table has Bomb also
        for (ArrayList<Srv_Card> handTable : bombHandsTable) {
            for (ArrayList<Srv_Card> handPlayer : bombHandsPlayer) {
                assertTrue(Srv_HandType.Bomb.isHigher(handTable, handPlayer, ht));
            }
        }
    }

    @Test // This is the test method for isOnePair in HandType.
    public void testIsSingleCard() {
        for (ArrayList<Srv_Card> hand : singleCardHandsPlayer) {
            assertTrue(Srv_HandType.isSingleCard(hand));
        }
    }

    @Test // This is the test method for isOnePair in HandType.
    public void testIsOnePair() {
        for (ArrayList<Srv_Card> hand : onePairHandsPlayer) {
                assertTrue(Srv_HandType.isOnePair(hand));
        }
    }

    @Test // This is the test method for isOnePair in HandType.
    public void testIsXPair() {
        for (ArrayList<Srv_Card> hand : XPairHandsPlayer) {
            assertTrue(Srv_HandType.isXPair(hand));
        }
    }



    @Test // This is the test method for isTripple in HandType.
    public void testIsTripple() {
        for (ArrayList<Srv_Card> hand : trippleHandsPlayer) {
            assertTrue(Srv_HandType.isTripple(hand));
        }
    }

    @Test // This is the test method for isStreet in HandType.
    public void testIsStreet() {
        for (ArrayList<Srv_Card> hand : streetHandsPlayer) {
            assertTrue(Srv_HandType.isStreet(hand));
        }
    }

    @Test // This is the test method for checking if some players has the wished card and the lastplayed card was a street with mahjong
    public void testIsMjWishStreetOnHand() {
        for (ArrayList<Srv_Card> hand : mJStreetWishHandPlayer) {
            for (ArrayList<Srv_Card> handTable : mJStreetWishHandTable) {
                assertTrue(Srv_HandType.mahJongWishStreet(hand, handTable, hand.get(3)));
            }
        }
    }

    @Test // This is the test method for isStreet in HandType.
    public void testIsFullHouse() {
        for (ArrayList<Srv_Card> hand : fullHouseHandsPlayer) {
            assertTrue(Srv_HandType.isFullHouse(hand));
        }
    }

    @Test // This is the test method for isStreet in HandType.
    public void testIsBomb() {
        for (ArrayList<Srv_Card> hand : bombHandsPlayer) {
            assertTrue(Srv_HandType.isBomb(hand));
        }
    }

    @Test // This is the test method for isStreet in HandType.
    public void testIsBombOnHand() {
        for (ArrayList<Srv_Card> hand : bombHandsPlayerFullHand) {
            assertTrue(Srv_HandType.isBombOnHand(hand));
        }
    }

    /**
     * Make an ArrayList of hands from an array of string-arrays
     */
    private ArrayList<ArrayList<Srv_Card>> makeHands(String[][] handsIn) {
        ArrayList<ArrayList<Srv_Card>> handsOut = new ArrayList<>();
        for (String[] hand : handsIn) {
            handsOut.add(makeHand(hand));
        }
        return handsOut;
    }

    /**
     * Make a hand (ArrayList<Card>) from an array of 5 strings
     */
    private ArrayList<Srv_Card> makeHand(String[] inStrings) {
        ArrayList<Srv_Card> hand = new ArrayList<>();
        for (String in : inStrings) {
            hand.add(makeCard(in));
        }
        return hand;
    }

    private Srv_Card makeCard(String in) {
        char r = in.charAt(0);
        Srv_Rank rank = null;
        if (r <= '9') rank = Srv_Rank.values()[r-'0' - 2];
        else if (r == 'T') rank = Srv_Rank.Ten;
        else if (r == 'J') rank = Srv_Rank.Jack;
        else if (r == 'Q') rank = Srv_Rank.Queen;
        else if (r == 'K') rank = Srv_Rank.King;
        else if (r == 'A') rank = Srv_Rank.Ace;
        else if (r == 'P') rank = Srv_Rank.Phoenix;
        else if (r == 'D') rank = Srv_Rank.Dragon;
        else if (r == 'W') rank = Srv_Rank.Dog; //Dog = Wuff
        else if (r == 'M') rank = Srv_Rank.Mahjong;

        char s = in.charAt(1);
        Srv_Suit suit = null;
        if (s == 'J') suit = Srv_Suit.Jade;
        if (s == 'P') suit = Srv_Suit.Pagodas;
        if (s == 'S') suit = Srv_Suit.Stars;
        if (s == 'K') suit = Srv_Suit.Swords; //Swords = Knife
        if (s == 'E') suit = Srv_Suit.SpecialCards; //SpecialCards = Extra

        return new Srv_Card(suit, rank, 0); //For HandType-testing: value not relevant
    }

}
