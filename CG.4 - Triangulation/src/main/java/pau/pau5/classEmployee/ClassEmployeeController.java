package pau.pau5.classEmployee;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pau.pau5.employee.Employee;

import java.util.List;

@RestController
@RequestMapping(path = "api/group")
public class ClassEmployeeController
{
    // Attributes
    private final ClassEmployeeService classEmployeeService;

    // Constructor
    @Autowired
    public ClassEmployeeController(ClassEmployeeService classEmployeeService)
    {
        this.classEmployeeService = classEmployeeService;
    }

    // Methods
    @GetMapping
    public ResponseEntity<?> getGroups()
    {
        List<ClassEmployee> list = classEmployeeService.getGroups();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<ClassEmployee> addGroup(@Valid @RequestBody ClassEmployeeDTO group)
    {
        ClassEmployee savedGroup = classEmployeeService.addGroup(group);
        return new ResponseEntity<>(savedGroup, HttpStatus.CREATED);
    }

    @DeleteMapping(path = ":{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable int id)
    {
        classEmployeeService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = ":{id}/employee")
    public ResponseEntity<?> getEmployees(@PathVariable int id)
    {
        List<Employee> list =  classEmployeeService.getEmployees(id);
        return ResponseEntity.ok(list);
    }

    @GetMapping(path = ":{id}/fill")
    public ResponseEntity<?> getUtilization(@PathVariable int id)
    {
        double utilization = classEmployeeService.getUtilization(id);
        return ResponseEntity.ok(utilization); // Returns HTTP 200 with utilization as the body
    }
}
