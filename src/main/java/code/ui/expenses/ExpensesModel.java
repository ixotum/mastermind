package code.ui.expenses;

import code.Main;
import code.bus.BusEvent;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.bus.BusEventType;
import code.db.expenses.ExpenseDB;
import code.managers.ExpenseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.util.List;

/**
 * Created by ixotum on 16.10.15
 */
public class ExpensesModel implements BusEventListener {
    private final ExpensesController controller;

    public ExpensesModel(ExpensesController expensesController) {
        this.controller = expensesController;
    }

    public void initTable() {
        initSizeHandlers();
        initKeyHandlers();
        initBusListener();
        initColumns();
        updateContent();
    }

    private void initBusListener() {
        BusEventManager.addListener(this, BusEventType.EXPENSE_UPDATED);
    }

    private void initColumns() {
        TableColumn<ExpenseRowData, String> columnDate = controller.getColumnDate();
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<ExpenseRowData, String> columnType = controller.getColumnType();
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<ExpenseRowData, String> columnDescription = controller.getColumnDescription();
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void updateContent() {
        ExpenseManager expenseManager = Main.getExpenseManager();
        List<ExpenseDB> expenseDBList = expenseManager.getExpenses();
        TableView<ExpenseRowData> table = controller.getTable();
        ObservableList<ExpenseRowData> tableRows = createExpensesRows(expenseDBList);
        table.getItems().setAll(tableRows);
    }

    private ObservableList<ExpenseRowData> createExpensesRows(List<ExpenseDB> expenseDBList) {
        ObservableList<ExpenseRowData> expenseRowDataList = FXCollections.observableArrayList();

        for (ExpenseDB expenseDB : expenseDBList) {
            ExpenseRowData expenseRowData = new ExpenseRowData();
            expenseRowData.setDate(expenseDB.getDate().toLocalDate());
            expenseRowData.setType(expenseDB.getType());
            expenseRowData.setDescription(expenseDB.getDescription());

            expenseRowDataList.add(expenseRowData);
        }

        return expenseRowDataList;
    }

    private void initKeyHandlers() {
        TableView table = controller.getTable();
        table.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }

    private void initSizeHandlers() {
        ScrollPane scrollPane = controller.getScrollPane();
        scrollPane.heightProperty().addListener((observable, oldValue, newValue) -> scrollPaneHeightChanged(newValue));
        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> scrollPaneWidthChanged(newValue));
    }

    private void scrollPaneWidthChanged(Number newValue) {
        TableView table = controller.getTable();
        table.setPrefWidth(newValue.doubleValue());
    }

    private void scrollPaneHeightChanged(Number newValue) {
        TableView table = controller.getTable();
        table.setPrefHeight(newValue.doubleValue() - 2);
    }

    @Override
    public void busEventDispatched(BusEvent busEvent) {
        updateContent();
    }
}
