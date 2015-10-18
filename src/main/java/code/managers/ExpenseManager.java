package code.managers;

import code.Defines;
import code.bus.BusEvent;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.bus.BusEventType;
import code.db.expenses.ExpenseDB;
import code.db.expenses.ExpenseJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

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
}
