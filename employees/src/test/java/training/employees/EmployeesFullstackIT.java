package training.employees;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql(statements = "delete from employees")
public class EmployeesFullstackIT {

    @Autowired
    EmployeesController controller;

    @MockBean
    EventStoreGateway eventStoreGateway;

    @Test
    void testCreate() {
        var employee = controller.createEmployee(new CreateEmployeeCommand("John Doe"));
        assertEquals("John Doe",
                ((EmployeeDto)controller.findEmployeeById(employee.getId()).getBody()).getName());
    }

    @Test
    void testDelete() {
        controller.createEmployee(new CreateEmployeeCommand("John Doe"));
        var employee = controller.createEmployee(new CreateEmployeeCommand("Jane Doe"));

        controller.deleteEmployee(employee.getId());

        assertEquals(1, controller.listEmployees(null).size());
    }
}
