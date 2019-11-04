package server.model;

import resources.ServiceLocator;
import resources.Translator;

import java.util.ArrayList;
import java.util.logging.Logger;

// @author Fabio
public class Srv_Player {

    private final int PLAYER_ID;
    private static int idgenerator = -1;

    private final ServiceLocator serviceLocator;
    private Translator translator;
    private Logger logger;

    private final ArrayList<Card> handCards;
    private final ArrayList<Card> wonCards;

    private boolean isActive;
    private int teamID;
    private String name;
    private int score;
    private int nextPlayerID;
    private boolean hasBomb;
    private boolean saidTichu;

    public Srv_Player(){

        serviceLocator = ServiceLocator.getServiceLocator();
        translator = serviceLocator.getTranslator();
        logger = serviceLocator.getLogger();

        PLAYER_ID = idgenerator++;
        name = translator.getString("model.player") +PLAYER_ID;
        handCards = new ArrayList<>();
        wonCards = new ArrayList<>();

        score = 0;
        hasBomb = false;
        saidTichu = false;
        isActive = false;

        logger.info("Created player with id: " + PLAYER_ID);


    }

    public void calculateScore(){

        for(Card c : wonCards){
            score += c.getValue();
        }

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

    public boolean isSaidTichu() {
        return saidTichu;
    }

    public void setSaidTichu(boolean saidTichu) {
        this.saidTichu = saidTichu;
    }
}
