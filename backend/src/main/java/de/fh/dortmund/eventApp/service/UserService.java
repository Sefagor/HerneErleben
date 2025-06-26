package de.fh.dortmund.eventApp.service;

import de.fh.dortmund.eventApp.dto.LoginRequest;
import de.fh.dortmund.eventApp.dto.Response;
import de.fh.dortmund.eventApp.dto.UserDTO;
import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.entity.Feedback;
import de.fh.dortmund.eventApp.entity.User;
import de.fh.dortmund.eventApp.exception.CustomException;
import de.fh.dortmund.eventApp.exception.DataNotFoundException;
import de.fh.dortmund.eventApp.repo.EventRepository;
import de.fh.dortmund.eventApp.repo.FeedbackRepository;
import de.fh.dortmund.eventApp.repo.UserRepository;
import de.fh.dortmund.eventApp.requestBody.FeedbackBody;
import de.fh.dortmund.eventApp.utils.JWTUtils;
import de.fh.dortmund.eventApp.utils.Utils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    private final FeedbackRepository feedbackRepository;
    private final EventRepository eventRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, AuthenticationManager authenticationManager, FeedbackRepository feedbackRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.feedbackRepository = feedbackRepository;
        this.eventRepository = eventRepository;
    }


    public Response register(User user) {
        Response response = new Response();
        try {
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new CustomException(user.getEmail() + "Already Exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);
        } catch (CustomException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During USer Registration " + e.getMessage());

        }
        return response;
    }


    public Response login(LoginRequest loginRequest) {

        Response response = new Response();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new CustomException("user Not found"));

            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("successful");

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error Occurred During USer Login " + e.getMessage());
        }
        return response;
    }


    public Response getAllUsers() {

        Response response = new Response();
        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUserList(userDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Transactional(readOnly = true)
    public Response getUserBookingHistory(String userId) {

        Response response = new Response();


        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndEvent(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error getting user bookings " + e.getMessage());
        }
        return response;
    }


    public Response deleteUser(String userId) {

        Response response = new Response();

        try {
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException("User Not Found"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }


    public Response getUserById(String userId) {

        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }


    public Response getMyInfo(String email) {

        Response response = new Response();

        try {
            User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new CustomException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }
    public Response updateProfile(String email, User userUpdate) {
        Response response = new Response();

        try {
            User existingUser = userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new CustomException("User Not Found"));

            // Update erlaubte Felder:
            if (userUpdate.getName() != null && !userUpdate.getName().isBlank()) {
                existingUser.setName(userUpdate.getName());
            }

            if (userUpdate.getPhoneNumber() != null && !userUpdate.getPhoneNumber().isBlank()) {
                existingUser.setPhoneNumber(userUpdate.getPhoneNumber());
            }

            if (userUpdate.getPassword() != null && !userUpdate.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
            }

            // Email wird normalerweise nicht geändert, außer du willst es explizit erlauben!
            // if (userUpdate.getEmail() != null && !userUpdate.getEmail().isBlank()) {
            //    existingUser.setEmail(userUpdate.getEmail());
            // }

            User savedUser = userRepository.save(existingUser);

            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);

            response.setStatusCode(200);
            response.setMessage("Profile updated successfully");
            response.setUser(userDTO);

        } catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating profile " + e.getMessage());
        }

        return response;
    }



    public void sendFeedback(FeedbackBody feedbackBody) {
        Event event = eventRepository.findById(feedbackBody.getEventId()).orElseThrow(DataNotFoundException::new);
        Feedback feedback = new Feedback();
        feedback.setComment(feedbackBody.getComment());
        feedback.setRating(feedback.getRating());
        feedback.setSubmissionDate(LocalDate.now());
        feedback.setEvent(event);
        feedbackRepository.save(feedback);
    }



}
