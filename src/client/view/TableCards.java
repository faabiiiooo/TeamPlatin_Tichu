package client.view;


import javafx.scene.layout.HBox;


//@author Pascal
public class TableCards extends HBox {


    public TableCards(){


        this.getStyleClass().add("tableCards");
    }

    public void addCards(CardView cardView){
        this.getChildren().addAll(cardView);
    }

    public void clear(){
        this.getChildren().clear();
    }
}
