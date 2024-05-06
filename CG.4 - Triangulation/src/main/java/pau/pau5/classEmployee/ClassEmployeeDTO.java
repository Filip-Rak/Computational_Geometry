package pau.pau5.classEmployee;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClassEmployeeDTO(
        @NotBlank(message = "Name is required.")
        String workgroup,

        @NotNull(message = "Maximum number of employees is required.")
        @Min(value = 1, message = "Maximum number of employees must be at least 1.")
        int maxEmployees
) { }
