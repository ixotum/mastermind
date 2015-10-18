package code.ui.models;

import code.Defines;
import code.bus.BusEvent;
import code.bus.BusEventManager;
import code.bus.BusEventType;
import code.db.expenses.ExpenseDB;
import code.db.expenses.ExpenseJDBCTemplate;
import code.managers.ExpenseManager;
import code.ui.ExpenseEditController;
import code.utils.UITools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by ixotum on 17.10.15
 */
public class ExpenseEditModel {
    private final ExpenseEditController controller;
    private final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
    private Integer entityId;

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

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
        if (entityId != null) {
            ExpenseDB expenseDB = ExpenseManager.find(entityId);
            initDialog(expenseDB);
        }
    }

    private void initDialog(ExpenseDB expenseDB) {
        controller.getDatePicker().setValue(expenseDB.getDate().toLocalDate());
        controller.getComboType().setValue(expenseDB.getType());
        controller.getTextFieldDescription().setText(expenseDB.getDescription());
        controller.getTextFieldNote().setText(expenseDB.getNote());
        controller.getTextFieldAmount().setText(expenseDB.getAmount().toString());
    }

    public void processExpense() {
        ExpenseJDBCTemplate expenseJDBCTemplate = (ExpenseJDBCTemplate) applicationContext.getBean("expenseJDBCTemplateId");
        ExpenseDB expenseDB = createExpenseDB();

        if (entityId == null) {
            expenseJDBCTemplate.saveNewExpense(expenseDB);
        } else {
            expenseJDBCTemplate.updateExpense(expenseDB, entityId);
        }

        BusEventManager.dispatch(new BusEvent(BusEventType.EXPENSE_UPDATED, null));
        controller.close();
    }

    private ExpenseDB createExpenseDB() {
        ExpenseDB expenseDB = new ExpenseDB();

        LocalDate localDate = controller.getDatePicker().getValue();
        expenseDB.setDate(java.sql.Date.valueOf(localDate));
        expenseDB.setType(controller.getComboType().getValue());
        expenseDB.setDescription(controller.getTextFieldDescription().getText());
        expenseDB.setNote(controller.getTextFieldNote().getText());

        String amountString = controller.getTextFieldAmount().getText();
        BigDecimal amount;
        if (amountString.isEmpty()) {
            amount = BigDecimal.ZERO;
        } else {
            amount = new BigDecimal(amountString);
        }

        expenseDB.setAmount(amount);

        return expenseDB;
    }

    public void initComboType() {
        ComboBox<String> comboType = controller.getComboType();
        ObservableList<String> expenseTypes = FXCollections.observableArrayList(Defines.expenseTypes);
        comboType.setItems(expenseTypes);
        comboType.setValue(expenseTypes.get(expenseTypes.size() - 1));
    }

    public void initTextFieldAmount() {
        TextField textFieldAmount = controller.getTextFieldAmount();
        Button buttonOK = controller.getButtonOK();
        textFieldAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (UITools.isCurrency(newValue)) {
                textFieldAmount.getStyleClass().removeAll("textFieldCurrencyError");
                buttonOK.setDisable(false);
            } else {
                textFieldAmount.getStyleClass().add("textFieldCurrencyError");
                buttonOK.setDisable(true);
            }
        });
    }
}
