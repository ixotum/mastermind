package code.ui.expenses;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by ixotum on 18.10.15
 */
public class ExpenseRowData {
    private LocalDate localDate;
    private String type;
    private String description;
    private String note;
    private BigDecimal amount;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAmount() {
        return amount.toString();
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
