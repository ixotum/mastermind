package code.ui.balance;

import javafx.scene.control.Label;

import java.math.BigDecimal;

/**
 * Created by ixotum on 08.11.15
 */
public class BalanceRowData {
    private final int id;
    private String month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal balance;

    public BalanceRowData(int id) {
        this.id = id;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Label getMonth() {
        Label label = new Label(month);

        if (isLastRow()) {
            label.getStyleClass().add("label_bold");
        }

        return label;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public Label getIncome() {
        Label label = new Label(income.toString());

        if (isLastRow()) {
            label.getStyleClass().add("label_bold");
        }

        return label;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public Label getExpenses() {
        Label label = new Label(expenses.toString());

        if (isLastRow()) {
            label.getStyleClass().add("label_bold");
        }

        return label;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Label getBalance() {
        Label label = new Label(balance.toString());

        if (isLastRow()) {
            label.getStyleClass().add("label_bold");
        }

        return label;
    }

    private boolean isLastRow() {
        return id == 13;
    }
}
