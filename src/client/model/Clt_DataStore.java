package client.model;

import javafx.beans.property.SimpleBooleanProperty;
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

    private Player playerTop;
    private Player playerRight;
    private Player playerLeft;

    private int nextPlayerID;

    private final ObservableList<Card> tableCards = FXCollections.observableArrayList();


    private final SimpleBooleanProperty isActive = new SimpleBooleanProperty(false);

    private SimpleBooleanProperty hasBomb = new SimpleBooleanProperty();

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
}
