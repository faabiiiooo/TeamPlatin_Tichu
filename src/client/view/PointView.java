package client.view;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


//@author Pascal
public class PointView extends GridPane {

    private Label scoreTeam1, scoreTeam2;


    public PointView(){
        this.scoreTeam1 = new Label("Score Team 1: ");
        this.scoreTeam2 = new Label("Score Team 2: ");
        this.scoreTeam1.getStyleClass().add("scoreLabels");
        this.scoreTeam2.getStyleClass().add("scoreLabels");

        this.add(scoreTeam1,1,1);
        this.add(scoreTeam2,1,2);
        this.setVgap(10);

        this.setPadding(new Insets(10,10,0,10));

    }

    public Label getScoreTeam1() {
        return scoreTeam1;
    }

    public Label getScoreTeam2() {
        return scoreTeam2;
    }
}
