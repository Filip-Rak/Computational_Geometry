package pau.pau5.employee;

import com.opencsv.CSVWriter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import pau.pau5.classEmployee.ClassEmployee;
import pau.pau5.classEmployee.ClassEmployeeRepository;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class EmployeeService
{
    // Attributes
    private final EmployeeRepository employeeRepository;
    private final ClassEmployeeRepository classEmployeeRepository;

    // Constructor
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, ClassEmployeeRepository classEmployeeRepository)
    {
        this.employeeRepository = employeeRepository;
        this.classEmployeeRepository = classEmployeeRepository;
    }

    // Methods
    public void writeEmployeesToCSV(List<Employee> employees, String csvFilePath) throws Exception
    {
        try (Writer writer = Files.newBufferedWriter(Paths.get(csvFilePath));
             CSVWriter csvWriter = new CSVWriter(writer))
        {

            // Writing header record
            csvWriter.writeNext(new String[]{"ID", "Name", "Surname", "Condition", "Birth Year", "Salary", "Group"});

            // Writing data records
            for (Employee emp : employees)
            {
                csvWriter.writeNext(new String[]{
                        String.valueOf(emp.getId()),
                        emp.getName(),
                        emp.getSurname(),
                        emp.getEmployeeCondition().toString(),
                        String.valueOf(emp.getBirthYear()),
                        String.format("%.2f", emp.getSalary()),
                        String.valueOf(emp.getClassEmployee().getId())
                });
            }
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    public Employee addEmployee(EmployeeDTO employeeDTO)
    {
        ClassEmployee classEmployee = classEmployeeRepository.findById(employeeDTO.classEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("ClassEmployee with id " + employeeDTO.classEmployeeId() + " not found."));

        Employee newEmployee = new Employee(employeeDTO, classEmployee);

        employeeRepository.save(newEmployee);
        return newEmployee;
    }

    //@Transactional
    public void deleteEmployee(int id)
    {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + id + " not found."));

        // Detaching employee from parent ClassEmployee
        ClassEmployee parent = employee.getClassEmployee();
        parent.getEmployeeList().remove(employee);

        employeeRepository.delete(employee);
    }

    public List<Employee> getAllEmployees()
    {
        return employeeRepository.findAll();
    }
}
