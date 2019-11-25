package client.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import resources.ServiceLocator;
import resources.Translator;



public class PlayerView extends HBox {


    public PlayerView(){
        super();
        //this.getStyleClass().add("TableView");





    }

    public void addCards(CardView cardView){
        this.getChildren().addAll(cardView);
    }

    public void clear(){
        this.getChildren().clear();
    }


}
