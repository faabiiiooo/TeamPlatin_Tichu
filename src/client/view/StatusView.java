package client.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import resources.ServiceLocator;
import resources.Translator;

import javax.swing.*;

//@author Pascal
public class StatusView extends GridPane {

    private HBox hBox;
    private Label wishHeader,wished,tichuHeader, tichuYesOrNo,statusHeader,status;
    private ScrollPane sp1,sp2,sp3;
    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Translator translator = serviceLocator.getTranslator();

    public StatusView() {

        this.sp1=new ScrollPane();
        this.sp2=new ScrollPane();
        this.sp3=new ScrollPane();
        this.wishHeader = new Label(translator.getString("label.wishHeader"));
        this.wished = new Label();
        this.tichuHeader = new Label("Tichu");
        this.tichuYesOrNo = new Label();
        this.statusHeader=new Label("Status");
        this.status=new Label();

        this.wishHeader.setId("statusTitle");
        this.tichuHeader.setId("statusTitle");
        this.statusHeader.setId("statusTitle");


        this.setVgap(5);


        this.setPadding(new Insets(10,10,0,10));
        this.add(wishHeader, 1, 1);
        this.add(sp1, 1, 2);

        this.add(statusHeader,1,3);
        this.add(sp2,1,4);

        this.add(tichuHeader,1,5);
        this.add(sp3,1,6);

        sp1.setContent(wished);
        sp1.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp1.setStyle("-fx-background-color:transparent");
        sp1.vvalueProperty().bind(wished.heightProperty());

        sp2.setContent(status);
        sp2.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp2.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp2.setStyle("-fx-background-color:transparent");
        sp2.vvalueProperty().bind(status.heightProperty());

        sp3.setContent(tichuYesOrNo);
        sp3.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp3.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp3.setStyle("-fx-background-color:transparent");
        sp3.vvalueProperty().bind(tichuYesOrNo.heightProperty());


        this.getStyleClass().add("wishView");
        this.tichuYesOrNo.setId("status");
        this.status.setId("status");
        this.wished.setId("status");



    }



    public Label getWishHeader() {
        return wishHeader;
    }

    public void setWishHeader(Label wishHeader) {
        this.wishHeader = wishHeader;
    }

    public Label getWished() {
        return wished;
    }

    public void setWished(Label wished) {
        this.wished = wished;
    }

    public Label getTichuHeader() {
        return tichuHeader;
    }

    public void setTichuHeader(Label tichuHeader) {
        this.tichuHeader = tichuHeader;
    }

    public Label getTichuYesOrNo() {
        return tichuYesOrNo;
    }

    public void setTichuYesOrNo(Label tichuYesOrNo) {
        this.tichuYesOrNo = tichuYesOrNo;
    }

    public Label getStatusHeader() {
        return statusHeader;
    }

    public void setStatusHeader(Label statusHeader) {
        this.statusHeader = statusHeader;
    }

    public Label getStatus() {
        return status;
    }

    public void setStatus(Label status) {
        this.status = status;
    }

    public ScrollPane getSp1() {
        return sp1;
    }

    public void setSp1(ScrollPane sp1) {
        this.sp1 = sp1;
    }

    public ScrollPane getSp2() {
        return sp2;
    }

    public void setSp2(ScrollPane sp2) {
        this.sp2 = sp2;
    }
}
