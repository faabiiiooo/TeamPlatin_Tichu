package client.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import resources.ServiceLocator;
import resources.Translator;

public class Clt_WinnerView extends HBox {

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Translator translator = serviceLocator.getTranslator();
    private Label label;
    private final Stage winStage;

    public Clt_WinnerView(Stage winStage) {
        this.winStage=winStage;
        this.label=new Label(translator.getString("label.winnMessage"));
        this.getChildren().add(label);

        this.setId("winnView");

        Scene scene = new Scene(this,900,600);
        this.setAlignment(Pos.CENTER);
        scene.getStylesheets().add(getClass().getResource("WinnView.css").toExternalForm());

        this.winStage.setTitle(translator.getString("program.name"));
        this.winStage.getIcons().add(new Image("./resources/images/logo.jpg"));
        this.winStage.setScene(scene);
        this.winStage.show();
    }

    public Label getLabel() {
        return label;
    }
}


