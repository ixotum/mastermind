package code.db.expenses;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by ixotum on 17.10.15
 */
public class ExpenseJDBCTemplate {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveNewExpense(ExpenseDB expenseDB) {
        String sql = "INSERT INTO EXPENSE(DATE, TYPE, DESCRIPTION, NOTE) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, expenseDB.getDate(), expenseDB.getType(), expenseDB.getDescription(), expenseDB.getNote());
    }

    public List<ExpenseDB> readAllExpenses() {
        String sql = "SELECT * FROM EXPENSE";
        List<ExpenseDB> expenseDBList = jdbcTemplate.query(sql, new ExpenseDBMapper());
        return expenseDBList;
    }
}
