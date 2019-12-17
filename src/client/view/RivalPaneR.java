package client.view;

import com.sun.jnlp.IntegrationServiceNSBImpl;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class RivalPaneR extends VBox {

    private Label avatarLabel, cardsLabel, rice,rName;
    private Image team1 = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/team2_avatar.png"));
    private Image cardBack = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/card_back.jpg"));
    private Image riceImg = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/rice.png"));

    private GridPane gridPane =new GridPane();
    private StackPane stack = new StackPane();
    private Text cardAmountText = new Text();

    public RivalPaneR(){
        this.avatarLabel = new Label();
        ImageView imgViewAvatar = new ImageView(team1);
        this.avatarLabel.setGraphic(imgViewAvatar);
        imgViewAvatar.fitHeightProperty().bind(avatarLabel.heightProperty());
        imgViewAvatar.fitWidthProperty().bind(avatarLabel.widthProperty());
        avatarLabel.getStyleClass().add("avatar");
        imgViewAvatar.setPreserveRatio(true);

        this.cardsLabel = new Label();
        ImageView imgViewCard = new ImageView(cardBack);
        this.cardsLabel.setGraphic(imgViewCard);
        imgViewCard.fitWidthProperty().bind(cardsLabel.widthProperty());
        imgViewCard.fitHeightProperty().bind(cardsLabel.heightProperty());
        cardsLabel.getStyleClass().add("cardBack");
        imgViewCard.setPreserveRatio(true);
        stack.getChildren().addAll(cardsLabel,cardAmountText);
        cardAmountText.setId("cardAmount");

        this.rName=new Label();
        this.rName.setPadding(new Insets(2));
        this.rName.setId("playerIdLR");
        this.rice = new Label();
        ImageView imgViewRice = new ImageView(riceImg);
        this.rice.setGraphic(imgViewRice);
        imgViewRice.fitWidthProperty().bind(rice.widthProperty());
        imgViewRice.fitHeightProperty().bind(rice.heightProperty());
        this.rice.getStyleClass().add("rice");
        imgViewRice.setPreserveRatio(true);

        this.setPadding(new Insets(2,30,0,80));
        gridPane.setPadding(new Insets(0,0,25,0));
        gridPane.add(rice,2,1);
        gridPane.add(stack,1,3);
        gridPane.setHgap(5);
        //gridPane.setVgap(5);

        gridPane.add(avatarLabel,2,3);
        gridPane.add(rName,2,2);
        this.getChildren().add(gridPane);

    }

    public Label getCardsLabel() {
        return cardsLabel;
    }
    public Text getCardAmountText() { return cardAmountText; }

    public Label getRiceLabel() {
        return this.rice;
    }

    public Label getrName() {
        return rName;
    }
}
