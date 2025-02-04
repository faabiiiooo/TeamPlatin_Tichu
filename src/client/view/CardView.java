package client.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import resources.Card;
import resources.ServiceLocator;
import java.util.logging.Logger;

//@author Sandro
public class CardView extends Label { // -> Reference: Poker_Projekt by Bradley Richards

    private final Card card;
    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();


    private final SimpleBooleanProperty selected = new SimpleBooleanProperty();

    public CardView(Card card) {
        super();
        this.getStyleClass().add("card");
        setCard(card);
        this.card = card;
        this.selected.set(false);

    }

    public void setCard(Card card) { //set the right image of the card
        String fileName = cardToFileName(card);
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

    public Card getCard(){
        return card;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
