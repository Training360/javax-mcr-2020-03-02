package training.employees;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataEmployeesRepository extends
        JpaRepository<Employee, Long> {
}
