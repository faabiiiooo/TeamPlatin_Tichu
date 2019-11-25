package client.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import resources.Card;
import resources.ServiceLocator;
import resources.Translator;

import java.util.ArrayList;


public class PlayerView extends HBox {

    private final ArrayList<CardView> cardViews = new ArrayList<>();


    public PlayerView(){
        super();
        //this.getStyleClass().add("TableView");

    }

    public void addCards(CardView cardView){
        this.getChildren().addAll(cardView);
        cardViews.add(cardView);
    }

    public void clear(){
        this.getChildren().clear();
        cardViews.clear();
    }

    public ArrayList<CardView> getCardViews(){ return cardViews; }


}
