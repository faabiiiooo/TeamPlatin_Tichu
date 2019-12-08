package client.view;

import com.sun.prism.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import resources.ServiceLocator;
import resources.Translator;


public class Clt_TableView extends BorderPane {

    private final StackPane root;
    private final RivalPaneL rivalLeft;
    private final RivalPaneR rivalRight;
    private final RivalPaneT rivalTop;
    private final PlayerView playerView;
    private final PointView pointView;
    private final ControlView controls;
    private final TableCards tableCards;
    private final VBox bottom;
    private final HBox top;
    private final Label countdown;
    private final Label tichuLabel;
    private final StatusView statusView;
    private ProgressIndicator countdownDisplay;





    private final Stage primaryStage;

    private final ServiceLocator sl = ServiceLocator.getServiceLocator();
    private final Translator translator = sl.getTranslator();



    public Clt_TableView(Stage primaryStage){

        this.primaryStage = primaryStage;

        this.root = new StackPane();
        this.rivalLeft = new RivalPaneL();
        this.rivalRight = new RivalPaneR();
        this.rivalTop = new RivalPaneT();
        this.playerView = new PlayerView();
        this.pointView = new PointView();
        this.bottom = new VBox();
        this.top = new HBox();
        this.countdown = new Label();
        this.controls = new ControlView();
        this.tableCards = new TableCards();
        this.tichuLabel=new Label();
        this.statusView=new StatusView();
        this.countdownDisplay=new ProgressIndicator(0);

        this.setLeft(rivalLeft);
        this.setRight(rivalRight);

        top.getChildren().addAll(countdownDisplay,statusView,rivalTop, pointView);
        this.setTop(top);


        bottom.getChildren().addAll(playerView,controls);
        this.setBottom(bottom);

        this.setCenter(tableCards);
        this.tichuLabel.setPadding(new Insets(40,0,0,0));
        this.tichuLabel.setId("tichuLabel");

        root.getChildren().addAll(this);
        root.getChildren().add(tichuLabel);

        StackPane.setAlignment(this, Pos.CENTER);

        Scene tableScene = new Scene(root,1480,900);
        tableScene.getStylesheets().add(getClass().getResource("TableView.css").toExternalForm());
        this.primaryStage.setResizable(true);
        this.setId("start-pane");
        this.primaryStage.setScene(tableScene);
        this.primaryStage.setTitle(translator.getString("program.name"));
        this.primaryStage.getIcons().add(new Image("./resources/images/logo.jpg"));
        this.primaryStage.show();




    }

    public StackPane getRoot() {
        return root;
    }

    public RivalPaneL getRivalLeft() {
        return rivalLeft;
    }

    public RivalPaneR getRivalRight() {
        return rivalRight;
    }

    public RivalPaneT getRivalTop() {
        return rivalTop;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    public PointView getPointView() {
        return pointView;
    }

    public ControlView getControls() {
        return controls;
    }


    public VBox getBottomBox() {
        return bottom;
    }

    public HBox getTopBox() {
        return top;
    }

    public Label getCountdown() {
        return countdown;
    }


    public StatusView getStatusView() {
        return statusView;
    }

    public Label getTichuLabel() {
        return tichuLabel;
    }

    public TableCards getTableCards() {
        return tableCards;
    }

    public ProgressIndicator getCountdownDisplay() {
        return countdownDisplay;
    }

    public void setCountdownDisplay(ProgressIndicator countdownDisplay) {
        this.countdownDisplay = countdownDisplay;
    }
}
