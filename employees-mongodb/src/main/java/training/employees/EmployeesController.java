package training.employees;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {

    private EmployeesService employeesService;

    public EmployeesController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @GetMapping
    public List<EmployeeDto> listEmployees(@RequestParam(required = false) String prefix) {
        return employeesService.listEmployees(prefix);
    }

    @GetMapping("/{id}")
    public ResponseEntity findEmployeeById(@PathVariable String id) {
//        try {
            return ResponseEntity.ok(employeesService.findEmployeeById(id));
//        }
//        catch (IllegalArgumentException iae) {
//            return ResponseEntity.notFound().build();
//        }
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleNotFound() {
        System.out.println("Employee not found");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDto createEmployee(@Valid @RequestBody CreateEmployeeCommand command) {
        return employeesService.createEmployee(command);
    }
    @PostMapping("/{id}")
    public EmployeeDto updateEmployee(@PathVariable("id") String id,
                                      @RequestBody UpdateEmployeeCommand command) {
        return employeesService.updateEmployee(id, command);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable("id") String id) {
        employeesService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

}
