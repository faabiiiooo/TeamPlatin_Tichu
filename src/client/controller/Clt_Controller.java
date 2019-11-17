package client.controller;

import client.model.Clt_Model;
import client.view.Clt_View;
import javafx.stage.Stage;
import resources.Message;
import resources.ServiceLocator;

import java.util.logging.Logger;

//Controllerclass for Client Part of the Game


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
    public void processIncomingMessage(Message msgIn) { // Generates Answermessage for every Incoming Message

        switch (msgIn.getType()) {

            case "card":  //generating a Card from Message

                break;

            case "player":


                break;

            case "string":

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
