package de.fh.dortmund.eventApp.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;
    private String metadata;

    private String token;
    private String role;
    private String expirationTime;
    private String bookingConfirmationCode;

    private UserDTO user;

  //  private EventDTO event;
  //private BookingDTO booking;
    private List<UserDTO> userList;
    //private List<EventDTO> eventList;
    // private List<BookingDTO> bookingList;


}
