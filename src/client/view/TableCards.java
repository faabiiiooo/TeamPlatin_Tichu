package client.view;

import javafx.scene.layout.HBox;

public class TableCards extends HBox {

    private final CardView tableCardView = new CardView();

    public TableCards(){
        this.getChildren().add(tableCardView);
    }
}
