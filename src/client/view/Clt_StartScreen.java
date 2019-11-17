package client.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import resources.ServiceLocator;
import resources.Translator;

public class Clt_StartScreen extends GridPane {

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Translator translator = serviceLocator.getTranslator();

    private final CheckBox beServer;
    private final TextField txtIpAddress;
    private final RadioButton radioDE, radioEN;
    private final Label lblLanguage, lblBeServer, lblIpAddress, lblTitle;
    private final Button btnNext;
    private final Stage startStage;

    public Clt_StartScreen(Stage startStage){
        this.startStage = startStage;

        this.beServer = new CheckBox();
        this.txtIpAddress = new TextField();

        this.btnNext = new Button(translator.getString("button.next"));

        this.lblTitle = new Label(translator.getString("label.welcome"));
        this.lblTitle.setId("lblTitle");
        this.lblLanguage = new Label(translator.getString("label.language"));
        this.lblBeServer = new Label(translator.getString("label.beServer"));
        this.lblIpAddress = new Label(translator.getString("label.serverIp"));

        ToggleGroup language = new ToggleGroup();
        this.radioDE = new RadioButton("DE");
        this.radioEN = new RadioButton("EN");
        radioDE.setToggleGroup(language);
        radioDE.setSelected(true);
        radioEN.setToggleGroup(language);

        this.add(lblTitle,0,0);

        this.add(lblBeServer,0,1);
        this.add(lblIpAddress,0,2);

        this.add(btnNext,0,3);


        this.add(lblLanguage,2,1);
        this.add(radioDE,3,1);
        this.add(radioEN,4,1);

        Scene startScene = new Scene(this);
        startScene.getStylesheets().add(getClass().getResource("StartScreen.css").toExternalForm());
        this.startStage.setScene(startScene);
        this.startStage.setResizable(false);
        this.startStage.setTitle(translator.getString("program.name"));
        this.startStage.show();


    }
}
