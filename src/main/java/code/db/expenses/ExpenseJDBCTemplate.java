package code.db.expenses;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by ixotum on 17.10.15
 */
public class ExpenseJDBCTemplate {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveNewExpense(ExpenseDB expenseDB) {
        String sql = "INSERT INTO EXPENSE(DATE) " +
                "VALUES (?)";
        jdbcTemplate.update(sql, expenseDB.getDate());
    }
}
