package code.db.expenses;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ixotum on 17.10.15
 */
public class ExpenseDBMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
        ExpenseDB expenseDB = new ExpenseDB();

        expenseDB.setDate(rs.getDate("DATE"));
        expenseDB.setType(rs.getString("TYPE"));
        expenseDB.setDescription(rs.getString("DESCRIPTION"));
        expenseDB.setNote(rs.getString("NOTE"));

        return expenseDB;
    }
}
