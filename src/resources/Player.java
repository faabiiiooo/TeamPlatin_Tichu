package resources;

import javafx.beans.property.SimpleBooleanProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

// @author Fabio
public class Player implements Serializable {

    protected static final long serialVersionUID= 1;

    private final int PLAYER_ID;
    private static int idgenerator = 1;
    private int clientID;

    private transient final ServiceLocator serviceLocator; //do not serialize those items
    private transient Translator translator;
    private transient Logger logger;

    private final ArrayList<Card> handCards;
    private final ArrayList<Card> wonCards;

    private boolean isActive;
    private int teamID;
    private String name;
    private int score;
    private int nextPlayerID = -1;
    private boolean hasBomb;
    private boolean saidBigTichu;
    private boolean saidSmallTichu;
    private boolean hasWishedCard = false;

    public Player(){

        serviceLocator = ServiceLocator.getServiceLocator();
        translator = serviceLocator.getTranslator();
        logger = serviceLocator.getLogger();

        PLAYER_ID = idgenerator++;
        name = translator.getString("model.player") + " " + PLAYER_ID;
        handCards = new ArrayList<>();
        wonCards = new ArrayList<>();

        score = 0;
        hasBomb = false;
        saidBigTichu = false;
        saidSmallTichu=false;
        isActive = false;

        logger.info("Created player with id: " + PLAYER_ID);


    }

    public int calculateScore(){

        for(Card c : wonCards){
            score += c.getValue();
        }

        return score;

    }

    //Two players are equal if they have the same id

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return getPLAYER_ID() == player.getPLAYER_ID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPLAYER_ID());
    }

    public String toString(){
        return "Player ID: "+PLAYER_ID + " Team ID: " + teamID;
    }

    public ArrayList<Card> getHandCards() {
        return handCards;
    }

    public ArrayList<Card> getWonCards() {
        return wonCards;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNextPlayerID() {
        return nextPlayerID;
    }

    public void setNextPlayerID(int nextPlayerID) {
        this.nextPlayerID = nextPlayerID;
    }

    public boolean isHasBomb() {
        return hasBomb;
    }

    public void setHasBomb(boolean hasBomb) {
        this.hasBomb = hasBomb;
    }

    public boolean isSaidBigTichu() {
        return saidBigTichu;
    }

    public void setSaidBigTichu(boolean saidBigTichu) {
        this.saidBigTichu = saidBigTichu;
    }

    public boolean isSaidSmallTichu() {
        return saidSmallTichu;
    }

    public void setSaidSmallTichu(boolean saidSmallTichu) {
        this.saidSmallTichu = saidSmallTichu;
    }

    public int getPLAYER_ID() {
        return PLAYER_ID;
    }

    public boolean isHasWishedCard() { return hasWishedCard; }

    public void setHasWishedCard(boolean hasWishedCard) { this.hasWishedCard = hasWishedCard; }

    public void setClientID(int clientID){ this.clientID = clientID; }

    public int getClientID() {
        return clientID;
    }


}
