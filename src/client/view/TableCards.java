package client.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;



public class TableCards extends HBox {


    public TableCards(){

        this.setPadding(new Insets(0,0,20,0));


        this.getStyleClass().add("tableCards");
    }

    public void addCards(CardView cardView){
        this.getChildren().addAll(cardView);
    }

    public void clear(){
        this.getChildren().clear();
    }
}
