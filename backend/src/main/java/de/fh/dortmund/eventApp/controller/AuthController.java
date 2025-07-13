package de.fh.dortmund.eventApp.controller;


import de.fh.dortmund.eventApp.dto.LoginRequest;
import de.fh.dortmund.eventApp.dto.Response;
import de.fh.dortmund.eventApp.entity.User;
import de.fh.dortmund.eventApp.requestBody.RegistrationBody;
import de.fh.dortmund.eventApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication controller", description = "Everything for the authentication")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "Register user", description = "add a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered"),
            @ApiResponse(responseCode = "500", description = "User could not be registered")
    })
    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody RegistrationBody body) {
        Response response = userService.register(body);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Log user in", description = "check if the user is in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in"),
            @ApiResponse(responseCode = "500", description = "Error")
    })
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @Operation(summary = "Update user", description = "Change users information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Changed"),
            @ApiResponse(responseCode = "500", description = "Error")
    })
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Response> updateUser(@RequestBody RegistrationBody body, @AuthenticationPrincipal User user) {
        Response response = userService.updateProfile(user.getEmail(), body);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
