package code.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import code.db.order.OrderDB;
import code.ui.models.EditOrderScreenModel;

/**
 * Created by ixotum on 7/12/15
 */
public class EditOrderScreenController implements Initializable {
    @FXML
    public OrderComponentController orderComponent;
    private Stage stage;
    private EditOrderScreenModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new EditOrderScreenModel(this);
        model.initListeners();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(OrderDB orderDB) {
        model.init(orderDB);
    }

    public void onClickCancelButton() {
        close();
    }

    public void onClickEditOrderDoneButton() {
        model.updateOrder();
    }

    public OrderComponentController getOrderComponent() {
        return orderComponent;
    }

    public void close() {
        stage.close();
    }

    public void setTitleAsterisk() {
        stage.setTitle(stage.getTitle() + " *");
    }
}
