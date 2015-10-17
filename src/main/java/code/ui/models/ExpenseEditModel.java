package code.ui.models;

import code.Defines;
import code.db.expenses.ExpenseDB;
import code.db.expenses.ExpenseJDBCTemplate;
import code.ui.ExpenseEditController;
import code.utils.UITools;
import javafx.scene.control.DatePicker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by ixotum on 17.10.15
 */
public class ExpenseEditModel {
    private final ExpenseEditController controller;
    private boolean createNew;
    private final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);

    public ExpenseEditModel(ExpenseEditController controller) {
        this.controller = controller;
    }

    public void initDatePicker() {
        DatePicker datePicker = controller.getDatePicker();
        Date todayDate = new Date();
        LocalDate localDate = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        UITools.initDatePicker(datePicker);
        datePicker.setValue(localDate);
    }

    public void setCreateNew(boolean createNew) {
        this.createNew = createNew;
    }

    public void processExpense() {
        if (createNew) {
            ExpenseDB expenseDB = createExpenseDB();
            ExpenseJDBCTemplate expenseJDBCTemplate = (ExpenseJDBCTemplate) applicationContext.getBean("expenseJDBCTemplateId");
            expenseJDBCTemplate.saveNewExpense(expenseDB);
        }
    }

    private ExpenseDB createExpenseDB() {
        ExpenseDB expenseDB = new ExpenseDB();

        LocalDate localDate = controller.getDatePicker().getValue();
        expenseDB.setDate(java.sql.Date.valueOf(localDate));

        return expenseDB;
    }
}
