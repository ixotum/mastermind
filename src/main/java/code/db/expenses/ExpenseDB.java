package code.db.expenses;

import java.sql.Date;

/**
 * Created by ixotum on 17.10.15
 */
public class ExpenseDB {
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
