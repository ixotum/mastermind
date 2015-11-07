package code.ui;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

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

    @FXML
    public void onClickCancelButton() {
        askAction();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(OrderDB orderDB) {
        model.init(orderDB);
    }

    public void onClickEditOrderDoneButton() {
        askAction();
    }

    public OrderComponentController getOrderComponent() {
        return orderComponent;
    }

    public void askAction() {
        if (model.isChanging()) {
            Alert questionDialog = new Alert(Alert.AlertType.CONFIRMATION);
            questionDialog.setTitle("Order changed");
            questionDialog.setHeaderText("The order was changed");
            questionDialog.setContentText("What would you like to do?");

            ButtonType typeSave = new ButtonType("Save");
            ButtonType typeDontSave = new ButtonType("Don't save");
            ButtonType typeCancel = new ButtonType("Cancel");
            questionDialog.getButtonTypes().setAll(typeSave, typeDontSave, typeCancel);

            Optional<ButtonType> result = questionDialog.showAndWait();
            ButtonType resultType = result.get();
            if (resultType == typeSave) {
                model.updateOrder();
                model.setChanging(false);
                stage.close();
            } else if (resultType == typeDontSave) {
                model.setChanging(false);
                stage.close();
            }
        } else {
            stage.close();
        }
    }

    public void setTitleAsterisk() {
        stage.setTitle(stage.getTitle() + " *");
    }
}
