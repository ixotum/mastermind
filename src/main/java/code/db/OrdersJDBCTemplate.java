package code.db;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by ixotum on 7/7/15
 */
public class OrdersJDBCTemplate {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveNewOrder(OrderDB orderDB) {
        String sql = "INSERT INTO ORDERS(ORDER_ID) " +
                "VALUES (?)";
        jdbcTemplate.update(sql, orderDB.getOrderId());
    }
}
