package code.ui.balance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import code.Main;
import code.managers.IncomeManager;
import code.ui.BalanceScreenController;

/**
 * Created by ixotum on 08.11.15
 */
public class BalanceScreenModel {
    private final BalanceScreenController controller;
    private List<String> months = Arrays.asList("January", "February", "Mart", "April", "May", "June", "July", "August",
            "September", "October", "November", "December");

    public BalanceScreenModel(BalanceScreenController controller) {
        this.controller = controller;
    }

    public void initTable() {
        initKeyHandlers();
        initColumns();
    }

    private void initColumns() {
        controller.getColumnMonth().setCellValueFactory(new PropertyValueFactory<>("month"));
        controller.getColumnIncome().setCellValueFactory(new PropertyValueFactory<>("income"));
        controller.getColumnExpenses().setCellValueFactory(new PropertyValueFactory<>("expenses"));
        controller.getColumnBalance().setCellValueFactory(new PropertyValueFactory<>("balance"));
    }

    public void updateContent() {
        ObservableList<BalanceRowData> tableRows = FXCollections.observableArrayList();
        int year = controller.getComboYear().getValue();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal totalBalance = BigDecimal.ZERO;

        for (int month = 1; month <= 12; ++month) {
            BalanceRowData balanceRowData = new BalanceRowData(month);
            balanceRowData.setMonth(months.get(month - 1));

            BigDecimal income = IncomeManager.getIncome(year, month);
            balanceRowData.setIncome(income);
            totalIncome = totalIncome.add(income);

            BigDecimal expenses = Main.getExpenseManager().getExpenses(year, month);
            balanceRowData.setExpenses(expenses);
            totalExpenses = totalExpenses.add(expenses);

            BigDecimal balance = income.subtract(expenses);
            balanceRowData.setBalance(balance);
            totalBalance = totalBalance.add(balance);

            tableRows.add(balanceRowData);
        }

        BalanceRowData totalRowData = new BalanceRowData(13);
        totalRowData.setMonth("Total:");
        totalRowData.setIncome(totalIncome);
        totalRowData.setExpenses(totalExpenses);
        totalRowData.setBalance(totalBalance);

        tableRows.add(totalRowData);

        controller.getTable().getItems().setAll(tableRows);
    }

    private void initKeyHandlers() {
        controller.getTable().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }

    public void initCombo() {
        controller.getComboYear().setValue(LocalDate.now().getYear());
        List<Integer> incomeYears = IncomeManager.findIncomeYears();
        List<Integer> expenseYears = Main.getExpenseManager().findExpenseYears();
        List<Integer> mergedYears = mergeArrays(incomeYears, expenseYears);
        mergedYears.sort((o1, o2) -> {
            if (o1 > o2) {
                return 1;
            }
            return -1;
        });

        controller.getComboYear().setItems(FXCollections.observableArrayList(mergedYears));
        controller.getComboYear().valueProperty().addListener(observable -> updateContent());
    }

    private List<Integer> mergeArrays(List<Integer> input1, List<Integer> input2) {
        input2.stream().filter(i2 -> !input1.stream().filter(i1 -> Objects.equals(i1, i2)).findFirst().isPresent()).forEach(input1::add);
        return input1;
    }
}
