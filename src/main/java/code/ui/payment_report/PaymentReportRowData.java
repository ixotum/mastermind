package code.ui.payment_report;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by ixotum on 31.10.15
 */
public class PaymentReportRowData {
    private Date date;
    private int id;
    private String name;
    private BigDecimal payment;

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDate() {
        return date.toString();
    }

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

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public String getPayment() {
        return payment.toString();
    }
}
