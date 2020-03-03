package training.employees;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeesContainerIT {

    @Autowired
    TestRestTemplate template;


    @Test
    void testCreateEmployee() {
        template.postForObject("/api/employees",
                new CreateEmployeeCommand("John Smith"),
                EmployeeDto.class);

        List<EmployeeDto> employees =
                template.exchange("/api/employees", HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<EmployeeDto>>() {}).getBody();

        assertThat(employees)
                .extracting(EmployeeDto::getName)
                .containsExactly("John Doe", "Jane Doe", "John Smith");
    }
}
