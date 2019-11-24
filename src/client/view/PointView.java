package client.view;


import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class PointView extends VBox {

    private Label scoreTeam1, scoreTeam2;


    public PointView(){
        this.scoreTeam1 = new Label("Score Team 1: ");
        this.scoreTeam2 = new Label("Score Team 2: ");
        this.scoreTeam1.getStyleClass().add("scoreLabels");
        this.scoreTeam2.getStyleClass().add("scoreLabels");

        this.getChildren().addAll(scoreTeam1,scoreTeam2);
    }

    public Label getScoreTeam1() {
        return scoreTeam1;
    }

    public Label getScoreTeam2() {
        return scoreTeam2;
    }
}
