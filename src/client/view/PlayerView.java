package client.view;


import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

//@author Pascal
public class PlayerView extends HBox {

    private final ArrayList<CardView> cardViews = new ArrayList<>();
    private final Label rice;
    private Image riceImg = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/rice.png"));


    public PlayerView(){
        super();
        this.getStyleClass().add("PlayerView");

        this.rice = new Label();
        ImageView imgViewRice = new ImageView(riceImg);
        this.rice.setGraphic(imgViewRice);
        imgViewRice.fitWidthProperty().bind(rice.widthProperty());
        imgViewRice.fitHeightProperty().bind(rice.heightProperty());
        this.rice.getStyleClass().add("rice");
        imgViewRice.setPreserveRatio(true);


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

    public Label getRice() {
        return rice;
    }
}
