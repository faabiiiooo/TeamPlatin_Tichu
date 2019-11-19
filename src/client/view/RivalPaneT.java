package client.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RivalPaneT extends VBox {

    private Label avatarLabel, cardsLabel, rice;
    private Image team2 = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/team2_avatar.png"));
    private Image cardBack = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/card_back.jpg"));
    private Image riceImg = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/rice.png"));
    private final HBox hBox = new HBox();

    public RivalPaneT(){
        this.avatarLabel = new Label();
        this.avatarLabel.setGraphic(new ImageView(team2));

        this.cardsLabel = new Label();
        this.cardsLabel.setGraphic(new ImageView(cardBack));

        this.rice = new Label();
        this.rice.setGraphic(new ImageView(riceImg));

        this.hBox.getChildren().addAll(avatarLabel, rice);

        this.getChildren().addAll(hBox, cardsLabel);
    }

    public Label getCardsLabel() {
        return cardsLabel;
    }

}
