package code.ui;

import code.ui.models.ExpenseEditModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ixotum on 17.10.15
 */
public class ExpenseEditController implements Initializable {
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private TextField textFieldDescription;
    @FXML
    private TextField textFieldNote;
    @FXML
    private TextField textFieldAmount;
    @FXML
    private Button buttonOK;

    private Stage stage;
    private ExpenseEditModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new ExpenseEditModel(this);
        model.initDatePicker();
        model.initComboType();
        model.initTextFieldAmount();
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

    public void close() {
        stage.close();
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setEntityId(Integer entityId) {
        model.setEntityId(entityId);
    }

    public ComboBox<String> getComboType() {
        return comboType;
    }

    public TextField getTextFieldDescription() {
        return textFieldDescription;
    }

    public TextField getTextFieldNote() {
        return textFieldNote;
    }

    public TextField getTextFieldAmount() {
        return textFieldAmount;
    }

    public Button getButtonOK() {
        return buttonOK;
    }
}
