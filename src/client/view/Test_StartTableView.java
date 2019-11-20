package client.view;

import javafx.application.Application;
import javafx.stage.Stage;
import resources.ServiceLocator;
import resources.Translator;

public class Test_StartTableView extends Application {

    private ServiceLocator serviceLocator;

    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage){
        initialize();
        Clt_TableView tblView = new Clt_TableView(primaryStage);

    }

    public void initialize() {

        //create ServiceLocator
        serviceLocator = ServiceLocator.getServiceLocator();

        //Initialize Resources in ServiceLocator
        serviceLocator.setTranslator(new Translator("de_CH"));


    }


}
