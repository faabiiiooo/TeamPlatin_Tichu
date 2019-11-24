package client.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import resources.ServiceLocator;
import resources.Translator;



public class PlayerView extends HBox {

    private  final  Label cardView = new Label();

    public PlayerView(){
        super();
        //this.getStyleClass().add("TableView");



        this.getChildren().add(cardView);

    }
}
