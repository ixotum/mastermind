package code.db.order.payment_component;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ixotum on 04.10.15
 */
public class PaymentDBMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
        PaymentDB paymentDB = new PaymentDB();
        paymentDB.setOrderId(rs.getInt("ORDER_ID"));
        paymentDB.setDate(rs.getDate("DATE"));
        paymentDB.setPayment(rs.getBigDecimal("PAYMENT"));
        return paymentDB;
    }
}
