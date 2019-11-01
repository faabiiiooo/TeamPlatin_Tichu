package server.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class Srv_HandTypeTest {

    private static String[][] highCards = {
            { "2S", "9C", "3H", "5D", "7H" },
    };

    // This is where we store the translated hands
    ArrayList<ArrayList<Srv_Card>> highCardHands;

    /**
     * The makeHands method is called before each test method,
     * and prepares the translated hands. We recreate these for
     * each test method, in case the test method damages the data.

    @Before
    public void makeHands() {
        highCardHands = makeHands(highCards);
    }  */

    @Test // This is the test method for the isOnePair in HandType.
    public void testIsOnePair() {
    }
}
