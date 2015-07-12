package code.ui;

import code.db.OrderDB;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Created by ixotum on 7/12/15
 */
public class EditOrderScreenController {
    @FXML
    public OrderComponentController orderComponent;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(OrderDB orderDB) {
        orderComponent.getLabelOrderId().setText(String.valueOf(orderDB.getOrderId()));
        orderComponent.getTextFieldName().setText(orderDB.getName());
    }

    public void onClickCancelButton() {
        stage.hide();
    }

    public void onClickNewOrderDoneButton() {
        stage.hide();
    }
}
