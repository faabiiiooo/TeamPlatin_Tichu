package client.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import resources.ServiceLocator;
import resources.Translator;

import java.io.File;
import java.net.MalformedURLException;

//@author Pascal
public class Clt_StartScreen extends StackPane {

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Translator translator = serviceLocator.getTranslator();

    private final TextField txtIpAddress;
    private final ToggleButton toggleDE, toggleEN;
    private final Label lblLanguage, lblIpAddress, lblTitle;
    private final Button btnNext;
    private final Stage startStage;
    private Media media;
    private MediaPlayer mp;

    private final GridPane gridPane = new GridPane();

    public Clt_StartScreen(Stage startStage) throws MalformedURLException {
        this.startStage = startStage;
        this.txtIpAddress = new TextField();


        this.btnNext = new Button(translator.getString("button.next"));

        this.lblTitle = new Label(translator.getString("label.welcome")+"\n\n");
        this.lblTitle.setId("lblTitle");
        this.lblLanguage = new Label(translator.getString("label.language"));
        this.lblIpAddress = new Label(translator.getString("label.serverIp"));



        ToggleGroup language = new ToggleGroup();
        this.toggleDE = new ToggleButton("DE");
        this.toggleEN = new ToggleButton("EN");
        toggleDE.setToggleGroup(language);
        toggleDE.setSelected(true);
        toggleEN.setToggleGroup(language);

        gridPane.add(lblTitle,0,0, 2,1);

        gridPane.add(lblIpAddress,0,1);
        gridPane.add(txtIpAddress,1,1);



        gridPane.add(btnNext,0,4);


        gridPane.add(lblLanguage,2,1);
        gridPane.add(toggleDE,3,1);
        gridPane.add(toggleEN,4,1);

        this.getChildren().add(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        StackPane.setAlignment(gridPane,Pos.CENTER);

        gridPane.setHgap(15);
        gridPane.setVgap(20);


        Scene startScene = new Scene(this);
        startScene.getStylesheets().add(getClass().getResource("StartScreen.css").toExternalForm());
        this.startStage.setScene(startScene);
        this.startStage.setResizable(false);
        this.startStage.setWidth(875);
        this.startStage.setHeight(502);

        gridPane.setId("backGif");
        this.setId("start-pane");
        this.startStage.setTitle(translator.getString("program.name"));
        this.startStage.getIcons().add(new Image("./resources/images/logo.jpg"));
        this.startStage.show();


        media = new Media(new File("..\\TeamPlatin_Tichu\\src\\resources\\Sound\\hero.mp3")
                .toURI().toURL().toExternalForm());
        mp=new MediaPlayer(media);
        mp.play();
        mp.getOnRepeat();

    }

    public TextField getTxtIpAddress() {
        return txtIpAddress;
    }

    public Button getBtnNext() {
        return btnNext;
    }

    public void close(){
        this.startStage.close();
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public ToggleButton getToggleDE() {
        return toggleDE;
    }

    public ToggleButton getToggleEN() {
        return toggleEN;
    }
}

