package de.fh.dortmund.eventApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private final int statusCode;
    private final String message;
    private final String metadata;

    private final String token;
    private final String role;
    private final String expirationTime;
    private final String bookingConfirmationCode;

    private final UserDTO user;
    private final EventDTO event;
    private final BookingDTO booking;
    private final List<UserDTO> userList;
    private final List<EventDTO> eventList;
    private final List<BookingDTO> bookingList;

    /**
     * public Response (int statusCode,
     * String message,
     * String metadata,
     * String token,
     * String role,
     * String expirationTime,
     * String bookingConfirmationCode,
     * UserDTO user,
     * EventDTO event,
     * BookingDTO booking,
     * List<UserDTO> userList,
     * List<EventDTO> eventList,
     * List<BookingDTO> bookingList) {
     * this.statusCode = statusCode;
     * this.message = message;
     * this.metadata = metadata;
     * this.token = token;
     * this.role = role;
     * this.expirationTime = expirationTime;
     * this.bookingConfirmationCode = bookingConfirmationCode;
     * this.user = user;
     * this.event = event;
     * this.booking = booking;
     * this.userList = userList;
     * this.eventList = eventList;
     * this.bookingList = bookingList;
     * }
     **/

    private Response(Builder builder) {
        this.statusCode = builder.statusCode;
        this.message = builder.message;
        this.metadata = builder.metadata;
        this.token = builder.token;
        this.role = builder.role;
        this.expirationTime = builder.expirationTime;
        this.bookingConfirmationCode = builder.bookingConfirmationCode;
        this.user = builder.user;
        this.event = builder.event;
        this.booking = builder.booking;
        this.userList = builder.userList;
        this.eventList = builder.eventList;
        this.bookingList = builder.bookingList;
    }

    public static class Builder {
        private int statusCode;
        private String message;
        private String metadata;

        private String token;
        private String role;
        private String expirationTime;
        private String bookingConfirmationCode;

        private UserDTO user;
        private EventDTO event;
        private BookingDTO booking;
        private List<UserDTO> userList;
        private List<EventDTO> eventList;
        private List<BookingDTO> bookingList;

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder metadata(String metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder expirationTime(String expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        public Builder bookingConfirmationCode(String code) {
            this.bookingConfirmationCode = code;
            return this;
        }

        public Builder user(UserDTO user) {
            this.user = user;
            return this;
        }

        public Builder event(EventDTO event) {
            this.event = event;
            return this;
        }

        public Builder booking(BookingDTO booking) {
            this.booking = booking;
            return this;
        }

        public Builder userList(List<UserDTO> userList) {
            this.userList = userList;
            return this;
        }

        public Builder eventList(List<EventDTO> eventList) {
            this.eventList = eventList;
            return this;
        }

        public Builder bookingList(List<BookingDTO> bookingList) {
            this.bookingList = bookingList;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
