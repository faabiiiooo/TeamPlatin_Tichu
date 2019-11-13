package client;

import client.controller.Clt_Controller;
import client.model.Clt_Model;
import client.view.Clt_View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Tichu_Clt extends Application {
    private static Tichu_Clt clt; //singleton

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        if(clt == null){
            clt = this;
        } else {
            Platform.exit();
        }
    }

    // @author Fabio
    public void start(Stage primaryStage){

        //Creating MVC for main Client Application
        Clt_Model model = new Clt_Model();
        Clt_View view = new Clt_View(primaryStage, model);
        Clt_Controller controller = Clt_Controller.getController();
        controller.setPrimaryStage(primaryStage);
        controller.setView(view);
        controller.setModel(model);

    }
}
