package de.fh.dortmund.eventApp.controller;


import de.fh.dortmund.eventApp.dto.Response;
import de.fh.dortmund.eventApp.entity.User;
import de.fh.dortmund.eventApp.requestBody.FeedbackBody;
import de.fh.dortmund.eventApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User controller", description = "Everything for the Users")

public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "Get all users", description = "find all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "users fetched"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers() {
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Get user by id", description = "Find an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events fetched"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId) {
        Response response = userService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @Operation(summary = "Delete user", description = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User deleted"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUSer(@PathVariable("userId") String userId) {
        Response response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @Operation(summary = "Get the logged user", description = "Get the logged user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping("/get-logged-in-profile-info")
    public ResponseEntity<Response> getLoggedInUserProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Response response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @Operation(summary = "Get all booking from an user", description = "Users's bookings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event deleted"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping("/get-user-bookings/{userId}")
    public ResponseEntity<Response> getUserBookingHistory(@PathVariable("userId") String userId) {
        Response response = userService.getUserBookingHistory(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @Operation(summary = "Send feedback ", description = "Send  feedback")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback sent"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PostMapping("send-feedback")
    @PreAuthorize("hasAuthority('USER')")
    public void sendFeedback(@RequestBody FeedbackBody feedbackBody, @AuthenticationPrincipal User user) {
        // TODO: send feedback
        // userService
    }
}