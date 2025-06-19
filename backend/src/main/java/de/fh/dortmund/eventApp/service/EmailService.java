package de.fh.dortmund.eventApp.service;

import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.repo.UserRepository;
import de.fh.dortmund.eventApp.requestBody.EmailContent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    public EmailService(JavaMailSender javaMailSender, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
    }

    private SimpleMailMessage makeMessage(String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@app-management.de");
        message.setSubject(subject);
        return message;
    }


    private SimpleMailMessage makeConfirmationEmail(String email, String confirmationCode, Event event) {
        SimpleMailMessage message = makeMessage("Booking Confirmation");
        message.setTo(email);
        String emailText = "We hereby confirm your reservation for the event: " +
                event.getId() + " on " + event.getEventDate().toString() +
                ". Thank you." + " Booking number " + confirmationCode;
        message.setText(emailText);
        return message;
    }

    private SimpleMailMessage makeCancellationEmail(String email, String confirmationCode, Event event) {
        SimpleMailMessage message = makeMessage("Cancellation Confirmation");
        message.setTo(email);
        String emailText = "We hereby confirm your Cancellation for the event: " +
                event.getId() + " on " + event.getEventDate().toString() +
                ". Thank you." + " Booking number " + confirmationCode;
        message.setText(emailText);
        return message;
    }

    @Async
    public void sendConfirmationEmail(String email, String confirmationCode, Event event) {
        javaMailSender.send(makeConfirmationEmail(email, confirmationCode, event));
    }

    @Async
    public void sendCancellationEmail(String email, String confirmationCode, Event event) {
        javaMailSender.send(makeCancellationEmail(email, confirmationCode, event));
    }


    @Async
    public void sendToEveryone(EmailContent emailContent) {
        SimpleMailMessage message = makeMessage(emailContent.getSubject());
        message.setText(emailContent.getMessage());
        userRepository.findAll().forEach(user -> {
            message.setTo(user.getEmail());
            javaMailSender.send(message);
        });
    }

    @Async
    public void testConnection() {
        SimpleMailMessage message = makeMessage("test");
        message.setTo("test@gmail.test");
        message.setText("Connection test");
        javaMailSender.send(message);
    }
}
