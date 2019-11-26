package client.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;



public class TableCards extends HBox {


    public TableCards(){


        this.getStyleClass().add("tableCards");
    }

    public void addCards(CardView cardView){
        this.getChildren().addAll(cardView);
    }

    public void clear(){
        this.getChildren().clear();
    }
}
