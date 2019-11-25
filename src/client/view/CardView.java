package client.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import resources.Card;

public class CardView extends Label { //@author Sandro

    public CardView(Card card) {
        super();
        this.getStyleClass().add("card");
        setCard(card);
    }

    public void setCard(Card card) { //set the right image of the card
        String fileName = cardToFileName(card);
        System.out.println(fileName);
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream("./resources/images/cards/" + fileName));
        ImageView imv = new ImageView(image);
        imv.fitWidthProperty().bind(this.widthProperty());
        imv.fitHeightProperty().bind(this.heightProperty());
        imv.setPreserveRatio(true);
        this.setGraphic(imv);
    }

    private String cardToFileName(Card card) { //toString methods to find the right image
        String rank = card.getRank().toString();
        String suit = card.getSuit().toString();
        return rank + "_of_" + suit + ".jpg";
    }
}
