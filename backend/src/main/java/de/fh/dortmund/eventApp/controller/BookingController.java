package de.fh.dortmund.eventApp.controller;


import de.fh.dortmund.eventApp.dto.Response;
import de.fh.dortmund.eventApp.service.BookingService;
import de.fh.dortmund.eventApp.service.facade.BookingEmailFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bookings")

public class BookingController {

    private final BookingService bookingService;
    private final BookingEmailFacade bookingEmailFacade;

    public BookingController(BookingService bookingService, BookingEmailFacade bookingEmailFacade) {
        this.bookingService = bookingService;
        this.bookingEmailFacade = bookingEmailFacade;
    }

    @PostMapping("/book-event/{eventID}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBookings(@PathVariable Long eventID,
                                                 @PathVariable Long userId) {

        System.out.println("Controller" + userId);
        Response response = bookingEmailFacade.makeBooking(userId, eventID);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings() {
        Response response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        Response response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> cancelBooking(@PathVariable Long bookingId) {
        Response response = bookingEmailFacade.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
