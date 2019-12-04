package client.controller;

import client.model.Clt_DataStore;
import client.model.Clt_Model;
import client.view.CardView;
import client.view.Clt_View;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import resources.*;

import javax.tools.Tool;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.logging.Logger;

//Controller for Client Part of the Game


public class Clt_Controller { //Controller is a Singleton

    private Stage primaryStage;
    private Clt_View view;
    private Clt_Model model;
    private ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private Logger logger = serviceLocator.getLogger();
    private final Translator translator = serviceLocator.getTranslator();
    private final Clt_DataStore dataStore = Clt_DataStore.getDataStore();



    public Clt_Controller(Stage primaryStage, Clt_View view, Clt_Model model){
        this.primaryStage = primaryStage;
        this.view = view;
        this.model = model;
        serviceLocator.setCltController(this);
        this.setStartScreenOnAction();


    }

    private void setStartScreenOnAction(){
        view.getStartScreen().getBtnNext().setOnAction(e -> startScreenBtnNext());
        view.getStartScreen().getBeServer().setOnAction(e -> {
            if (view.getStartScreen().getBeServer().isSelected()){
              view.getStartScreen().getTxtIpAddress().setText("127.0.0.1");
              view.getStartScreen().getTxtIpAddress().setDisable(true);
            } else {
                view.getStartScreen().getTxtIpAddress().setText("");
                view.getStartScreen().getTxtIpAddress().setDisable(false);

            }
        });

    }

    private void setTableViewOnAction() {
        view.getTableView().getControls().getPlayButton().setOnAction(e -> processPlayButton());
        view.getTableView().getControls().getCallTichuButton().setOnAction(e -> processTichuButton());
        view.getTableView().getControls().getPassButton().setOnAction(e -> processSkipButton());
        model.getDataStore().getHandCards().addListener((ListChangeListener) c -> handCardChanged());
        model.getDataStore().getTableCards().addListener((ListChangeListener<? super Card>) c -> tableCardChanged());
        model.getDataStore().isActiveProperty().addListener((observable, oldValue, newValue) -> activeStatusChanged(oldValue,newValue));
        model.getDataStore().getHandCards().addListener((ListChangeListener<? super Card>) c -> handCardChanged());
        view.getTableView().getRivalLeft().getCardAmountText().textProperty().bind(dataStore.cardsPlayerLeftProperty().asString());
        view.getTableView().getRivalRight().getCardAmountText().textProperty().bind(dataStore.cardsPlayerRightProperty().asString());
        view.getTableView().getRivalTop().getCardAmountText().textProperty().bind(dataStore.cardsPlayerTopProperty().asString());

        model.getDataStore().hasBombProperty().addListener( (observable, oldValue, newValue) -> updateBombButton(newValue));
        view.getTableView().getControls().getBombButton().setOnAction(e -> processBombButton());
        model.getDataStore().isWantsCardWish().addListener( (observable, oldValue, newValue) -> wishedCardfromMahjong(newValue));

    }
    //@author Thomas Activate the bomb button if the player has a bomb on his hand
    private void updateBombButton(Boolean newValue) {
        if(newValue){
                logger.info("Reenable the bomb button because the player has a bomb");
            Platform.runLater(() -> {
                view.getTableView().getControls().getBombButton().setDisable(false);
            });
        }else{
            if(!newValue) {
                logger.info("Player has no Bomb - disable the button");
                Platform.runLater(() -> {
                    view.getTableView().getControls().getBombButton().setDisable(true);
                });

            }
        }
    }




    //@author Sandro
    private void processSkipButton() {
        logger.info("Clt_processSkipButton");

        boolean successful = false;
        successful = model.sendMessage(model.createMessage("string","skip")); //send skip-string to server and get answer of server

        if(successful){ //does Server accept the skip?
            logger.info("Skip-String sent to Server.");
        } else { //else give feedback to the user
            logger.info("Skipping not possible");
        }
    }

    //@author Pascal
    private void processTichuButton() {

        logger.info("processTichuButton");

        boolean successful = false;
        successful = model.sendMessage(model.createMessage("string", "tichu"));//Send a tichu String to Server
        if (successful) {
            logger.info("Tichu-String sent to Server");
            Platform.runLater(()->view.getTableView().getTichuLabel().setText(translator.getString("player.said.tichu")));//Show a message in the Gui that the player has announced a Tichu
            Platform.runLater(() ->view.getTableView().getControls().getCallTichuButton().setDisable(true));
        } else {
            logger.info("saying tichu is not possible");
        }




    }

//@author thomas
    private void processBombButton(){
        logger.info("Clt_processBombButton");
        boolean successfulActiveStatusSent = false;
        //player who pressed bomb button needs to the the active player
        successfulActiveStatusSent = model.sendMessage(model.createMessage("string","bombActiveChange" ));
        if(successfulActiveStatusSent){
            logger.info("Player who pressed bomb button is now active Player");
            view.getTableView().getControls().getPassButton().setDisable(true);
        }else{
            logger.info("Server can't set the player with the bomb to active player");
        }

    }



    //@author Fabio
    private void startScreenBtnNext(){

        if(view.getStartScreen().getBeServer().isSelected()){ //if user wants to be server, start server and client
            model.startServer();
            model.startClient("127.0.0.1");


        } else { //else just start client with connection to entered ip address
            String serverIp = view.getStartScreen().getTxtIpAddress().getText();
            model.startClient(serverIp);

        }

        Stage mainStage = new Stage(); //start client view
        this.primaryStage = mainStage;
        view.setPrimaryStage(mainStage);
        view.getStartScreen().close();
        view.getStartScreen().getMp().stop();// Stops the sound
        view.startTableView();
       // view.startDcView();
        this.setTableViewOnAction();

    }



    //@author Fabio
    synchronized public void processPlayButton(){
        boolean successful = false;
        Platform.runLater(()->view.getTableView().getTichuLabel().setText(""));

        ArrayList<Card> cardsToSend = Clt_DataStore.getDataStore().getCardsToSend(); //get selected cards by player
        Collections.sort(cardsToSend);
        if(cardsToSend.size() > 0 ){ // if he has cards selected
          successful = model.sendMessage(model.createMessage("card/playCard",cardsToSend.toArray())); //send cards to server and get answer of server
           if(successful){ //does Server accept the cards? if yes, remove the cards from hand
               logger.info("Cards sent to Server.");
               dataStore.getHandCards().removeAll(cardsToSend);
               cardsToSend.clear(); //remove the played cards out of the list

           } else { //else give feedback to the user
               logger.info(this.toString()+"Cards declined by server. player has to replay.");
               this.displayWrongCardsStatus();
           }
        }


    }

    private void wishedCardfromMahjong(boolean newValue){
        logger.info(this.toString()+ "I get Info to show Wish view");
        if(newValue) {
            Platform.runLater(() -> view.startWishView());
            Platform.runLater(() -> view.getCardWishView().getWishButtonGroup().selectedToggleProperty().addListener((ov, toggle, new_toggle) -> {
                if (view.getCardWishView().getWishButtonGroup().getSelectedToggle() != null)
                    for (ToggleButton tb : view.getCardWishView().getCardButtons()) {
                        if (tb == new_toggle) {
                            createWishedCard(tb.getText());
                        }

                    }
                Platform.runLater(() -> view.getCardWishView().getWishStage().close());

            }));
        }

    }


    //@author Fabio
    private void displayWrongCardsStatus(){

    }
    //@author Fabio
    public void handCardChanged(){ //method gets called when client retrieves or playes card
        Platform.runLater(() -> {
            view.getTableView().getPlayerView().clear();
        });
        logger.info("Cleared HandCards");

        for(Card c : model.getDataStore().getHandCards()){
            Platform.runLater(() -> {
                CardView cv = new CardView(c);
                cv.setOnMouseClicked(e -> processCardClicked(e));
                view.getTableView().getPlayerView().addCards(cv);
                //updateCardAmountView();
            });
        }

    }
    //@auhtor Fabio
    private void tableCardChanged(){
        Platform.runLater(() -> {
            view.getTableView().getTableCards().clear();
        });
        logger.info("cleared TableCards");

        for(Card c : model.getDataStore().getTableCards()){
            Platform.runLater(() -> {
                CardView cv = new CardView(c);
                view.getTableView().getTableCards().addCards(cv);
            });
        }
    }

    //@author Fabio
    private void activeStatusChanged(boolean oldValue, boolean newValue){

        if(oldValue){
            Platform.runLater(()-> {
            disableButtons();
            });
            logger.info("disabling button from not active players");
        } else {
            if(!oldValue){
                Platform.runLater(()-> {
               enableButtons();
             });
            }
        }

    }


    private void disableButtons(){
            view.getTableView().getControls().getPlayButton().setDisable(true);
            view.getTableView().getControls().getPassButton().setDisable(true);
    }

    private void enableButtons(){
        view.getTableView().getControls().getPlayButton().setDisable(false);
        view.getTableView().getControls().getPassButton().setDisable(false);

    }

    //@author Sandro
    private void changeRiceLabel() {
        if (dataStore.getPlayerTop().isActive()) {
            view.getTableView().getRivalTop().getRiceLabel().setVisible(true);
            view.getTableView().getRivalLeft().getRiceLabel().setVisible(false);
            view.getTableView().getRivalRight().getRiceLabel().setVisible(false);
        }
        if (dataStore.getPlayerLeft().isActive()) {
            view.getTableView().getRivalTop().getRiceLabel().setVisible(false);
            view.getTableView().getRivalLeft().getRiceLabel().setVisible(true);
            view.getTableView().getRivalRight().getRiceLabel().setVisible(false);
        }
        if (dataStore.getPlayerRight().isActive()) {
            view.getTableView().getRivalTop().getRiceLabel().setVisible(false);
            view.getTableView().getRivalLeft().getRiceLabel().setVisible(false);
            view.getTableView().getRivalRight().getRiceLabel().setVisible(true);
        }
        if (dataStore.getPlayerTop().isActive() == false && dataStore.getPlayerLeft().isActive() == false && dataStore.getPlayerRight().isActive() == false) {
            view.getTableView().getRivalTop().getRiceLabel().setVisible(false);
            view.getTableView().getRivalLeft().getRiceLabel().setVisible(false);
            view.getTableView().getRivalRight().getRiceLabel().setVisible(false);
        }
    }

    //@author Fabio
    private void processCardClicked(MouseEvent e){ //sets a border around the card, adds it to send queue
        logger.info("clicked on card");
        CardView source = (CardView) e.getSource();
        if(!source.isSelected()){
            source.getStyleClass().add("card-selected");
            Card c = source.getCard();
            model.getDataStore().addCardsToSend(c);
            source.setSelected(true);
        } else {
            if(source.isSelected()){
                source.getStyleClass().remove("card-selected");
                Card c = source.getCard();
                model.getDataStore().getCardsToSend().remove(c);
                source.setSelected(false);
            }
        }
    }

    private void createWishedCard(String rank){
        boolean successful = false;
        logger.info("going to create new card: "+rank);
        int value = 0;
        Suit suit = Suit.Swords;
        Rank wishRank = null;
        switch (rank) {
            case "2":
                wishRank = Rank.Two;
                break;
            case "3":
                wishRank = Rank.Three;
                break;
            case "4":
                wishRank = Rank.Four;
                break;
            case "5":
                wishRank = Rank.Five;
                break;
            case "6":
                wishRank = Rank.Six;
                break;
            case "7":
                wishRank = Rank.Seven;
                break;
            case "8":
                wishRank = Rank.Eight;
                break;
            case "9":
                wishRank = Rank.Nine;
                break;
            case "10":
                wishRank = Rank.Ten;
                break;
            case "J":
                wishRank = Rank.Jack;
                break;
            case "Q":
                wishRank = Rank.Queen;
                break;
            case "K":
                wishRank = Rank.King;
                break;
            case "A":
                wishRank = Rank.Ace;
                break;
            case "N":
                wishRank = null;
                break;
        }

        Card wishedCard = new Card(suit, wishRank, value);

        successful = model.sendMessage(model.createMessage("card/wishCard",wishedCard)); //send cards to server and get answer of server
        if(successful){
            logger.info(wishedCard+" wished card created");
        }else{
            logger.info("Cant send the wished card");
        }
    }


    //@author Fabio
    public void processIncomingMessage(Message msgIn) { // Generates Answermessage for every Incoming Message

        switch (msgIn.getType()) {

            case "card/dealCards":  //getting the playerHand from server
                ArrayList<Card> handCards = new ArrayList<>();
                for(Object o : msgIn.getObjects()){
                    handCards.add((Card) o);
                }
                Platform.runLater(() -> {
                    dataStore.getHandCards().clear();
                    dataStore.getHandCards().addAll(handCards);
                    Collections.sort(dataStore.getHandCards());
                });
                logger.info("Added Cards to hand");
                logger.info("HandCards: " + dataStore.getHandCards().toString());
                break;

            case "card/tableCards":
                ArrayList<Card> tableCards = new ArrayList<>();
                for(Object o : msgIn.getObjects()){
                    tableCards.add((Card) o);
                }
                dataStore.getTableCards().clear();
                dataStore.getTableCards().addAll(tableCards);
                logger.info("Added TableCards to table");
                break;

            case "boolean/isActive": //client gets information if he is the active player or not
                boolean isActive = (boolean) msgIn.getObjects().get(0);
                Platform.runLater(() -> {
                    dataStore.isActiveProperty().set(isActive);
                    changeRiceLabel();
                });
                logger.info("Clt_Controller: Player ActiveStatus: "+ dataStore.isActiveProperty().get());
                break;

            case "boolean/hasBomb":
                boolean hasBomb = (boolean) msgIn.getObjects().get(0);
                dataStore.hasBombProperty().set(hasBomb);
                logger.info("HasBomb set to: "+hasBomb );
                break;

            case "player":
                ArrayList<Player> otherPlayers = new ArrayList<>();
                for(Object o : msgIn.getObjects()){
                    otherPlayers.add((Player) o);
                }
                for(Player p : otherPlayers){
                    logger.info("oP"+p.getPLAYER_ID() +" c:"+p.getHandCards().size());

                }
                logger.info(otherPlayers.size()+"");


                for(int i = 0; i < otherPlayers.size(); i++){
                    logger.info("P"+otherPlayers.get(i).getPLAYER_ID()+" c:"+otherPlayers.get(i).getHandCards().size());
                    if(dataStore.getNextPlayerID() == otherPlayers.get(i).getPLAYER_ID()){
                        dataStore.setPlayerRight(otherPlayers.get(i));
                    }
                }
                otherPlayers.remove(dataStore.getPlayerRight());
                for(Player p : otherPlayers){
                    if(p.getTeamID() == dataStore.getPlayerRight().getTeamID()){
                        dataStore.setPlayerLeft(p);
                    }
                }
                otherPlayers.remove(dataStore.getPlayerLeft());
                if(otherPlayers.size() == 1){
                    dataStore.setPlayerTop(otherPlayers.get(0));
                }

                dataStore.setCardAmountProperties();

                logger.info("Added Players to datastore");
                break;

            case "string/nextPlayer":
                int nextPlayerID = (int) msgIn.getObjects().get(0);
                dataStore.setNextPlayerID(nextPlayerID);
                break;
            case "string/wishView":
                model.getDataStore().isWantsCardWish().set(true);
                //wishedCardfromMahjong();


            case "string":
                logger.info("Recieved a String, going to evaluate it.");

                break;

            case "response/string":
                logger.info("Recieved a responseString, going to evaluate it.");
                MessageResponse responseIn = (MessageResponse) msgIn; //Cast it to a Response
                String info = (String) responseIn.getObjects().get(0); //Responses always contain only 1 string
                logger.info("String info from Response: " + info);
                if(Clt_DataStore.getDataStore().queueContains(responseIn.getParentMessageID())) { //if Data Store contains parent message
                    logger.info("Datastore contains parent message");
                    for (Message m : Clt_DataStore.getDataStore().getWaitingForResponse()) {
                        if (m.getMessageID().equals(responseIn.getParentMessageID())) { //searching the correct message
                            logger.info("searching correct message");
                            if (info.equals("ok")) { //if string is ok, and set status of message
                                logger.info("Answer is ok");
                                m.setMessageStatus(MessageStats.accepted);
                            } else {
                                logger.info("Answer is n-ok");
                                m.setMessageStatus(MessageStats.declined);
                            }
                        }
                    }
                }

                break;

            case "connection-lost": //stop game. A client got disconnected.
                logger.warning("Client is going to stop because of connection loss");


                break;

        }

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
