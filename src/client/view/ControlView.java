package client.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import resources.ServiceLocator;
import resources.Translator;

//@author thomas
public class ControlView extends HBox {

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Translator translator = serviceLocator.getTranslator();

    private Button playButton, passButton, bombButton, callTichuButton;
    private Label countDownLabel;


    public ControlView(){
        this.playButton = new Button(translator.getString("button.playCards"));
        this.passButton = new Button(translator.getString("button.pass"));
        this.bombButton = new Button(translator.getString("button.bomb"));
        this.callTichuButton = new Button(translator.getString("button.callTichu"));
        this.countDownLabel = new Label("");

        this.getChildren().addAll(countDownLabel,playButton,passButton,bombButton,callTichuButton);

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

    public Label getCountDownLabel() {
        return countDownLabel;
    }

}
