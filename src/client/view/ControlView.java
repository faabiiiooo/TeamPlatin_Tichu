package client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import resources.ServiceLocator;
import resources.Translator;

//@author thomas
public class ControlView extends HBox {

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Translator translator = serviceLocator.getTranslator();

    private Button playButton, passButton, bombButton, callTichuButton;
    private ProgressIndicator countDown = new ProgressIndicator(0);





    public ControlView(){
        this.playButton = new Button(translator.getString("button.playCards"));
        this.passButton = new Button(translator.getString("button.pass"));
        this.bombButton = new Button(translator.getString("button.bomb"));
        this.callTichuButton = new Button(translator.getString("button.callTichu"));


        playButton.getStyleClass().add("controlButtons");
        passButton.getStyleClass().add("controlButtons");
        bombButton.getStyleClass().add("controlButtons");
        callTichuButton.getStyleClass().add("controlButtons");

        Region region1 = new Region();
        this.setHgrow(region1, Priority.ALWAYS);
        countDown.setPrefHeight(65);
        countDown.setPrefWidth(65);

        this.setId("bottom");
        this.getChildren().addAll(playButton,passButton,bombButton,callTichuButton, region1, countDown );
        //Bomb button is always disabled at the beginning, only able it when player got a bomb
        this.bombButton.setDisable(true);
        //disable buttons until player is active
        this.playButton.setDisable(true);
        this.passButton.setDisable(true);



    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getPassButton() {
        return passButton;
    }

    public Button getBombButton() {
        return bombButton;
    }

    public Button getCallTichuButton() {
        return callTichuButton;
    }

    public ProgressIndicator getCountDown() { return countDown; }
}
