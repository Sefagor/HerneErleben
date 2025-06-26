package de.fh.dortmund.eventApp.service;

import de.fh.dortmund.eventApp.dto.CategoryDTO;
import de.fh.dortmund.eventApp.dto.EventDTO;
import de.fh.dortmund.eventApp.dto.Response;
import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.exception.CustomException;
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
import java.util.ArrayList;
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
        Response response = new Response();

        try {
            eventBody.setEventPhoto(Base64.getEncoder().encodeToString(photo.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Event savedEvent = convertAndSaveEventBodyToEvent(eventBody);
            EventDTO eventDTO = Utils.mapEventEntityToEventDTO(savedEvent);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setEvent(eventDTO);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving an event " + e.getMessage());
        }
        return response;
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
        Response response = new Response();

        try {
            List<Event> eventList = eventRepository.findAll()
                    .stream()
                    .sorted((e1, e2) -> Math.toIntExact(e1.getId() - e2.getId())).toList();
            List<EventDTO> eventDTOList = Utils.mapEventListEntityToEventListDTO(eventList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setEventList(eventDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving an event " + e.getMessage());
        }
        return response;
    }

    public Response deleteEvent(Long eventId) {
        Response response = new Response();

        try {
            eventRepository.findById(eventId).orElseThrow(() -> new CustomException("Event Not Found"));
            eventRepository.deleteById(eventId);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving event " + e.getMessage());
        }
        return response;
    }

    public Response updateEvent(Long eventID, MultipartFile photo, Event body) {
        Response response = new Response();

        try {
            Event event = eventRepository.findById(eventID).orElseThrow(() -> new CustomException("Event Not Found"));
            if (body.getEventDate() != null) {
                event.setEventDate(body.getEventDate());
            }
            if (body.getStatus() != null) event.setStatus(body.getStatus());
            if (body.getEventLocation() != null) event.setEventLocation(body.getEventLocation());
            if (body.getEventPhoto() != null) event.setEventPhoto(body.getEventPhoto());
            if (body.getMaxParticipant() != event.getMaxParticipant() && body.getMaxParticipant() > 0) {
                event.setMaxParticipant(body.getMaxParticipant());
            }
            if (!photo.isEmpty()) {
                Base64.getEncoder().encodeToString(photo.getBytes());
            }
            Event updatedEvent = eventRepository.save(event);
            EventDTO eventDTO = Utils.mapEventEntityToEventDTO(updatedEvent);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setEvent(eventDTO);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving an event " + e.getMessage());
        }
        return response;
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
        }
        if(!event.equals(eventS)){
            eventRepository.save(event);
        }
    }

    public Response getEventById(Long eventID) {
        Response response = new Response();

        try {
            Event event = eventRepository.findById(eventID).orElseThrow(() -> new CustomException("Event Not Found"));
            EventDTO eventDTO = Utils.mapEventEntityToEventDTOPlusBookings(event);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setEvent(eventDTO);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a event " + e.getMessage());
        }
        return response;
    }

    public Event findEventById(Long eventID) {
        return eventRepository.findById(eventID).orElseThrow(() -> new CustomException("Event Not Found"));
    }


    public Response getAllAvailableEvents() {
        Response response = new Response();

        try {
            List<Event> eventList = findAllAvailableEvents();
            List<EventDTO> eventDTOList = Utils.mapEventListEntityToEventListDTO(eventList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setEventList(eventDTOList);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error loading an Event " + e.getMessage());
        }
        return response;
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
