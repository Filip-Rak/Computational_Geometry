package pau.pau5.classEmployee;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pau.pau5.employee.Employee;

import java.util.List;

@Service
public class ClassEmployeeService
{
    // Attributes
    private final ClassEmployeeRepository classEmployeeRepository;

    // Constructor
    @Autowired
    public ClassEmployeeService(ClassEmployeeRepository classEmployeeRepository)
    {
        this.classEmployeeRepository = classEmployeeRepository;
    }

    // Methods
    public List<ClassEmployee> getGroups()
    {
        List<ClassEmployee> list = classEmployeeRepository.findAll();

        if (list.isEmpty())
            throw new EntityNotFoundException("There are no groups");

        return list;
    }

    public ClassEmployee addGroup(ClassEmployeeDTO classEmployeeDTO)
    {
        ClassEmployee newGroup = new ClassEmployee(classEmployeeDTO);
        return classEmployeeRepository.save(newGroup);
    }

    public void deleteGroup(int id)
    {
        ClassEmployee classEmployee = classEmployeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + id + " not found."));

        classEmployeeRepository.deleteById(id);
    }

    public List<Employee> getEmployees(int id)
    {
        ClassEmployee classEmployee = classEmployeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + id + " not found."));


        return classEmployee.getEmployeeList();
    }

    public double getUtilization(int id)
    {
        ClassEmployee classEmployee =  classEmployeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + id + " not found."));

        double max = classEmployee.getMaxEmployees();
        double size = classEmployee.getEmployeeList().size();

        return size / max;
    }
}
