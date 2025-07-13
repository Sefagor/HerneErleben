package de.stadt.herne.eventApp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String herneID;


    @NotNull
    private LocalDate eventDate;

    @NotNull
    private LocationDTO eventLocationDTO;

    @Min(value = 0)
    private int maxParticipant;

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
