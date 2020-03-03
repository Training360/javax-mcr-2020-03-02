package training.employees;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class EmployeesRepository {

    private JdbcTemplate jdbcTemplate;

    public EmployeesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Employee saveEmployee(Employee employee) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps =
                            con.prepareStatement("insert into employees(emp_name) values (?)",
                                    Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, employee.getName());
                    return ps;
                }, keyHolder);

        employee.setId(keyHolder.getKey().longValue());
        return employee;
    }

    public Employee updateEmployee(Employee employee) {
        jdbcTemplate.update("update employees set emp_name = ? where id = ?",
                employee.getName(), employee.getId());
        return employee;
    }
}
