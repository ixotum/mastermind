package code.db;

import code.db.order_structure_component.OrderStructureComponentDB;

import java.sql.Date;

/**
 * Created by ixotum on 7/7/15
 */
public class OrderDB {
    private final int orderId;
    private String name;
    @Deprecated
    private OrderStructureComponentDB orderStructureComponentDBOld;
    private OrderStructureComponentDB orderStructureComponentDB;
    private String customer;
    private String vk;
    private Date dueDate;
    private Date eventDate;
    private String description;
    private String notes;
    private String address;

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

    public void setOrderStructureComponentDBOld(OrderStructureComponentDB orderStructureComponentDBOld) {
        this.orderStructureComponentDBOld = orderStructureComponentDBOld;
    }

    @Deprecated
    public OrderStructureComponentDB getOrderStructureComponentDBOld() {
        return orderStructureComponentDBOld;
    }

    public void setOrderStructureComponentDB(OrderStructureComponentDB orderStructureComponentDB) {
        this.orderStructureComponentDB = orderStructureComponentDB;
    }

    public OrderStructureComponentDB getOrderStructureComponentDB() {
        return orderStructureComponentDB;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomer() {
        return customer;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
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

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }
}
