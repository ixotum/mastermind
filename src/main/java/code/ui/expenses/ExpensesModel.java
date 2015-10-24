package code.ui.expenses;

import code.Defines;
import code.Main;
import code.bus.BusEvent;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.bus.BusEventType;
import code.db.expenses.ExpenseDB;
import code.db.expenses.ExpenseJDBCTemplate;
import code.managers.ExpenseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ixotum on 16.10.15
 */
public class ExpensesModel implements BusEventListener {
    private final ExpensesController controller;
    private List<String> dateIntervals = Arrays.asList("Month", "All Time");

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

    private void applySortOrder() {
        TableView<ExpenseRowData> tableView = controller.getTable();
        TableColumn<ExpenseRowData, String> columnDate = controller.getColumnDate();
        columnDate.setSortType(TableColumn.SortType.DESCENDING);
        tableView.getSortOrder().add(columnDate);
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

        TableColumn<ExpenseRowData, String> columnNote = controller.getColumnNote();
        columnNote.setCellValueFactory(new PropertyValueFactory<>("note"));

        TableColumn<ExpenseRowData, String> columnAmount = controller.getColumnAmount();
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private void updateContent() {
        ExpenseManager expenseManager = Main.getExpenseManager();
        List<ExpenseDB> expenseDBList = expenseManager.getExpenses();
        ExpensesFilter expensesFilter = createExpensesFilter();
        List<ExpenseDB> expenseDBListFiltered = expensesFilter.filter(expenseDBList);
        TableView<ExpenseRowData> table = controller.getTable();
        ObservableList<ExpenseRowData> tableRows = createExpensesRows(expenseDBListFiltered);
        table.getItems().setAll(tableRows);
        applySortOrder();
    }

    private ExpensesFilter createExpensesFilter() {
        String dateInterval = controller.getComboShowFor().getValue();

        if (dateInterval == null) {
            dateInterval = dateIntervals.get(0);
        }

        ExpensesFilter expensesFilter = new ExpensesFilter();
        expensesFilter.initDateInterval(dateInterval);
        return expensesFilter;
    }

    private ObservableList<ExpenseRowData> createExpensesRows(List<ExpenseDB> expenseDBList) {
        ObservableList<ExpenseRowData> expenseRowDataList = FXCollections.observableArrayList();

        for (ExpenseDB expenseDB : expenseDBList) {
            ExpenseRowData expenseRowData = new ExpenseRowData();
            expenseRowData.setId(expenseDB.getId());
            expenseRowData.setDate(expenseDB.getDate().toLocalDate());
            expenseRowData.setType(expenseDB.getType());
            expenseRowData.setDescription(expenseDB.getDescription());
            expenseRowData.setNote(expenseDB.getNote());
            expenseRowData.setAmount(expenseDB.getAmount());

            expenseRowDataList.add(expenseRowData);
        }

        return expenseRowDataList;
    }

    private void initKeyHandlers() {
        TableView<ExpenseRowData> table = controller.getTable();
        table.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                controller.getButtonEdit().setDisable(false);
                controller.getButtonDelete().setDisable(false);
            } else {
                controller.getButtonEdit().setDisable(true);
                controller.getButtonDelete().setDisable(true);
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

    public void deleteSelectedExpense(int expenseId) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
        ExpenseJDBCTemplate expenseJDBCTemplate = (ExpenseJDBCTemplate) applicationContext.getBean("expenseJDBCTemplateId");
        expenseJDBCTemplate.delete(expenseId);
        BusEventManager.dispatch(new BusEvent(BusEventType.EXPENSE_UPDATED, null));
    }

    public void initComboShowFor() {
        ComboBox<String> comboShowFor = controller.getComboShowFor();
        comboShowFor.setItems(FXCollections.observableArrayList(dateIntervals));
        comboShowFor.setValue(dateIntervals.get(0));
        comboShowFor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateContent());
    }
}
