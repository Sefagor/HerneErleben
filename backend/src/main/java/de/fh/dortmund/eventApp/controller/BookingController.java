package de.fh.dortmund.eventApp.controller;


import de.fh.dortmund.eventApp.dto.Response;
import de.fh.dortmund.eventApp.service.BookingService;
import de.fh.dortmund.eventApp.service.facade.BookingEmailFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bookings")
@Tag(name = "Booking controller", description = "Everything for the Booking")
public class BookingController {

    private final BookingService bookingService;
    private final BookingEmailFacade bookingEmailFacade;

    public BookingController(BookingService bookingService, BookingEmailFacade bookingEmailFacade) {
        this.bookingService = bookingService;
        this.bookingEmailFacade = bookingEmailFacade;
    }


    @Operation(summary = "Make a booking", description = "Create a new Booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booked"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @PostMapping("/book-event/{eventID}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBookings(@PathVariable Long eventID,
                                                 @PathVariable Long userId) {

        Response response = bookingEmailFacade.makeBooking(userId, eventID);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @Operation(summary = "Get all Bookings", description = "fetch all bookings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bookings fetched"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings() {
        Response response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @Operation(summary = "Find booking by confirmation code", description = "Get booking from the reservation code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking found"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        Response response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Cancel booking", description = "Cancel booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking cancelled"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> cancelBooking(@PathVariable Long bookingId) {
        Response response = bookingEmailFacade.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
