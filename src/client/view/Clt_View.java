package client.view;

import client.model.Clt_Model;
import javafx.stage.Stage;

import java.net.MalformedURLException;

//Basic Class to start build the GUI
//@author Pascal
public class Clt_View {

    private Stage primaryStage;
    private Clt_Model model;

    private final Clt_StartScreen startScreen;
    private Clt_TableView tableView;
    private Clt_CardWishView cardWishView;
    private Clt_DisconnectedView dcView;
    private Clt_WinnerView winnerView;


    public Clt_View(Stage primaryStage, Clt_Model model) throws MalformedURLException {
        this.primaryStage = primaryStage;
        this.model = model;

        startScreen = new Clt_StartScreen(primaryStage);

    }

    public void startTableView(){
        tableView = new Clt_TableView(primaryStage);
    }

    public void startWishView(){cardWishView=new Clt_CardWishView(new Stage());}

    public void startDcView(){dcView=new Clt_DisconnectedView(primaryStage);}

    public void startWinnView(){winnerView=new Clt_WinnerView(primaryStage);}


    //Start displaying View
    public void start(){
        this.primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Clt_Model getModel() {
        return model;
    }

    public Clt_StartScreen getStartScreen() { return startScreen; }

    public Clt_TableView getTableView() { return tableView; }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Clt_CardWishView getCardWishView() { return cardWishView; }

    public Clt_WinnerView getWinnerView() {
        return winnerView;
    }
}
