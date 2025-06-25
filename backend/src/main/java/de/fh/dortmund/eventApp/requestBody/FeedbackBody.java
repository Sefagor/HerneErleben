package de.fh.dortmund.eventApp.requestBody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Valid
@Data
public class FeedbackBody {

    @NotNull
    private long eventId;

    @Min(value = 1)
    @Max(value = 5)
    private int rating;

    @NotNull
    private String comment;

}
