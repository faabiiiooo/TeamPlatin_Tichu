package client.view;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

//@author Pascal
public class RivalPaneT extends HBox {

    private Label avatarLabel, cardsLabel, rice,tName;
    private Image team2 = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/team1_avatar.png"));
    private Image cardBack = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/card_back.jpg"));
    private Image riceImg = new Image(getClass().getClassLoader().getResourceAsStream("./resources/images/ingame/rice.png"));
    private GridPane gridPane=new GridPane();
    private StackPane stack = new StackPane();
    private Text cardAmountText = new Text();

    public RivalPaneT(){
        this.getStyleClass().add("RivalPaneT");

        this.avatarLabel = new Label();
        ImageView imgViewAvatar = new ImageView(team2);
        this.avatarLabel.setGraphic(imgViewAvatar);
        imgViewAvatar.fitHeightProperty().bind(avatarLabel.heightProperty());
        imgViewAvatar.fitWidthProperty().bind(avatarLabel.widthProperty());
        avatarLabel.getStyleClass().add("avatar");
        imgViewAvatar.setPreserveRatio(true);

        this.cardsLabel = new Label();
        ImageView imgViewCard = new ImageView(cardBack);
        imgViewCard.setRotate(90);
        this.cardsLabel.setGraphic(imgViewCard);
        imgViewCard.fitWidthProperty().bind(cardsLabel.widthProperty());
        imgViewCard.fitHeightProperty().bind(cardsLabel.heightProperty());
        cardsLabel.getStyleClass().add("cardBack");
        cardsLabel.setId("cardBackTop");
        imgViewCard.setPreserveRatio(true);
        stack.getChildren().addAll(cardsLabel,cardAmountText);
        cardAmountText.setId("cardAmount");


        this.rice = new Label();
        ImageView imgViewRice = new ImageView(riceImg);
        this.rice.setGraphic(imgViewRice);
        imgViewRice.fitWidthProperty().bind(rice.widthProperty());
        imgViewRice.fitHeightProperty().bind(rice.heightProperty());
        this.rice.setId("riceTop");
        imgViewRice.setPreserveRatio(true);

        this.tName=new Label();
        this.tName.setId("playerIdTop");
        gridPane.setPadding(new Insets(0,450,0,350));
        gridPane.add(avatarLabel,0,1);
        gridPane.add(tName,0,2);
        gridPane.add(stack,0,3);
        gridPane.add(rice,2,1);
        gridPane.setHgap(8);
        this.getChildren().add(gridPane);


    }

    public Label getCardsLabel() {
        return cardsLabel;
    }

    public Text getCardAmountText() { return cardAmountText; }

    public Label getRiceLabel() {
        return this.rice;
    }

    public Label gettName() {
        return tName;
    }
}


