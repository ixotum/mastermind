package code.ui;

import code.ui.models.ExpenseEditModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ixotum on 17.10.15
 */
public class ExpenseEditController implements Initializable {
    private Stage stage;

    @FXML
    private DatePicker datePicker;
    private ExpenseEditModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new ExpenseEditModel(this);
        model.initDatePicker();
    }

    @FXML
    public void onClickOkButton() {
        model.processExpense();
    }

    @FXML
    public void onClickCancelButton() {
        close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void close() {
        stage.close();
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setCreateNew(boolean createNew) {
        model.setCreateNew(createNew);
    }
}
