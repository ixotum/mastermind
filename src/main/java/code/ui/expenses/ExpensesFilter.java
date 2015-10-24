package code.ui.expenses;

import code.db.expenses.ExpenseDB;
import code.utils.UITools;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ixotum on 20.10.15
 */
public class ExpensesFilter {
    private LocalDate showFrom;
    private LocalDate showTo;

    public void initDateInterval(String dateInterval) {
        showTo = LocalDate.now();

        switch (dateInterval) {
            case "Month" :
                showFrom = showTo.withDayOfMonth(1);
                break;
            default:
                showFrom = showTo = null;
        }
    }

    public List<ExpenseDB> filter(List<ExpenseDB> inputList) {
        List<ExpenseDB> outputList = inputList;

        if (showFrom == null || showTo == null) {
            return outputList;
        }

        outputList = outputList.stream().filter(this::isInsideDateInterval).collect(Collectors.toList());

        return outputList;
    }

    private boolean isInsideDateInterval(ExpenseDB expenseDB) {
        LocalDate expenseDate = UITools.convertToLocalDate(expenseDB.getDate());

        return (expenseDate.isAfter(showFrom) || expenseDate.isEqual(showFrom)) &&
                (expenseDate.isBefore(showTo) || expenseDate.isEqual(showTo));

    }
}
