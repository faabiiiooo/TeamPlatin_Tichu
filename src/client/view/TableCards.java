package client.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;



public class TableCards extends HBox {

    private final Label tableCardsLabel = new Label();

    public TableCards(){
        this.getChildren().add(tableCardsLabel);

        this.getStyleClass().add("tableCards");
    }
}
