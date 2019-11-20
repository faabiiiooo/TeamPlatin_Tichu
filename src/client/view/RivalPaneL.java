package client.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class RivalPaneL extends HBox {

    private Label avatarLabel, cardsLabel, rice;
    private Image team1 = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/team1_avatar.png"));
    private Image cardBack = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/card_back.jpg"));
    private Image riceImg = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/rice.png"));

    private VBox vBox = new VBox();

    public RivalPaneL(){

        this.avatarLabel = new Label();
        ImageView imgViewAvatar = new ImageView(team1);
        this.avatarLabel.setGraphic(imgViewAvatar);
        imgViewAvatar.fitHeightProperty().bind(avatarLabel.heightProperty());
        imgViewAvatar.fitWidthProperty().bind(avatarLabel.widthProperty());
        avatarLabel.getStyleClass().add("avatar");
        imgViewAvatar.setPreserveRatio(true);

        this.cardsLabel = new Label();
        ImageView imgViewCard = new ImageView(cardBack);
        this.cardsLabel.setGraphic(imgViewCard);
        imgViewCard.fitWidthProperty().bind(cardsLabel.widthProperty());
        imgViewCard.fitHeightProperty().bind(cardsLabel.heightProperty());
        cardsLabel.getStyleClass().add("cardBack");
        imgViewCard.setPreserveRatio(true);


        this.rice = new Label();
        ImageView imgViewRice = new ImageView(riceImg);
        this.rice.setGraphic(imgViewRice);
        imgViewRice.fitWidthProperty().bind(rice.widthProperty());
        imgViewRice.fitHeightProperty().bind(rice.heightProperty());
        this.rice.getStyleClass().add("rice");
        imgViewRice.setPreserveRatio(true);

        this.vBox.getChildren().addAll(avatarLabel, rice);



        this.getChildren().addAll(vBox,cardsLabel);

    }

    public Label getCardsLabel() {
        return cardsLabel;
    }
}
