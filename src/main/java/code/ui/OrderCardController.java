package code.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by ixotum on 7/8/15
 */
public class OrderCardController extends VBox {
    @FXML
    public AnchorPane mainAnchor;
    @FXML
    public Label labelOrderId;

    public OrderCardController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/order_card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onOrderCardClick() {
        System.out.println("++");
    }

    public double getCardWidth() {
        return mainAnchor.getPrefWidth();
    }

    public void setOrderId(int orderId) {
        labelOrderId.setText(String.valueOf(orderId));
    }
}
