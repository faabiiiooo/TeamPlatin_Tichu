package client.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class RivalPaneL extends HBox {

    private Label avatarLabel, cardsLabel, rice;
    private Image team1 = new Image(getClass().getResourceAsStream("team1_avatar.png"));
    private Image cardBack = new Image(getClass().getResourceAsStream("card_back.png"));
    private Image riceImg = new Image(getClass().getResourceAsStream("rice.png"));

    private VBox vBox = new VBox();

    public RivalPaneL(){
        this.avatarLabel = new Label();
        this.avatarLabel.setGraphic(new ImageView(team1));

        this.cardsLabel = new Label();
        this.cardsLabel.setGraphic(new ImageView(cardBack));

        this.rice = new Label();
        this.rice.setGraphic(new ImageView(riceImg));

        this.vBox.getChildren().addAll(avatarLabel, rice);

        this.getChildren().addAll(vBox, cardsLabel);
    }

    public Label getCardsLabel() {
        return cardsLabel;
    }
}
