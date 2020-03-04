package training.employees;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeesRepository extends MongoRepository<Employee, String> {
}
