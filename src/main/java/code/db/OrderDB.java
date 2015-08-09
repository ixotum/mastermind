package code.db;

import java.sql.Date;

/**
 * Created by ixotum on 7/7/15
 */
public class OrderDB {
    private final int orderId;
    private String name;
    private String structure;
    private String customer;
    private String vk;
    private Date dueDate;
    private Date eventDate;
    private String description;

    public OrderDB(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getStructure() {
        return structure;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomer() {
        return customer;
    }

    public void setVK(String vk) {
        this.vk = vk;
    }

    public String getVk() {
        return vk;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
