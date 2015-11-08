package code.ui.search_result;

import java.sql.Date;

/**
 * Created by ixotum on 08.11.15
 */
public class SearchResultRowData {
    private int id;
    private String name;
    private Date dueDate;

    public void setId(int id) {
        this.id = id;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDate() {
        return dueDate.toString();
    }

    public int getIdNumeric() {
        return id;
    }
}
