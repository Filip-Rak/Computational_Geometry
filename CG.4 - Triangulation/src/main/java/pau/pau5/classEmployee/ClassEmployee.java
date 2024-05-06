package pau.pau5.classEmployee;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import pau.pau5.employee.Employee;
import pau.pau5.rate.Rate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table (name = "classemployee")
public class ClassEmployee
{
    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "maxEmployees")
    private int maxEmployees;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "classEmployee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private final List<Employee> employeeList;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "classEmployee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private final List<Rate> ratingList;

    @Column(name = "workgroup")
    private String workgroup;

    //Constructor
    public ClassEmployee(String workgroup, int maxEmployees)
    {
        this.workgroup = workgroup;
        this.maxEmployees = maxEmployees;
        this.employeeList = new ArrayList<>();
        this.ratingList = new ArrayList<>();
    }

    public ClassEmployee()
    {
        employeeList = new LinkedList<>();
        ratingList = new LinkedList<>();
    }

    public ClassEmployee(ClassEmployeeDTO classEmployeeDTO)
    {
        this.workgroup = classEmployeeDTO.workgroup();
        this.maxEmployees = classEmployeeDTO.maxEmployees();

        this.employeeList = new LinkedList<>();
        this.ratingList = new LinkedList<>();
    }

    //Methods
    public boolean addEmployee(Employee tgt)
    {
        if(employeeList.size() >= maxEmployees)
            return false;

        for (Employee e : employeeList)
        {
            if(e == tgt)
                return false;
        }

        tgt.setClassEmployee(this);
        employeeList.add(tgt);
        return true;
    }

    Employee findEmployee(Employee tgt)
    {
        for (Employee employee : employeeList)
        {
            if (employee == tgt)
                return employee;
        }

        return null;
    }

    public List<Employee> searchPartial(String keyword)
    {
        keyword = keyword.toLowerCase();

        List<Employee> outputList = new ArrayList<>();
        for(Employee employee : this.employeeList)
        {
            if(employee.getName().toLowerCase().contains(keyword) || employee.getSurname().toLowerCase().contains(keyword))
                outputList.add(employee);
        }

        return outputList;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassEmployee that = (ClassEmployee) o;
        return id == that.id; // Equality based on ID
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    //getters
    public List<Employee> getEmployeeList() { return this.employeeList; }
    public List<Rate> getRatingList() { return this.ratingList; }
    public int getMaxEmployees() { return this.maxEmployees; }
    public int getId() { return this.id; }
    public String getWorkgroup() { return this.workgroup; }

    //setters
    public void setWorkgroup(String w) { this.workgroup = w; }
    public void setMax(int m) { this.maxEmployees = m; }

    public int getReviewCount() { return ratingList.size(); }

    public double getAverageScore()
    {
        double avg = 0;
        for(Rate r : ratingList)
            avg += r.getRating();

        if(avg == 0)
            return 0;
        else
            return avg / ratingList.size();
    }
}
