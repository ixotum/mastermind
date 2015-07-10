package code.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by ixotum on 7/8/15
 */
public class OrderCardController extends VBox {
    @FXML
    public AnchorPane mainAnchor;

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
    }

    public double getCardWidth() {
        return mainAnchor.getPrefWidth();
    }
}
