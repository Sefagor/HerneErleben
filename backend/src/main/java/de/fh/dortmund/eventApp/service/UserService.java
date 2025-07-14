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
import de.fh.dortmund.eventApp.requestBody.RegistrationBody;
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


    public Response register(RegistrationBody body) {
        int statusCode = 200;
        String message = "Successful";
        UserDTO userDTO = null;
        User user = new User();
        try {
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }
            if (userRepository.existsByEmail(body.getEmail())) {
                throw new CustomException(body.getEmail() + " already exists");
            }
            user.setName(body.getName());
            user.setEmail(body.getEmail());
            user.setPassword(passwordEncoder.encode(body.getPassword()));
            user.setPhoneNumber(body.getPhoneNumber());
            User savedUser = userRepository.save(user);
            userDTO = Utils.mapUserEntityToUserDTO(savedUser);
        } catch (CustomException e) {
            statusCode = 400;
            message = e.getMessage();
        } catch (Exception e) {
            statusCode = 500;
            message = "Error Occurred During User Registration: " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .user(userDTO)
                .build();
    }


    public Response login(LoginRequest loginRequest) {

        int statusCode = 200;
        String message = "Successful";
        String token = null;
        String role = null;
        String expirationTime = null;

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new CustomException("user Not found"));

            token = jwtUtils.generateToken(user);
            role = user.getRole();
            expirationTime = "7 Days";

        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();

        } catch (Exception e) {
            statusCode = 500;
            message = "Error occurred during user login: " + e.getMessage();

        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .token(token)
                .role(role)
                .expirationTime(expirationTime)
                .build();
    }


    public Response getAllUsers() {

        int statusCode = 200;
        String message = "Successful";
        List<UserDTO> userDTOList = null;
        try {
            List<User> userList = userRepository.findAll();
            userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
        } catch (Exception e) {
            statusCode = 500;
            message = "Error getting all users: " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .userList(userDTOList)
                .build();
    }

    @Transactional(readOnly = true)
    public Response getUserBookingHistory(String userId) {
        int statusCode = 200;
        String message = "Successful";
        UserDTO userDTO = null;
        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException("User Not Found"));
            userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndEvent(user);
        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();

        } catch (Exception e) {

            statusCode = 500;
            message = "Error getting user bookings: " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .user(userDTO)
                .build();
    }


    public Response deleteUser(String userId) {

        int statusCode = 200;
        String message = "Successful";
        try {
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException("User Not Found"));
            userRepository.deleteById(Long.valueOf(userId));
        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();

        } catch (Exception e) {
            statusCode = 500;
            message = "Error deleting user: " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .build();
    }


    public Response getUserById(String userId) {
        int statusCode = 200;
        String message = "Successful";
        UserDTO userDTO = null;

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException("User Not Found"));
            userDTO = Utils.mapUserEntityToUserDTO(user);


        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();
        } catch (Exception e) {

            statusCode = 500;
            message = "Error getting user: " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .user(userDTO)
                .build();
    }


    public Response getMyInfo(String email) {

        int statusCode = 200;
        String message = "Successful";
        UserDTO userDTO = null;

        try {
            User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new CustomException("User Not Found"));
            userDTO = Utils.mapUserEntityToUserDTO(user);

        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();

        } catch (Exception e) {
            statusCode = 500;
            message = "Error getting user info: " + e.getMessage();
        }
        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .user(userDTO)
                .build();
    }

    public Response updateProfile(String email, RegistrationBody body) {
        int statusCode = 200;
        String message = "Profile updated successfully";
        UserDTO userDTO = null;
        try {
            User existingUser = userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new CustomException("User Not Found"));

            if (body.getName() != null && !body.getName().isBlank()) {
                existingUser.setName(body.getName());
            }

            if (body.getPhoneNumber() != null && !body.getPhoneNumber().isBlank()) {
                existingUser.setPhoneNumber(body.getPhoneNumber());
            }

            if (body.getPassword() != null && !body.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(body.getPassword()));
            }


            User savedUser = userRepository.save(existingUser);

            userDTO = Utils.mapUserEntityToUserDTO(savedUser);

        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();

        } catch (Exception e) {
            statusCode = 500;
            message = "Error updating profile: " + e.getMessage();
        }

        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .user(userDTO)
                .build();
    }
    public Response updateUserById(String userId, User userUpdate) {
        int statusCode = 200;
        String message = "User updated successfully";
        UserDTO userDTO = null;

        try {
            User existingUser = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new CustomException("User not found"));

            if (userUpdate.getName() != null && !userUpdate.getName().isBlank()) {
                existingUser.setName(userUpdate.getName());
            }

            if (userUpdate.getEmail() != null && !userUpdate.getEmail().isBlank()) {
                existingUser.setEmail(userUpdate.getEmail());
            }

            if (userUpdate.getPhoneNumber() != null && !userUpdate.getPhoneNumber().isBlank()) {
                existingUser.setPhoneNumber(userUpdate.getPhoneNumber());
            }

            if (userUpdate.getPassword() != null && !userUpdate.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
            }

            if (userUpdate.getRole() != null && !userUpdate.getRole().isBlank()) {
                existingUser.setRole(userUpdate.getRole());
            }

            User savedUser = userRepository.save(existingUser);
            userDTO = Utils.mapUserEntityToUserDTO(savedUser);

        } catch (CustomException e) {
            statusCode = 404;
            message = e.getMessage();
        } catch (Exception e) {
            statusCode = 500;
            message = "Error updating user: " + e.getMessage();
        }

        return Response.builder()
                .statusCode(statusCode)
                .message(message)
                .user(userDTO)
                .build();
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
