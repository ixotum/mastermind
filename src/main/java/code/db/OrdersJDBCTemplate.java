package code.db;

import code.db.order_structure_component.OrderStructureComponentDB_old;
import code.db.order_structure_component.OrderStructureComponentRowDB;
import code.db.order_structure_component.OrderStructureComponentRowDBMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Created by ixotum on 7/7/15
 */
public class OrdersJDBCTemplate {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveNewOrder(OrderDB orderDB) {
        String sql = "INSERT INTO ORDERS(ORDER_ID, NAME, CUSTOMER, ADDRESS, VK, DUE_DATE, EVENT_DATE, DESCRIPTION, NOTES) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderDB.getOrderId(), orderDB.getName(), orderDB.getCustomer(), orderDB.getAddress(),
                orderDB.getVk(), orderDB.getDueDate(), orderDB.getEventDate(), orderDB.getDescription(), orderDB.getNotes());

//        saveNewOrderStructureComponent_old(orderDB.getOrderId(), orderDB.getOrderStructureComponentDBOld());
    }

    private void saveNewOrderStructureComponent_old(int orderId, OrderStructureComponentDB_old orderStructureComponentDBOld) {
        String sql = "INSERT INTO ORDER_STRUCTURE_COMPONENTS(ORDER_ID, POSITION, ITEM, PRICE) VALUES (?, ?, ?, ?)";
        int componentRowCount = orderStructureComponentDBOld.getComponentRowCount();

        for (int rowIndex = 0; rowIndex < componentRowCount; ++rowIndex) {
            OrderStructureComponentRowDB componentRowDB = orderStructureComponentDBOld.getComponentRow(rowIndex);
            jdbcTemplate.update(sql, orderId, rowIndex, componentRowDB.getItem(), componentRowDB.getPrice());
        }
    }

    public List<OrderDB> readAllOrders() {
        String sql = "SELECT * FROM ORDERS";
        List<OrderDB> orderDBList = jdbcTemplate.query(sql, new OrderDBMapper());
        List<Integer> orderIdList = extractOrderIdList(orderDBList);
        /*List<OrderStructureComponentDB_old> orderStructureComponentDBOldList = readAllOrderStructureComponents_old(orderIdList);

        if (!orderStructureComponentDBOldList.isEmpty()) {
            for (OrderDB orderDB : orderDBList) {
                try {
                    OrderStructureComponentDB_old orderStructureComponentDBOld = orderStructureComponentDBOldList.stream().filter(component -> isOrderIdEqual(orderDB.getOrderId(), component.getOrderId())).findFirst().get();
                    orderDB.setOrderStructureComponentDBOld(orderStructureComponentDBOld);
                } catch (NoSuchElementException e) {
                    System.out.println("INFO: orderStructureComponentDBOld is empty!");
                }
            }
        }*/

        return orderDBList;
    }

    private List<OrderStructureComponentDB_old> readAllOrderStructureComponents_old(List<Integer> orderIdList) {
        List<OrderStructureComponentDB_old> orderStructureComponentDBOldList = new ArrayList<>();
        String sql = "SELECT * FROM ORDER_STRUCTURE_COMPONENTS";
        List<OrderStructureComponentRowDB> orderStructureComponentRowDBList = jdbcTemplate.query(sql, new OrderStructureComponentRowDBMapper());

        for (Integer orderId : orderIdList) {
            List<OrderStructureComponentRowDB> filteredRowDBList = orderStructureComponentRowDBList.stream().filter(row -> isOrderIdEqual(row.getOrderId(), orderId)).collect(Collectors.toList());

            if (!filteredRowDBList.isEmpty()) {
                OrderStructureComponentDB_old orderStructureComponentDBOld = new OrderStructureComponentDB_old();
                orderStructureComponentDBOld.setComponentRowList(filteredRowDBList);
                orderStructureComponentDBOldList.add(orderStructureComponentDBOld);
            }
        }

        return orderStructureComponentDBOldList;
    }

    private static boolean isOrderIdEqual(int id0, int id1) {
        return id0 == id1;
    }

    private static List<Integer> extractOrderIdList(List<OrderDB> orderDBList) {
        return orderDBList.stream().map(OrderDB::getOrderId).collect(Collectors.toList());
    }

    public void updateExistedOrder(OrderDB orderDB) {
        String sql = "UPDATE ORDERS SET NAME=?, CUSTOMER=?, ADDRESS=?, VK=?, DUE_DATE=?, EVENT_DATE=?, DESCRIPTION=?, NOTES=? " +
                "WHERE ORDER_ID = ?";
        jdbcTemplate.update(sql, orderDB.getName(), orderDB.getCustomer(), orderDB.getAddress(), orderDB.getVk(),
                orderDB.getDueDate(), orderDB.getEventDate(), orderDB.getDescription(), orderDB.getNotes(), orderDB.getOrderId());

//        deleteOrderStructureComponent_old(orderDB.getOrderId());
//        saveNewOrderStructureComponent_old(orderDB.getOrderId(), orderDB.getOrderStructureComponentDBOld());
    }

    private void deleteOrderStructureComponent_old(int orderId) {
        String sql = "DELETE FROM ORDER_STRUCTURE_COMPONENTS WHERE ORDER_ID = ?";
        jdbcTemplate.update(sql, orderId);
    }
}
