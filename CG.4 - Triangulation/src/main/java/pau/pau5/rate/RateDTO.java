package pau.pau5.rate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RateDTO(
        @NotNull(message = "Rating cant be NULL.")
        @Min(value = Rate.LOWER_BOUND, message = "Rating cant be lower than " + Rate.LOWER_BOUND)
        @Max(value = Rate.UPPER_BOUND, message = "Rating cant be higher than " + Rate.UPPER_BOUND)
        int rating,

        @NotNull(message = "Group has to be specified")
        int classEmployeeId,

        String comment
) { }
