package training.employees;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeesRepositoryIT {

    @Autowired
    DataEmployeesRepository repository;

    @Test
    public void testCreate() {
        repository.save(new Employee("John Doe"));
        assertThat(repository.findAll())
                .extracting(Employee::getName)
                .containsExactly("John Doe");
    }
}
