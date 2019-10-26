package client.view;

import client.model.Clt_Model;
import javafx.stage.Stage;

//Basic Class to start build the GUI

public class Clt_View {

    private Stage primaryStage;
    private Clt_Model model;

    public Clt_View(Stage primaryStage, Clt_Model model){
        this.primaryStage = primaryStage;
        this.model = model;
    }


    //Start displaying View
    public void start(){
        this.primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Clt_Model getModel() {
        return model;
    }
}
