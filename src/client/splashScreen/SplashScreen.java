package client.splashScreen;

import javafx.application.Preloader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashScreen extends Preloader {

    private Stage stage;
    private ProgressBar progress = new ProgressBar();
    private Label lblStatus = new Label();

    public void start(Stage splashStage) throws Exception{
        this.stage = splashStage;
        stage.initStyle(StageStyle.TRANSPARENT);
    }


}
