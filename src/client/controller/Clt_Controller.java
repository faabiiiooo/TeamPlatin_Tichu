package client.controller;

import client.model.Clt_Model;
import client.view.Clt_View;
import javafx.stage.Stage;

//Controllerclass for Client Part of the Game

public class Clt_Controller {

    private Stage primaryStage;
    private Clt_View view;
    private Clt_Model model;

    public Clt_Controller(Stage primaryStage, Clt_View view, Clt_Model model){
        this.primaryStage = primaryStage;
        this.view = view;
        this.model = model;

    }
}
