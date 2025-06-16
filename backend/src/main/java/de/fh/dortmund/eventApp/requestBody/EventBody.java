package de.fh.dortmund.eventApp.requestBody;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.fh.dortmund.eventApp.dto.CategoryDTO;
import de.fh.dortmund.eventApp.entity.Location;
import de.fh.dortmund.eventApp.entity.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Valid
public class EventBody {


    @NotNull
    private Status status;

    @NotNull
    private LocalDate eventDate;

    @NotNull
    private Location eventLocation;

    @Min(value = 0)
    private int maxParticipant;

    @NotNull
    private String herneID;

    @NotNull
    private String eventPhoto;

    @NotNull
    private String eventDescription;

    @NotNull
    private String eventName;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @NotEmpty
    private List<CategoryDTO> categories = new ArrayList<>();
}
