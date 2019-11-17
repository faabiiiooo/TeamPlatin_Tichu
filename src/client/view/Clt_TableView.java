package client.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    private final VBox bottom;
    private final HBox top;
    private final Label countdown;

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

        this.setLeft(rivalLeft);
        this.setRight(rivalRight);

        top.getChildren().addAll(rivalTop, pointView);
        this.setTop(top);

        bottom.getChildren().addAll(playerView,controls);
        this.setBottom(bottom);


        root.getChildren().addAll(this);

        Scene tableScene = new Scene(root);
        tableScene.getStylesheets().add(getClass().getResource("TableView.css").toExternalForm());
        this.primaryStage.setResizable(true);
        this.primaryStage.setScene(tableScene);
        this.primaryStage.setTitle(translator.getString("program.name"));
        this.primaryStage.getIcons().add(new Image("./resources/images/logo.jpg"));
        this.primaryStage.show();




    }
}
