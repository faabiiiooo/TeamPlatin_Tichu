package client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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

    private ArrayList<ToggleButton>cardButtons;
    private ToggleButton jButton,qButton,kButton,aButton,nButton;
    private Label mLabel;
    private Image mahjong = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/cards/mahjong_of_specialCards.jpg"));
    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Translator translator = serviceLocator.getTranslator();
    private final Stage wishStage;
    private final ToggleGroup wishButtonGroup = new ToggleGroup();


    public Clt_CardWishView(Stage wishStage){
        this.wishStage=wishStage;
        cardButtons=new ArrayList<ToggleButton>();
        this.jButton=new ToggleButton("J");
        qButton=new ToggleButton("Q");
        kButton=new ToggleButton("K");
        aButton=new ToggleButton("A");
        nButton=new ToggleButton("N");

        mLabel=new Label();
        ImageView imgViewMahjong = new ImageView(mahjong);
        this.mLabel.setGraphic(imgViewMahjong);
        imgViewMahjong.fitHeightProperty().bind(mLabel.heightProperty());
        imgViewMahjong.fitWidthProperty().bind(mLabel.widthProperty());
        mLabel.getStyleClass().add("mahjong");
        imgViewMahjong.setPreserveRatio(true);


        for(int i=2;i<11;i++) {
            ToggleButton b = new ToggleButton(Integer.toString(i));
            cardButtons.add(b);
            this.getChildren().add(b);
            b.setToggleGroup(wishButtonGroup);

            b.getStyleClass().add("wishButtons");
            jButton.getStyleClass().add("wishButtons"); jButton.setToggleGroup(wishButtonGroup);
            qButton.getStyleClass().add("wishButtons"); qButton.setToggleGroup(wishButtonGroup);
            kButton.getStyleClass().add("wishButtons"); kButton.setToggleGroup(wishButtonGroup);
            aButton.getStyleClass().add("wishButtons"); aButton.setToggleGroup(wishButtonGroup);
            nButton.getStyleClass().add("wishButtons"); nButton.setToggleGroup(wishButtonGroup);
            cardButtons.add(jButton); cardButtons.add(qButton); cardButtons.add(kButton); cardButtons.add(aButton); cardButtons.add(nButton);
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

    public ArrayList<ToggleButton> getCardButtons() {
        return cardButtons;
    }

    public ToggleButton getjButton() {
        return jButton;
    }

    public ToggleButton getqButton() {
        return qButton;
    }

    public ToggleButton getkButton() {
        return kButton;
    }

    public ToggleButton getaButton() {
        return aButton;
    }

    public ToggleButton getnButton() {
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

    public ToggleGroup getWishButtonGroup() { return wishButtonGroup; }
}
