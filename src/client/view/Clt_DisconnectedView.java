package client.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import resources.ServiceLocator;
import resources.Translator;

//@author Pascal
public class Clt_DisconnectedView extends HBox {

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Translator translator = serviceLocator.getTranslator();
    private Label label;
    private final Stage dcStage;

    public Clt_DisconnectedView(Stage dcStage) {
        this.dcStage = dcStage;
        this.label=new Label(translator.getString("label.error"));
        this.getChildren().add(label);



        Scene scene = new Scene(this,900,600);
        this.setAlignment(Pos.CENTER);
        scene.getStylesheets().add(getClass().getResource("DcView.css").toExternalForm());
        this.setId("background");

        this.dcStage.setScene(scene);
        this.dcStage.show();
    }
}
