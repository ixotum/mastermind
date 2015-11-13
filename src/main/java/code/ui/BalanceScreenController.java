package code.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import code.ui.balance.BalanceRowData;
import code.ui.balance.BalanceScreenModel;

/**
 * Created by ixotum on 08.11.15
 */
public class BalanceScreenController implements Initializable {
    @FXML
    private TableView<BalanceRowData> table;
    @FXML
    private TableColumn<BalanceRowData, Label> columnMonth;
    @FXML
    private TableColumn<BalanceRowData, Label> columnIncome;
    @FXML
    private TableColumn<BalanceRowData, Label> columnExpenses;
    @FXML
    private TableColumn<BalanceRowData, Label> columnBalance;
    @FXML
    private ComboBox<Integer> comboYear;

    private Stage stage;
    private BalanceScreenModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new BalanceScreenModel(this);
        model.initTable();
        model.initCombo();
        model.updateContent();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TableView<BalanceRowData> getTable() {
        return table;
    }

    public TableColumn<BalanceRowData, Label> getColumnMonth() {
        return columnMonth;
    }

    public TableColumn<BalanceRowData, Label> getColumnIncome() {
        return columnIncome;
    }

    public TableColumn<BalanceRowData, Label> getColumnExpenses() {
        return columnExpenses;
    }

    public TableColumn<BalanceRowData, Label> getColumnBalance() {
        return columnBalance;
    }

    public ComboBox<Integer> getComboYear() {
        return comboYear;
    }

    public void close() {
        stage.close();
    }
}
