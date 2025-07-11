package de.fh.dortmund.eventApp.service;

import de.fh.dortmund.eventApp.dto.BookingDTO;
import de.fh.dortmund.eventApp.dto.Response;
import de.fh.dortmund.eventApp.entity.Booking;
import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.entity.Status;
import de.fh.dortmund.eventApp.entity.User;
import de.fh.dortmund.eventApp.exception.CustomException;
import de.fh.dortmund.eventApp.repo.BookingRepository;
import de.fh.dortmund.eventApp.repo.EventRepository;
import de.fh.dortmund.eventApp.repo.UserRepository;
import de.fh.dortmund.eventApp.utils.Utils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final EmailService emailService;

    public BookingService(BookingRepository bookingRepository, EventService eventService, EventRepository eventRepository, UserRepository userRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }


    public Response bookAnEvent(Long eventID, Long userId) {
        int statusCode = 200;
        String message = "Buchung erfolgreich";
        String metadata = null;
        String bookingConfirmationCode = null;

        System.out.println("userid:" + userId + "\neventid:" + eventID);

        try {
            // 1) Existiert das Event und der User?
            Event event = eventRepository.findById(eventID)
                    .orElseThrow(() -> new CustomException("Event nicht gefunden"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException("Benutzer nicht gefunden"));

            // 2) Buchungen für vergangene Events verhindern
            if (event.getEventDate().isBefore(LocalDate.now())) {
                throw new CustomException("Buchung für vergangene Veranstaltungen nicht möglich");
            }

            // 3) Prüfen, ob noch Plätze frei sind
            if (!eventService.isEventAvailable(event)) {
                throw new CustomException("Keine freien Plätze mehr für diese Veranstaltung");
            }

            // 4) Doppelte Buchungen unterbinden
            if (bookingRepository.existsByEventAndUser(event, user)) {
                throw new CustomException("Sie haben diese Veranstaltung bereits gebucht");
            }

            // 5) Neue Buchung anlegen und speichern
            Booking bookingRequest = new Booking();
            bookingRequest.setEvent(event);
            bookingRequest.setUser(user);
            bookingRequest.setStatus(Status.ACTIVE);
            bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            metadata = user.getEmail();

        } catch (CustomException e) {
            // Benutzerfehler (z.B. schon gebucht, kein Platz)
            statusCode = 400;
            message = e.getMessage();
        } catch (Exception e) {
            // Serverfehler
            statusCode = 500;
            message = "Fehler beim Speichern der Buchung: " + e.getMessage();
        }

        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .metadata(metadata)
                .bookingConfirmationCode(bookingConfirmationCode)
                .build();
    }


    @Transactional(readOnly = true)
    public Response findBookingByConfirmationCode(String confirmationCode) {

        String message = "Successful";
        int statusCode = 200;
        BookingDTO bookingDTO = null;
        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new CustomException("Booking Not Found"));
            bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedEvents(booking, true);

        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();

        } catch (Exception e) {
            statusCode = 500;
            message = "Error Finding the booking: " + e.getMessage();

        }
        return Response.builder()
                .message(message)
                .statusCode(statusCode)
                .booking(bookingDTO)
                .build();
    }

    public Response getAllBookings() {

        String message = "Successful";
        int statusCode = 200;
        List<BookingDTO> bookingDTOList = null;

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);

        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();

        } catch (Exception e) {
            statusCode = 500;
            message = "Error Getting all bookings: " + e.getMessage();
        }
        return Response.builder()
                .message(message)
                .statusCode(statusCode)
                .bookingList(bookingDTOList)
                .build();
    }

    public Booking findBookingByID(Long bookingID) {
        return bookingRepository.findById(bookingID).orElse(null);
    }

    public Response cancelBooking(Long bookingId) {

        String message = "Successful";
        int statusCode = 200;

        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new CustomException("Booking Does Not Exist"));
            bookingRepository.delete(booking);
        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();

        } catch (Exception e) {
            statusCode = 500;
            message = "Error Cancelling a booking: "+ e.getMessage();
        }
        return Response.builder()
                .message(message)
                .statusCode(statusCode)
                .build();
    }

}
