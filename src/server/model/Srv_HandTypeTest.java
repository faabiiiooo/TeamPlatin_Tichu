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


    // This is where we store the translated hands
    ArrayList<ArrayList<Srv_Card>> singleCardHandsTable;
    ArrayList<ArrayList<Srv_Card>> singleCardHandsPlayer;
    ArrayList<ArrayList<Srv_Card>> onePairHandsTable;
    ArrayList<ArrayList<Srv_Card>> onePairHandsPlayer;
    ArrayList<ArrayList<Srv_Card>> XPairHandsTable;
    ArrayList<ArrayList<Srv_Card>> XPairHandsPlayer;


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
