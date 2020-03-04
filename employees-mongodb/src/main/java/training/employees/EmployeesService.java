package training.employees;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeesService {

    private EmployeesRepository repository;

    private ModelMapper modelMapper;

    public EmployeesService(EmployeesRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public EmployeeDto createEmployee(CreateEmployeeCommand command) {
        var employee = repository.save(new Employee(command.getName()));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    public List<EmployeeDto> listEmployees(String prefix) {

        log.info("Alkalmazottak listazasa");
        log.debug("A parameter: {}", prefix);

        return repository.findAll().stream()
                .map(e -> modelMapper.map(e, EmployeeDto.class))
                .collect(Collectors.toList());
    }

    public EmployeeDto findEmployeeById(String id) {
        return modelMapper.map(repository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Employee not found")),
                EmployeeDto.class);
    }

    public EmployeeDto updateEmployee(String id, UpdateEmployeeCommand command) {
        var employee = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        employee.setName(command.getName());
        return modelMapper.map(employee, EmployeeDto.class);
    }

    public void deleteEmployee(String id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

}
