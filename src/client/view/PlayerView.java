package client.view;

import javafx.scene.layout.HBox;
import resources.ServiceLocator;
import resources.Translator;

public class PlayerView extends HBox {

    private  final  CardView cardView = new CardView();

    public PlayerView(){
        super();
        this.getStyleClass().add("TableView");

        this.getChildren().add(cardView);

    }
}
