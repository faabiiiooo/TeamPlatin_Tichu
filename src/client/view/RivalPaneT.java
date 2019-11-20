package client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;

public class RivalPaneT extends VBox {

    private Label avatarLabel, cardsLabel, rice;
    private Image team2 = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/team2_avatar.png"));
    private Image cardBack = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/card_back.jpg"));
    private Image riceImg = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/rice.png"));
    private final HBox hBox = new HBox();

    public RivalPaneT(){
        this.getStyleClass().add("RivalPaneT");

        this.avatarLabel = new Label();
        ImageView imgViewAvatar = new ImageView(team2);
        this.avatarLabel.setGraphic(imgViewAvatar);
        imgViewAvatar.fitHeightProperty().bind(avatarLabel.heightProperty());
        imgViewAvatar.fitWidthProperty().bind(avatarLabel.widthProperty());
        avatarLabel.getStyleClass().add("avatar");
        imgViewAvatar.setPreserveRatio(true);

        this.cardsLabel = new Label();
        ImageView imgViewCard = new ImageView(cardBack);
        imgViewCard.setRotate(90);
        this.cardsLabel.setGraphic(imgViewCard);
        imgViewCard.fitWidthProperty().bind(cardsLabel.widthProperty());
        imgViewCard.fitHeightProperty().bind(cardsLabel.heightProperty());
        cardsLabel.getStyleClass().add("cardBack");
        cardsLabel.setId("cardBackTop");
        imgViewCard.setPreserveRatio(true);

        this.rice = new Label();
        ImageView imgViewRice = new ImageView(riceImg);
        this.rice.setGraphic(imgViewRice);
        imgViewRice.fitWidthProperty().bind(rice.widthProperty());
        imgViewRice.fitHeightProperty().bind(rice.heightProperty());
        this.rice.setId("riceTop");
        imgViewRice.setPreserveRatio(true);

        this.setPadding(new Insets(0,600,80,600));

        this.hBox.getChildren().addAll(avatarLabel, rice);


        this.getChildren().addAll(hBox, cardsLabel);

    }

    public Label getCardsLabel() {
        return cardsLabel;
    }

}
