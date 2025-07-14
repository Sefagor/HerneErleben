package de.fh.dortmund.eventApp.facade;

import de.fh.dortmund.eventApp.dto.Response;
import de.fh.dortmund.eventApp.entity.Booking;
import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.service.BookingService;
import de.fh.dortmund.eventApp.service.EmailService;
import de.fh.dortmund.eventApp.service.EventService;
import org.springframework.stereotype.Service;

@Service
public class BookingEmailFacade {
    private final EmailService emailService;
    private final BookingService bookingService;

    private final EventService eventService;

    public BookingEmailFacade(EmailService emailService, BookingService bookingService, EventService eventService) {
        this.emailService = emailService;

        this.bookingService = bookingService;
        this.eventService = eventService;
    }

    public Response makeBooking(Long userID, Long eventID) {
        Response response = bookingService.bookAnEvent(eventID, userID);
        if (response.getStatusCode() == 200) {
            emailService.sendConfirmationEmail(response.getMetadata(), response.getBookingConfirmationCode(), eventService.findEventById(eventID));
        }
        return response;
    }

    public Response cancelBooking(Long bookingId) {
        Booking booking = bookingService.findBookingByID(bookingId);
        String email = booking.getUser().getEmail();
        String confirmationCode = booking.getBookingConfirmationCode();
        Event event = new Event();
        event.setId(booking.getEvent().getId());
        event.setEventDate(booking.getEvent().getEventDate());
        event.setEventName(booking.getEvent().getEventName());

        Response response = bookingService.cancelBooking(bookingId);
        if (response.getStatusCode() == 200) {
                emailService.sendCancellationEmail(email, confirmationCode, event);
        }
        return response;
    }
}
