package client.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import server.model.Srv_Card;

public class CardLabel extends Label { //@author Sandro

    public CardLabel() {
        super();
        this.getStyleClass().add("card");
    }

    public void setCard(Srv_Card card) { //set the right image of the card
        String fileName = cardToFileName(card);
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream("./resources/images/cards/" + fileName));
        ImageView imv = new ImageView(image);
        imv.fitWidthProperty().bind(this.widthProperty());
        imv.fitHeightProperty().bind(this.heightProperty());
        imv.setPreserveRatio(true);
        this.setGraphic(imv);
    }

    private String cardToFileName(Srv_Card card) { //toString methods to find the right image
        String rank = card.getRank().toString();
        String suit = card.getSuit().toString();
        return rank + "_of_" + suit + ".jpg";
    }
}