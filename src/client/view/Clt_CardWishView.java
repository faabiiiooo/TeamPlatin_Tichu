package client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import resources.ServiceLocator;
import resources.Translator;


import java.util.ArrayList;

//@author Pascal
public class Clt_CardWishView extends HBox {

    private ArrayList<Button>cardButtons;
    private Button jButton,qButton,kButton,aButton,nButton;
    private Label mLabel;
    private Image mahjong = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/cards/mahjong_of_specialCards.jpg"));
    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Translator translator = serviceLocator.getTranslator();
    private final Stage wishStage;


    public Clt_CardWishView(Stage wishStage){
        this.wishStage=wishStage;
        cardButtons=new ArrayList<Button>();
        this.jButton=new Button("J");
        qButton=new Button("Q");
        kButton=new Button("K");
        aButton=new Button("A");
        nButton=new Button("N");

        mLabel=new Label();
        ImageView imgViewMahjong = new ImageView(mahjong);
        this.mLabel.setGraphic(imgViewMahjong);
        imgViewMahjong.fitHeightProperty().bind(mLabel.heightProperty());
        imgViewMahjong.fitWidthProperty().bind(mLabel.widthProperty());
        mLabel.getStyleClass().add("mahjong");
        imgViewMahjong.setPreserveRatio(true);


        for(int i=2;i<11;i++) {
            Button b = new Button(Integer.toString(i));
            cardButtons.add(b);
            this.getChildren().add(b);


            b.getStyleClass().add("wishButtons");
            jButton.getStyleClass().add("wishButtons");
            qButton.getStyleClass().add("wishButtons");
            kButton.getStyleClass().add("wishButtons");
            aButton.getStyleClass().add("wishButtons");
            nButton.getStyleClass().add("wishButtons");
        }
            this.getChildren().addAll(jButton, qButton, kButton, aButton, nButton,mLabel);

            Scene scene = new Scene(this, 1455, 130);
            this.setPadding(new Insets(10,40,10,40));
            this.setAlignment(Pos.CENTER);
            this.setSpacing(1);
            mLabel.setPadding(new Insets(10,10,10,10));
            this.setId("hBox");
            wishStage.setResizable(false);

            scene.getStylesheets().add(getClass().getResource("CardWishView.css").toExternalForm());
            this.wishStage.setTitle(translator.getString("program.name"));
            this.wishStage.getIcons().add(new Image("./resources/images/logo.jpg"));
            this.wishStage.setScene(scene);
            this.wishStage.show();


}

    public ArrayList<Button> getCardButtons() {
        return cardButtons;
    }

    public Button getjButton() {
        return jButton;
    }

    public Button getqButton() {
        return qButton;
    }

    public Button getkButton() {
        return kButton;
    }

    public Button getaButton() {
        return aButton;
    }

    public Button getnButton() {
        return nButton;
    }

    public Label getmLabel() {
        return mLabel;
    }

    public Image getMahjong() {
        return mahjong;
    }

    public Stage getWishStage() {
        return wishStage;
    }

}
