package de.fh.dortmund.eventApp.controller;


import de.fh.dortmund.eventApp.dto.CategoryDTO;
import de.fh.dortmund.eventApp.dto.Response;
import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.requestBody.EmailContent;
import de.fh.dortmund.eventApp.requestBody.EventBody;
import de.fh.dortmund.eventApp.service.EmailService;
import de.fh.dortmund.eventApp.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/events")
@Tag(name = "Event controller", description = "Everything for the Events")
public class EventController {

    private final EventService eventService;

    private final EmailService emailService;

    public EventController(EventService eventService, EmailService emailService) {
        this.eventService = eventService;
        this.emailService = emailService;
    }


    @Operation(summary = "Create a new event", description = "Add new event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event added"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewEvent(@Valid @RequestBody EventBody eventBody,
                                                @RequestParam(value = "photo", required = false) MultipartFile photo
    ) {

        Response response = eventService.addNewEvent(eventBody, photo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @Operation(summary = "Get all events", description = "Find all events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Events fetched"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping("/all")
    public ResponseEntity<Response> getAllEvents() {
        Response response = eventService.getAllEvents();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Get all categories", description = "Find all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categories found"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories() {
        return eventService.getAllCategories();
    }


    @Operation(summary = "Get event", description = "Get event by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event found"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping("/event-by-id/{eventID}")
    public ResponseEntity<Response> getEventById(@PathVariable Long eventID) {
        Response response = eventService.getEventById(eventID);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @Operation(summary = "Get all events", description = "find all events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events fetched"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping("/all-available-events")
    public ResponseEntity<Response> getAvailableEvents() {
        Response response = eventService.getAllAvailableEvents();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @Operation(summary = "Update Event", description = "Update event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event updated"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PutMapping("/update/{eventID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateEvent(@PathVariable Long eventID,
                                                @RequestParam(value = "photo", required = false) MultipartFile photo,
                                                @RequestBody Event event

    ) {
        Response response = eventService.updateEvent(eventID, photo, event);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @Operation(summary = "Delete event", description = "Delete event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event deleted"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @DeleteMapping("/delete/{eventID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteEvent(@PathVariable Long eventID) {
        Response response = eventService.deleteEvent(eventID);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }


    @Operation(summary = "Send email to everyone ", description = "Send a general email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Email sent"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("notify")
    public void sendEmailToEveryUser(@Valid EmailContent emailContent) {
        emailService.sendToEveryone(emailContent);
    }

}
