package code.ui.expenses;

import java.time.LocalDate;

/**
 * Created by ixotum on 18.10.15
 */
public class ExpenseRowData {
    private LocalDate localDate;

    public String getDate() {
        return localDate.toString();
    }

    public void setDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
