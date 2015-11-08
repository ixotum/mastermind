package code.ui.balance;

import java.math.BigDecimal;

/**
 * Created by ixotum on 08.11.15
 */
public class BalanceRowData {
    private String month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal balance;

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonth() {
        return month;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public String getIncome() {
        return income.toString();
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public String getExpenses() {
        return expenses.toString();
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance.toString();
    }
}
