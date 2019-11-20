package client.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RivalPaneR extends HBox {

    private Label avatarLabel, cardsLabel, rice;
    private Image team1 = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/team1_avatar.png"));
    private Image cardBack = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/card_back.jpg"));
    private Image riceImg = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/rice.png"));

    private VBox vBox = new VBox();

    public RivalPaneR(){
        this.avatarLabel = new Label();
        this.avatarLabel.setGraphic(new ImageView(team1));

        this.cardsLabel = new Label();
        this.cardsLabel.setGraphic(new ImageView(cardBack));

        this.rice = new Label();
        this.rice.setGraphic(new ImageView(riceImg));

        this.vBox.getChildren().addAll(avatarLabel, rice);

        this.getChildren().addAll(cardsLabel, vBox);

        cardsLabel.getStyleClass().add("cardBack");
    }

    public Label getCardsLabel() {
        return cardsLabel;
    }
}
