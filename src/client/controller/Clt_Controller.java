package client.controller;

import client.model.Clt_Model;
import client.view.Clt_View;
import javafx.stage.Stage;

//Controllerclass for Client Part of the Game

public class Clt_Controller { //Controller is a Singleton

    private Stage primaryStage;
    private Clt_View view;
    private Clt_Model model;

    private static Clt_Controller controller;

    public static Clt_Controller getController(){
        if(controller == null){
            controller = new Clt_Controller();
        }
        return controller;
    }

    private Clt_Controller(){

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setView(Clt_View view) {
        this.view = view;
    }

    public void setModel(Clt_Model model) {
        this.model = model;
    }
}
