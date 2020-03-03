package training.employees;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeesContainerIT {

    @Autowired
    TestRestTemplate template;


    @Test
    void testCreateEmployee() {
        template.postForObject("/api/employees",
                new CreateEmployeeCommand("John Doe"),
                EmployeeDto.class);

        List<EmployeeDto> employees =
                template.getForObject("/api/employees", List.class);
        System.out.println(employees);
    }
}
