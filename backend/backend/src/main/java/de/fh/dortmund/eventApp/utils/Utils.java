package de.fh.dortmund.eventApp.utils;

import de.fh.dortmund.eventApp.dto.*;
import de.fh.dortmund.eventApp.entity.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();


    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }


    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        return userDTO;
    }



    public static CategoryDTO mapCategoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public static LocationDTO mapLocationEntityToLocationDTO(Location location) {
        return LocationDTO.builder()
                .city(location.getCity())
                .houseNumber(location.getHouseNumber())
                .zip(location.getZip())
                .street(location.getStreet()).build();
    }

    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        // Map simple fields
        bookingDTO.setId(booking.getId());
        bookingDTO.setBookingDate(booking.getBookingDate());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        return bookingDTO;
    }

    public static EventDTO mapEventEntityToEventDTOPlusBookings(Event event) {
        EventDTO eventDTO = new EventDTO();

        eventDTO.setId(event.getId());
        eventDTO.setEventDate(event.getEventDate());
        eventDTO.setStatus(event.getStatus());
        eventDTO.setEventPhoto(event.getEventPhoto());
        eventDTO.setEventName(event.getEventName());
        eventDTO.setEventLocation(mapLocationEntityToLocationDTO(event.getEventLocation()));
        eventDTO.setAverageRating(getAverageRating(event));
        eventDTO.setStartTime(event.getStartTime());
        eventDTO.setEndTime(event.getEndTime());
        eventDTO.setCategories(event.getCategory().stream().map(Utils::mapCategoryToCategoryDTO).toList());
        eventDTO.setEventDescription(event.getEventDescription());
        if (event.getBookings() != null) {
            eventDTO.setBookings(event.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList()));
        }
        return eventDTO;
    }

    private static float getAverageRating(Event event) {
        List<Feedback> feedbacks = event.getFeedback();
        if (feedbacks.isEmpty()) {
            return 0f;
        } else {
            return (float) (feedbacks.stream().map(Feedback::getRating).reduce(0, Integer::sum)) / feedbacks.size();
        }
    }

    public static BookingDTO mapBookingEntityToBookingDTOPlusBookedEvents(Booking booking, boolean mapUser) {

        BookingDTO bookingDTO = new BookingDTO();
        // Map simple fields
        bookingDTO.setId(booking.getId());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setBookingDate(booking.getBookingDate());
        if (mapUser) {
            bookingDTO.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
        }
        Event event = booking.getEvent();
        if (event != null) {
            EventDTO eventDTO = new EventDTO();

            eventDTO.setId(event.getId());
            eventDTO.setEventDate(event.getEventDate());
            eventDTO.setEndTime(event.getEndTime());
            eventDTO.setStartTime(event.getStartTime());
            eventDTO.setStatus(event.getStatus());
            eventDTO.setEventPhoto(event.getEventPhoto());
            eventDTO.setEventLocation(mapLocationEntityToLocationDTO(event.getEventLocation()));
            bookingDTO.setEvent(eventDTO);
        }
        return bookingDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndEvent(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        if (!user.getBookings().isEmpty()) {
            userDTO.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOPlusBookedEvents(booking, false)).collect(Collectors.toList()));
        }
        return userDTO;
    }


    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static List<EventDTO> mapEventListEntityToEventListDTO(List<Event> eventList) {
        return eventList.stream().map(Utils::mapEventEntityToEventDTO).collect(Collectors.toList());
    }

    public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList) {
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
    }


    public static Category mapCategoryDTOToCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return category;
    }

    public static EventDTO mapEventEntityToEventDTO(Event updatedEvent) {
        return null;
    }
}


