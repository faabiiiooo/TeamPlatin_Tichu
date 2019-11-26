package client.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import resources.Message;
import resources.Card;

import java.util.ArrayList;

//@author Fabio
public class Clt_DataStore {

    private static Clt_DataStore dataStore;

    private final ObservableList<Card> handCards = FXCollections.observableArrayList();
    private final ArrayList<Card> cardsToSend = new ArrayList<>();
    private final ArrayList<Message> waitingForResponse = new ArrayList<>();

    private final ObservableList<Card> tableCards = FXCollections.observableArrayList();
    private SimpleIntegerProperty amountOfCards = new SimpleIntegerProperty(handCards.size());

    private final SimpleBooleanProperty isActive = new SimpleBooleanProperty(false);

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

    public int getAmountOfCards() { return amountOfCards.get(); }

    public SimpleIntegerProperty amountOfCardsProperty() { return amountOfCards; }

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
}
