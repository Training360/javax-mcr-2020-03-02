package training.employees;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeesService {

    private EmployeesRepository employeesRepository;

    private ModelMapper modelMapper;

    public EmployeesService(EmployeesRepository employeesRepository, ModelMapper modelMapper) {
        this.employeesRepository = employeesRepository;
        this.modelMapper = modelMapper;
    }

    public EmployeeDto createEmployee(CreateEmployeeCommand command) {
        var employee = employeesRepository.saveEmployee(new Employee(command.getName()));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    public List<EmployeeDto> listEmployees(String prefix) {

        log.info("Alkalmazottak listazasa");
        log.debug("A parameter: {}", prefix);

        return employeesRepository.listAllEmployees(prefix).stream()
                .filter(e -> prefix == null || e.getName().toLowerCase().startsWith(prefix.toLowerCase()))
                .map(e -> modelMapper.map(e, EmployeeDto.class))
                .collect(Collectors.toList());
    }

    public EmployeeDto findEmployeeById(long id) {
        return modelMapper.map(employeesRepository.findEmployeeById(id),
                EmployeeDto.class);
    }

    public EmployeeDto updateEmployee(long id, UpdateEmployeeCommand command) {
        var employee = employeesRepository.updateEmployee(new Employee(id, command.getName()));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    public void deleteEmployee(long id) {
        employeesRepository.deleteEmployee(id);
    }

    public void deleteAll() {
        employeesRepository.deleteAll();
    }

}
