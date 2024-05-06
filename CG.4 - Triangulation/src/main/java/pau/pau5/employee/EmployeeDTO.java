package pau.pau5.employee;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeDTO(
        @NotBlank(message = "Name cant be blank.")
        String name,

        @NotBlank(message = "Surname cant be blank.")
        String surname,

        @NotNull(message = "Employee condition cant be blank.")
        EmployeeCondition employeeCondition,

        @NotNull(message = "Birth year cant be null.")
        int birthYear,

        @NotNull(message = "Salary cant be null.")
        @Min(value = 0, message = "Salary cant be below zero")
        double salary,

        @NotNull(message = "Every employee has to be asigned to a group")
        int classEmployeeId
) { }