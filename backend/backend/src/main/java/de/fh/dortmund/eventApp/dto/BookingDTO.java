package de.fh.dortmund.eventApp.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import de.fh.dortmund.eventApp.entity.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {


    private Long id;
    private LocalDate bookingDate;
    private Status status;
    private String bookingConfirmationCode;
    private UserDTO user;
    private EventDTO event;

}
