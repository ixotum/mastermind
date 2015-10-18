package code.db.expenses;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by ixotum on 17.10.15
 */
public class ExpenseDB {
    private Date date;
    private String type;
    private String description;
    private String note;
    private BigDecimal amount;

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
