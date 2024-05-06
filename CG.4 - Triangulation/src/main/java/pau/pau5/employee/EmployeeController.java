package pau.pau5.employee;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(path = "api/employee")
public class EmployeeController
{
    // Attributes
    private final EmployeeService employeeService;

    // Constructor
    @Autowired
    public EmployeeController(EmployeeService employeeService)
    {
        this.employeeService = employeeService;
    }

    // Methods
    @GetMapping(path = "csv")
    public ResponseEntity<?> downloadEmployeeCSV()
    {
        try
        {
            String filename = "employees.csv";
            Path path = Paths.get(filename);
            employeeService.writeEmployeesToCSV(employeeService.getAllEmployees(), filename);

            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
        catch (Exception e)
        {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error downloading the file: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO)
    {
        Employee employee = employeeService.addEmployee(employeeDTO);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @DeleteMapping(path = ":{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id)
    {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }
}
