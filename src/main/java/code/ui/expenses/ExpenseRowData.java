package code.ui.expenses;

import java.time.LocalDate;

/**
 * Created by ixotum on 18.10.15
 */
public class ExpenseRowData {
    private LocalDate localDate;
    private String type;
    private String description;

    public String getDate() {
        return localDate.toString();
    }

    public void setDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
