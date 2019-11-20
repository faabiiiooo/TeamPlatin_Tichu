package client.view;

import client.model.Clt_Model;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

//Basic Class to start build the GUI

public class Clt_View {

    private Stage primaryStage;
    private Clt_Model model;

    private final Clt_StartScreen startScreen;
    private Clt_TableView tableView;



    public Clt_View(Stage primaryStage, Clt_Model model){
        this.primaryStage = primaryStage;
        this.model = model;

        startScreen = new Clt_StartScreen(primaryStage);

    }

    public void startTableView(){
        tableView = new Clt_TableView(primaryStage);
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

    public Clt_StartScreen getStartScreen() { return startScreen; }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
