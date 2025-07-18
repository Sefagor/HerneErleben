package de.fh.dortmund.eventApp.service;

import de.fh.dortmund.eventApp.dto.CategoryDTO;
import de.fh.dortmund.eventApp.dto.EventDTO;
import de.fh.dortmund.eventApp.dto.Response;
import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.exception.CustomException;
import de.fh.dortmund.eventApp.handler.*;
import de.fh.dortmund.eventApp.repo.BookingRepository;
import de.fh.dortmund.eventApp.repo.CategoryRepository;
import de.fh.dortmund.eventApp.repo.EventRepository;
import de.fh.dortmund.eventApp.requestBody.EventBody;
import de.fh.dortmund.eventApp.utils.Utils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
public class EventService {


    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;
    private final CategoryRepository categoryRepository;

    public EventService(EventRepository eventRepository, BookingRepository bookingRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
        this.categoryRepository = categoryRepository;
    }


    public Response addNewEvent(EventBody eventBody, MultipartFile photo) {
        int statusCode = 200;
        String message = "Successful";
        EventDTO eventDTO = null;

        try {
            eventBody.setEventPhoto(Base64.getEncoder().encodeToString(photo.getBytes()));
            Event savedEvent = convertAndSaveEventBodyToEvent(eventBody);
            eventDTO = Utils.mapEventEntityToEventDTO(savedEvent);


        } catch (IOException e) {
            statusCode = 400;
            message = "Failed to process event photo:" + e.getMessage();
        } catch (Exception e) {
            statusCode = 500;
            message = "Error saving an event " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .event(eventDTO)
                .build();
    }


    public Event convertAndSaveEventBodyToEvent(EventBody eventBody) {
        Event event = new Event();
        event.setStatus(eventBody.getStatus());
        event.setHerneID(eventBody.getHerneID());
        event.setEventDate(eventBody.getEventDate());
        event.setEventPhoto(eventBody.getEventPhoto());
        event.setEventLocation(eventBody.getEventLocation());
        event.setMaxParticipant(eventBody.getMaxParticipant());
        event.setEventDescription(eventBody.getEventDescription());
        event.setEventName(eventBody.getEventName());
        event.setStartTime(eventBody.getStartTime());
        event.setEndTime(eventBody.getEndTime());
        event.setCategory(eventBody.getCategories().stream().map(Utils::mapCategoryDTOToCategory).toList());
        return eventRepository.save(event);
    }

    public Response getAllEvents() {
        int statusCode = 200;
        String message = "Successful";
        List<EventDTO> eventDTOList = null;

        try {
            List<Event> eventList = eventRepository.findAll()
                    .stream()
                    .sorted((e1, e2) -> Math.toIntExact(e1.getId() - e2.getId())).toList();
            eventDTOList = Utils.mapEventListEntityToEventListDTO(eventList);

        } catch (Exception e) {
            statusCode = 500;
            message = "Error retrieving events: " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .eventList(eventDTOList)
                .build();
    }

    public Response deleteEvent(Long eventId) {
        int statusCode = 200;
        String message = "Successful";

        try {
            eventRepository.findById(eventId).orElseThrow(() -> new CustomException("Event Not Found"));
            eventRepository.deleteById(eventId);
        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();
        } catch (Exception e) {
            statusCode = 500;
            message = "Error deleting event: " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .build();
    }

    public Response updateEvent(Long eventID, MultipartFile photo, Event body) {
        int statusCode = 200;
        String message = "Successful";
        EventDTO eventDTO = null;

        try {
            Event event = eventRepository.findById(eventID).orElseThrow(() -> new CustomException("Event Not Found"));

            if (!photo.isEmpty()) {
                body.setEventPhoto(Base64.getEncoder().encodeToString(photo.getBytes()));

            }

            if (body.getEventDate() != null) {
                event.setEventDate(body.getEventDate());
            }
            if (body.getStatus() != null) event.setStatus(body.getStatus());
            if (body.getEventLocation() != null) event.setEventLocation(body.getEventLocation());
            if (body.getEventPhoto() != null) event.setEventPhoto(body.getEventPhoto());


            Event updatedEvent = eventRepository.save(event);
            eventDTO = Utils.mapEventEntityToEventDTO(updatedEvent);


        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();
        } catch (Exception e) {
            statusCode = 500;
            message = "Error updating event: " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .event(eventDTO)
                .build();
    }

    @Transactional
    @Async
    public void updateOrCreateEventFromHerne(EventBody body) {

        Event event = eventRepository.findByHerneIDIgnoreCase(body.getHerneID()).orElse(null);
        Event eventS = eventRepository.findByHerneIDIgnoreCase(body.getHerneID()).orElse(null);
        if (event == null) {
            convertAndSaveEventBodyToEvent(body);
            return;
        }
        System.out.println(event.getEventName());
        System.out.println("After");
        BaseHandler baseHandler = new EventNameBaseHandler();
        baseHandler
                .next(new EventDescriptionBaseHandler())
                .next(new EventLocationBaseHandler())
                .next(new EventMaxParticipantBaseHandler())
                .next(new EventDateBaseHandler())
                .next(new EventTimeBaseHandler())
                .next(new EventStatusBaseHandler())
                .next(new EventPhotoBaseHandler())
                .next(new EventCategoryBaseHandler());
        baseHandler.handle(event, body);
        System.out.println(event.getEventName());
        /**
         if(body.getEventName() != null){
         event.setEventName(body.getEventName());
         }
         if(body.getEventDescription() != null){
         event.setEventDescription(body.getEventDescription());
         }
         if(body.getEventLocation() != null){
         event.setEventLocation(body.getEventLocation());
         }

         if(body.getMaxParticipant() != event.getMaxParticipant()){
         event.setMaxParticipant(body.getMaxParticipant());
         }
         if (body.getEventDate() != null) {
         event.setEventDate(body.getEventDate());
         }
         if (!body.getCategories().isEmpty()) {
         event.setCategory(new ArrayList<>(body.getCategories().stream().map(Utils::mapCategoryDTOToCategory).toList()));
         }
         if (body.getStatus() != null) event.setStatus(body.getStatus());
         if (body.getEventLocation() != null) event.setEventLocation(body.getEventLocation());
         if (body.getStartTime() != null) {
         event.setStartTime(body.getStartTime());
         }
         if (body.getEndTime() != null) {
         event.setStartTime(body.getStartTime());
         }
         if (body.getEventPhoto() != null) event.setEventPhoto(body.getEventPhoto());
         if (body.getMaxParticipant() != event.getMaxParticipant() && body.getMaxParticipant() > 0) {
         event.setMaxParticipant(body.getMaxParticipant());
         }
         if (body.getEventPhoto() != null) {
         event.setEventPhoto(body.getEventPhoto());
         }**/
        if (!event.equals(eventS)) {
            eventRepository.save(event);
        }
    }

    public Response getEventById(Long eventID) {
        int statusCode = 200;
        String message = "Successful";
        EventDTO eventDTO = null;

        try {
            Event event = eventRepository.findById(eventID).orElseThrow(() -> new CustomException("Event Not Found"));
            eventDTO = Utils.mapEventEntityToEventDTOPlusBookings(event);

        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();
        } catch (Exception e) {
            statusCode = 500;
            message = "Error retrieving event: " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .event(eventDTO)
                .build();
    }

    public Event findEventById(Long eventID) {
        return eventRepository.findById(eventID).orElseThrow(() -> new CustomException("Event Not Found"));
    }


    public Response getAllAvailableEvents() {
        int statusCode = 200;
        String message = "Successful";
        List<EventDTO> eventDTOList = null;

        try {
            List<Event> eventList = findAllAvailableEvents();
            eventDTOList = Utils.mapEventListEntityToEventListDTO(eventList);

        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();
        } catch (Exception e) {
            statusCode = 500;
            message = "Error loading events: " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .eventList(eventDTOList)
                .build();
    }

    private List<Event> findAllAvailableEvents() {
        return eventRepository.findAll().stream().filter(this::isEventAvailable).toList();
    }


    public boolean isEventAvailable(Event event) {
        return
                event.getMaxParticipant() > event.getBookings().size() && LocalDate.now().isBefore(event.getEventDate());
    }


    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(Utils::mapCategoryToCategoryDTO)
                .distinct()
                .toList();
    }
}
