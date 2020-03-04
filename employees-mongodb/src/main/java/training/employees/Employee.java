package training.employees;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document("employees")
public class Employee {

    @Id
    private String id;

    private String name;

    public Employee(String name) {
        this.name = name;
    }
}
