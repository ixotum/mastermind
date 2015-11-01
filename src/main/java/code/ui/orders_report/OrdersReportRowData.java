package code.ui.orders_report;

import java.math.BigDecimal;

/**
 * Created by ixotum on 01.11.15
 */
public class OrdersReportRowData {
    private int id;
    private String name;
    private BigDecimal total;
    private BigDecimal paid;
    private BigDecimal due;

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

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getTotal() {
        return total.toString();
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public String getPaid() {
        return paid.toString();
    }

    public void setDue(BigDecimal due) {
        this.due = due;
    }

    public String getDue() {
        return due.toString();
    }
}
