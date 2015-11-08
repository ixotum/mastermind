package code.managers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import code.Defines;
import code.bus.BusEvent;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.bus.BusEventType;
import code.db.expenses.ExpenseDB;
import code.db.expenses.ExpenseJDBCTemplate;

/**
 * Created by ixotum on 17.10.15
 */
public class ExpenseManager implements BusEventListener {
    private List<ExpenseDB> expenses = new ArrayList<>();

    public ExpenseManager() {
        updateExpenses();
        BusEventManager.addListener(this, BusEventType.EXPENSE_UPDATED);
    }

    private void updateExpenses() {
        expenses = readAllExpensesFromDB();
    }

    private static List<ExpenseDB> readAllExpensesFromDB() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
        ExpenseJDBCTemplate expenseJDBCTemplate = (ExpenseJDBCTemplate) applicationContext.getBean("expenseJDBCTemplateId");
        return expenseJDBCTemplate.readAllExpenses();
    }

    @Override
    public void busEventDispatched(BusEvent busEvent) {
        if (busEvent.getType() == BusEventType.EXPENSE_UPDATED) {
            updateExpenses();
        }
    }

    public List<ExpenseDB> getExpenses() {
        return expenses;
    }

    public static ExpenseDB find(Integer entityId) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
        ExpenseJDBCTemplate expenseJDBCTemplate = (ExpenseJDBCTemplate) applicationContext.getBean("expenseJDBCTemplateId");
        return expenseJDBCTemplate.findById(entityId);
    }

    public BigDecimal getExpenses(int year, int month) {
        List<ExpenseDB> monthExpenses = expenses.stream().filter(expenseDB -> isInMonth(expenseDB.getDate().toLocalDate(), year, month)).collect(Collectors.toList());
        return monthExpenses.stream().map(ExpenseDB::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isInMonth(LocalDate date, int year, int month) {
        return date.getYear() == year && date.getMonthValue() == month;
    }

    public List<Integer> findExpenseYears() {
        List<Integer> expenseYears = new ArrayList<>();
        expenses.stream().forEach(expense -> updateExpenseYears(expense.getDate().toLocalDate().getYear(), expenseYears));
        return expenseYears;
    }

    private void updateExpenseYears(int year, List<Integer> expenseYears) {
        for (Integer existedYear : expenseYears) {
            if (existedYear == year) {
                return;
            }
        }

        expenseYears.add(year);
    }
}
