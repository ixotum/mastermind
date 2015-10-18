package code.ui.expenses;

import code.ui.ExpenseEditController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by ixotum on 16.10.15
 */
public class ExpensesController implements Initializable {
    private Stage stage;
    private ExpensesModel model;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TableView<ExpenseRowData> table;
    @FXML
    private TableColumn<ExpenseRowData, String> columnDate;
    @FXML
    private TableColumn<ExpenseRowData, String> columnType;
    @FXML
    private TableColumn<ExpenseRowData, String> columnDescription;
    @FXML
    private TableColumn<ExpenseRowData, String> columnNote;
    @FXML
    private TableColumn<ExpenseRowData, String> columnAmount;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new ExpensesModel(this);
        model.initTable();
    }

    @FXML
    public void onClickOKButton() {
        close();
    }

    @FXML
    public void onClickAddButton() {
        showEditExpenseDialog(null);
    }

    @FXML
    public void onClickEditButton() {
        int id = table.getSelectionModel().getSelectedItem().getId();
        showEditExpenseDialog(id);
    }

    @FXML
    public void onClickDeleteButton() {
        Alert questionDialog = new Alert(Alert.AlertType.CONFIRMATION);
        questionDialog.setTitle("Deleting expense");
        questionDialog.setContentText("Are you sure?");
        Optional<ButtonType> result = questionDialog.showAndWait();

        if (result.get() == ButtonType.OK) {
            int id = table.getSelectionModel().getSelectedItem().getId();
            model.deleteSelectedExpense(id);
        }
    }

    private void showEditExpenseDialog(Integer entityId) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/expenses/expense_edit.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            Scene scene = new Scene(pane);
            Stage newStage = new Stage();

            if (entityId == null) {
                newStage.setTitle("Add New Expense");
            } else {
                newStage.setTitle("Edit Expense");
            }

            newStage.setResizable(false);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(stage);
            newStage.setScene(scene);
            ExpenseEditController expenseEditController = fxmlLoader.getController();
            expenseEditController.setStage(newStage);
            expenseEditController.setEntityId(entityId);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public TableView<ExpenseRowData> getTable() {
        return table;
    }

    public void close() {
        stage.hide();
    }

    public TableColumn<ExpenseRowData, String> getColumnDate() {
        return columnDate;
    }

    public TableColumn<ExpenseRowData, String> getColumnType() {
        return columnType;
    }

    public TableColumn<ExpenseRowData, String> getColumnDescription() {
        return columnDescription;
    }

    public TableColumn<ExpenseRowData, String> getColumnNote() {
        return columnNote;
    }

    public TableColumn<ExpenseRowData, String> getColumnAmount() {
        return columnAmount;
    }

    public Button getButtonEdit() {
        return buttonEdit;
    }

    public Button getButtonDelete() {
        return buttonDelete;
    }
}
