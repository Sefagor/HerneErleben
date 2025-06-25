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

        Response response = new Response();

        try {
            Event event = eventRepository.findById(eventID).orElseThrow(() -> new CustomException("Event Not Found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException("User Not Found"));

            if (event.getEventDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Booking isn't possible");
            }


            if (!eventService.isEventAvailable(event)) {
                throw new CustomException("Not more place Available for selected date range");
            }

            Booking bookingRequest = new Booking();

            bookingRequest.setEvent(event);
            bookingRequest.setUser(user);
            bookingRequest.setStatus(Status.ACTIVE);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setMetadata(user.getEmail());
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a booking: " + e.getMessage());

        }
        return response;
    }


    @Transactional(readOnly = true)
    public Response findBookingByConfirmationCode(String confirmationCode) {

        Response response = new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new CustomException("Booking Not Found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedEvents(booking, true);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBooking(bookingDTO);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Finding a booking: " + e.getMessage());

        }
        return response;
    }

    public Response getAllBookings() {

        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingList(bookingDTOList);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Getting all bookings: " + e.getMessage());

        }
        return response;
    }

    public Booking findBookingByID(Long bookingID) {
        return bookingRepository.findById(bookingID).orElse(null);
    }

    public Response cancelBooking(Long bookingId) {

        Response response = new Response();

        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new CustomException("Booking Does Not Exist"));
            booking.setStatus(Status.CANCELLED);
            bookingRepository.save(booking);
            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Cancelling a booking: " + e.getMessage());

        }
        return response;
    }

}
