package pau.pau5.employee;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import pau.pau5.classEmployee.ClassEmployee;

@Entity
@Table (name = "employee")
public class Employee implements Comparable<Employee>
{
    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "employeeCondition")
    private EmployeeCondition employeeCondition;

    @Column(name = "birthYear")
    private int birth_year;

    @Column(name = "salary")
    private double salary;

    @ManyToOne
    @JoinColumn(name = "class_employee_id")
    @JsonBackReference
    private ClassEmployee classEmployee;

    //Constructor
    public Employee(String name, String surname, EmployeeCondition ec, int birth_year, double salary, ClassEmployee classEmployee)
    {
        this.name = name;
        this.surname = surname;
        this.employeeCondition = ec;
        this.birth_year = birth_year;
        this.salary = salary;
        this.classEmployee = classEmployee;
    }

    public Employee(EmployeeDTO employeeDTO, ClassEmployee classEmployee)
    {
        this.name = employeeDTO.name();
        this.surname = employeeDTO.surname();
        this.employeeCondition = employeeDTO.employeeCondition();
        this.birth_year = employeeDTO.birthYear();
        this.salary = employeeDTO.salary();
        this.classEmployee = classEmployee;
    }

    public Employee()
    {

    }

    //Methods
    void printing()
    {
        System.out.println("-------------------------------------------");
        System.out.println("Imie: " + name + ", nazwisko: " + surname);
        System.out.println("Stan: " + employeeCondition);
        System.out.println("Rok urodzenia: " + birth_year);
        System.out.println("Wynagrodzenie: " + salary);
    }

    void printing(String prefix)
    {
        System.out.println(prefix + "-------------------------------------------");
        System.out.println(prefix + "Imie: " + name + ", nazwisko: " + surname);
        System.out.println(prefix + "Stan: " + employeeCondition);
        System.out.println(prefix + "Rok urodzenia: " + birth_year);
        System.out.println(prefix + "Wynagrodzenie: " + salary);
    }

    public int compareTo(Employee o) { return this.surname.compareTo(o.surname); }

    //Getters
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public EmployeeCondition getEmployeeCondition() { return employeeCondition; }
    public double getSalary() { return salary; }
    public int getBirthYear() { return birth_year; }
    public ClassEmployee getClassEmployee() { return this.classEmployee; }
    public int getId() { return this.id; }

    //Setter
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setCondition(EmployeeCondition condition) { this.employeeCondition = condition; }
    public void setBirth_year(int birth_year) { this.birth_year = birth_year; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setClassEmployee(ClassEmployee ce) { this.classEmployee = ce; }
}