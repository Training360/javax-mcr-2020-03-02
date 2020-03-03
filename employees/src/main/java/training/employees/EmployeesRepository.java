package training.employees;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

    public void deleteEmployee(long id) {
        jdbcTemplate.update("delete from employees where id = ?", id);
    }

    public void deleteAll() {
        jdbcTemplate.update("delete from employees");
    }

    public List<Employee> listAllEmployees(String prefix) {
        return jdbcTemplate.query("select id, emp_name from employees where emp_name like ?",
                this::convertEmployee, prefix == null ? "%" : prefix + "%");
    }

    public Employee findEmployeeById(long id) {
        try {
            return jdbcTemplate.queryForObject("select id, emp_name from employees where id = ?",
                    this::convertEmployee,
                    id);
        }
        catch (EmptyResultDataAccessException ee) {
            throw new IllegalArgumentException("Employee not found", ee);
        }
    }

    private Employee convertEmployee(ResultSet resultSet, int i) {
        try {
            var id = resultSet.getLong("id");
            var name = resultSet.getString("emp_name");
            var employee = new Employee(id, name);
            return employee;
        }
        catch (SQLException se) {
            throw new IllegalStateException("Can not convert", se);
        }
    }
}
