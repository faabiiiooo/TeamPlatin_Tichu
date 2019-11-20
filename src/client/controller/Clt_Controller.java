package client.controller;

import client.model.Clt_DataStore;
import client.model.Clt_Model;
import client.view.Clt_View;
import javafx.stage.Stage;
import resources.Message;
import resources.MessageResponse;
import resources.MessageStats;
import resources.ServiceLocator;
import resources.Card;

import java.util.ArrayList;
import java.util.logging.Logger;

//Controller for Client Part of the Game


public class Clt_Controller { //Controller is a Singleton

    private Stage primaryStage;
    private Clt_View view;
    private Clt_Model model;
    private ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private Logger logger = serviceLocator.getLogger();


    public Clt_Controller(Stage primaryStage, Clt_View view, Clt_Model model){
        this.primaryStage = primaryStage;
        this.view = view;
        this.model = model;
        serviceLocator.setCltController(this);
        this.setViewOnAction();

    }

    private void setViewOnAction(){
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
        view.getTableView().getControls().getPlayButton().setOnAction(e -> processPlayButton());
    }

    //@author Fabio
    private void startScreenBtnNext(){

        if(view.getStartScreen().getBeServer().isSelected()){ //if user wants to be server, start server and client
            model.startServer();
            model.startClient("127.0.0.1");

            Stage mainStage = new Stage(); //start client view
            this.primaryStage = mainStage;
            view.setPrimaryStage(mainStage);

        } else { //else just start client with connection to entered ip address
            String serverIp = view.getStartScreen().getTxtIpAddress().getText();
            model.startClient(serverIp);

            Stage mainStage = new Stage(); //start client view
            this.primaryStage = mainStage;
            view.setPrimaryStage(mainStage);

            view.startTableView();

        }

    }

    //@author Fabio
    public void processPlayButton(){

        boolean successful = false;

        ArrayList<Card> cardsToSend = Clt_DataStore.getDataStore().getCardsToSend(); //get selected cards by player
        if(cardsToSend.size() > 0){ // if he has cards selected
           successful = model.sendMessage(model.createMessage("card",cardsToSend.toArray())); //send cards to server and get answer of server
           if(successful){ //does Server accept the cards? if yes, remove the cards from hand
               logger.info("Cards sent to Server.");
               Clt_DataStore.getDataStore().removeCardsFromHand(cardsToSend);
           } else { //else give feedback to the user
               logger.info("Cards declined by server. player has to replay.");
               this.displayWrongCardsStatus();
           }
        } else {
            logger.info("No cards selected");
        }
    }

    //@author Fabio
    private void displayWrongCardsStatus(){

    }

    //@author Fabio
    public void processIncomingMessage(Message msgIn) { // Generates Answermessage for every Incoming Message

        switch (msgIn.getType()) {

            case "card":  //generating a Card from Message

                break;

            case "player":


                break;

            case "string":
                logger.info("Recieved a String, going to evaluate it.");

                break;

            case "response/string":
                logger.info("Recieved a responseString, going to evaluate it.");
                MessageResponse responseIn = (MessageResponse) msgIn; //Cast it to a Response
                String info = (String) responseIn.getObjects().get(0); //Responses always contain only 1 string
                if(Clt_DataStore.getDataStore().queueContains(responseIn.getParentMessageID())) { //if Data Store contains parent message
                    for (Message m : Clt_DataStore.getDataStore().getWaitingForResponse()) {
                        if (m.getMessageID().equals(responseIn.getParentMessageID())) { //searching the correct message
                            if (info.equals("ok")) { //if string is ok, and set status of message
                                m.setMessageStatus(MessageStats.accepted);
                            } else {
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
