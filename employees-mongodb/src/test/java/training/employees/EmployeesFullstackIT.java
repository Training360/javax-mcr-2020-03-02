package training.employees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EmployeesFullstackIT {

    @Autowired
    EmployeesController controller;

    @Autowired
    EmployeesService employeesService;

    @BeforeEach
    void clear() {
        employeesService.deleteAll();
    }

    @Test
    void testCreate() {
        var employee = controller.createEmployee(new CreateEmployeeCommand("John Doe"));
        System.out.println(employee.getId());
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
