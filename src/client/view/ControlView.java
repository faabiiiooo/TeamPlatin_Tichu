package client.view;


import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import resources.ServiceLocator;
import resources.Translator;

//@author Pascal
public class ControlView extends HBox {

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Translator translator = serviceLocator.getTranslator();

    private Button playButton, passButton, bombButton, callTichuButton;



    public ControlView(){
        this.playButton = new Button(translator.getString("button.playCards"));
        this.passButton = new Button(translator.getString("button.pass"));
        this.bombButton = new Button(translator.getString("button.bomb"));
        this.callTichuButton = new Button(translator.getString("button.callTichu"));


        playButton.getStyleClass().add("controlButtons");
        passButton.getStyleClass().add("controlButtons");
        bombButton.getStyleClass().add("controlButtons");
        callTichuButton.getStyleClass().add("controlButtons");


        this.setId("bottom");
        this.getChildren().addAll(playButton,passButton,bombButton,callTichuButton);
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

}
