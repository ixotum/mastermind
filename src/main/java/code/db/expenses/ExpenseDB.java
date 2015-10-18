package code.db.expenses;

import java.sql.Date;

/**
 * Created by ixotum on 17.10.15
 */
public class ExpenseDB {
    private Date date;
    private String type;

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
}
