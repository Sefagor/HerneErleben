package de.fh.dortmund.eventApp.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import de.fh.dortmund.eventApp.entity.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDTO {

    private Long id;
    private String eventPhoto;
    private String eventDescription;
    private LocalDate eventDate;
    private LocationDTO eventLocation;
    private int maxParticipant;
    private Status status;
    private String eventName;
    private List<BookingDTO> bookings;
    private float averageRating;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<CategoryDTO> categories;

}
