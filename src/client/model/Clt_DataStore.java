package client.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import resources.Message;
import resources.Card;
import resources.Player;

import java.util.ArrayList;

//@author Fabio
public class Clt_DataStore {

    private static Clt_DataStore dataStore;

    private final ObservableList<Card> handCards = FXCollections.observableArrayList();
    private final ArrayList<Card> cardsToSend = new ArrayList<>();
    private final ArrayList<Message> waitingForResponse = new ArrayList<>();

    private boolean finish = false;

    private Player playerTop;
    private Player playerRight;
    private Player playerLeft;


    private final SimpleIntegerProperty cardsPlayerTop = new SimpleIntegerProperty();
    private final SimpleIntegerProperty cardsPlayerRight = new SimpleIntegerProperty();
    private final SimpleIntegerProperty cardsPlayerLeft = new SimpleIntegerProperty();

    private final SimpleStringProperty teamScoreT1 = new SimpleStringProperty("Team 1: 0");
    private final SimpleStringProperty teamScoreT2 = new SimpleStringProperty("Team 2: 0");

    private SimpleObjectProperty wishedCard = new SimpleObjectProperty();


    private int nextPlayerID;


    private final ObservableList<Card> tableCards = FXCollections.observableArrayList();
    private SimpleBooleanProperty wantsCardWish = new SimpleBooleanProperty(false);


    private final SimpleBooleanProperty isActive = new SimpleBooleanProperty(false);

    private SimpleBooleanProperty hasBomb = new SimpleBooleanProperty(false);

    public static Clt_DataStore getDataStore(){
        if(dataStore == null){
            dataStore = new Clt_DataStore();
        }
        return dataStore;
    }

    private Clt_DataStore(){ //Datastore is a singleton because it should only exist one datastore per client.

    }

    public void addCardsToSend(Card card){ //gets Called when a CardLabel gets klicked
        cardsToSend.add(card);
    }

    public ArrayList<Card> getCardsToSend(){ //get the cards to send them
        return cardsToSend;
    }

    public void removeCardsFromHand(ArrayList<Card> sentCards){ //remove successfully sent cards
        handCards.removeAll(sentCards);
    }

    public void addMessageToQueue(Message sentMsg){
        waitingForResponse.add(sentMsg);
    }

    public void removeMessageFromQueue(String msgId){ //remove Message based on messageID.
        ArrayList<Message> tempQueue = (ArrayList<Message>) waitingForResponse.clone();
        for (int i = 0; i < tempQueue.size(); i++){
            if(tempQueue.get(i).getMessageID().equals(msgId)){
                waitingForResponse.remove(i);
            }
        }
    }

    public void setCardAmountProperties(){
            cardsPlayerLeft.set(playerLeft.getHandCards().size());
            cardsPlayerRight.set(playerRight.getHandCards().size());
            cardsPlayerTop.set(playerTop.getHandCards().size());
    }

    public ArrayList<Message> getWaitingForResponse(){return waitingForResponse;}

    public boolean queueContains(String messageID){
        boolean containsMessage = false;
        for(Message m : waitingForResponse){
            if (m.getMessageID().equals(messageID)){
                containsMessage = true;
            }
        }
        return containsMessage;
    }

    public ObservableList<Card> getHandCards() {
        return handCards;
    }

    public ObservableList<Card> getTableCards() {
        return tableCards;
    }

    public boolean isIsActive() {
        return isActive.get();
    }

    public SimpleBooleanProperty isActiveProperty() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    public Player getPlayerTop() {
        return playerTop;
    }

    public void setPlayerTop(Player playerTop) {
        this.playerTop = playerTop;
    }

    public Player getPlayerRight() {
        return playerRight;
    }

    public void setPlayerRight(Player playerRight) {
        this.playerRight = playerRight;
    }

    public Player getPlayerLeft() {
        return playerLeft;
    }

    public void setPlayerLeft(Player playerLeft) {
        this.playerLeft = playerLeft;
    }

    public int getNextPlayerID() {
        return nextPlayerID;
    }

    public void setNextPlayerID(int nextPlayerID) {
        this.nextPlayerID = nextPlayerID;
    }

    public boolean isHasBomb() { return hasBomb.get(); }

    public SimpleBooleanProperty hasBombProperty() { return hasBomb; }

    public void setHasBomb(boolean hasBomb) { this.hasBomb.set(hasBomb); }

    public int getCardsPlayerTop() {
        return cardsPlayerTop.get();
    }

    public SimpleIntegerProperty cardsPlayerTopProperty() {
        return cardsPlayerTop;
    }

    public int getCardsPlayerRight() {
        return cardsPlayerRight.get();
    }

    public SimpleIntegerProperty cardsPlayerRightProperty() {
        return cardsPlayerRight;
    }

    public int getCardsPlayerLeft() {
        return cardsPlayerLeft.get();
    }

    public SimpleIntegerProperty cardsPlayerLeftProperty() {
        return cardsPlayerLeft;
    }

    public SimpleBooleanProperty isWantsCardWish() { return wantsCardWish; }

    public void setWantsCardWish(boolean wishedCardIsSet) { this.wantsCardWish.set(wishedCardIsSet); }

    public String getTeamScoreT1() {
        return teamScoreT1.get();
    }

    public SimpleStringProperty teamScoreT1Property() {
        return teamScoreT1;
    }

    public void setTeamScoreT1(String teamScore) {
        this.teamScoreT1.set("Team 1: " + teamScore);
    }

    public String getTeamScoreT2() {
        return teamScoreT2.get();
    }

    public SimpleStringProperty teamScoreT2Property() {
        return teamScoreT2;
    }

    public void setTeamScoreT2(String teamScoreT2) {
        this.teamScoreT2.set("Team 2: "+teamScoreT2);
    }

    public Object getWishedCard() { return wishedCard.get(); }

    public SimpleObjectProperty wishedCardProperty() { return wishedCard; }

    public void setWishedCard(Object wishedCard) {
        this.wishedCard.set(wishedCard);
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
