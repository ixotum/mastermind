package code.db;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by ixotum on 7/7/15
 */
public class OrdersJDBCTemplate {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveNewOrder(OrderDB orderDB) {
        String sql = "INSERT INTO ORDERS(ORDER_ID, NAME, STRUCTURE, CUSTOMER, VK, DUE_DATE, EVENT_DATE, DESCRIPTION) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderDB.getOrderId(), orderDB.getName(), orderDB.getStructure(), orderDB.getCustomer(),
                orderDB.getVk(), orderDB.getDueDate(), orderDB.getEventDate(), orderDB.getDescription());
    }

    public List<OrderDB> readAllOrders() {
        String sql = "SELECT * FROM ORDERS";
        return jdbcTemplate.query(sql, new OrderDBMapper());
    }

    public void updateExistedOrder(OrderDB orderDB) {
        String sql = "UPDATE ORDERS SET NAME=?, STRUCTURE=?, CUSTOMER=?, VK=?, DUE_DATE=?, EVENT_DATE=?, DESCRIPTION=? " +
                "WHERE ORDER_ID = ?";
        jdbcTemplate.update(sql, orderDB.getName(), orderDB.getStructure(), orderDB.getCustomer(), orderDB.getVk(),
                orderDB.getDueDate(), orderDB.getEventDate(), orderDB.getDescription(), orderDB.getOrderId());
    }
}
