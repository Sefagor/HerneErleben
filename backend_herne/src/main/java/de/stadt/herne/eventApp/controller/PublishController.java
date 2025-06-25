package de.stadt.herne.eventApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.stadt.herne.eventApp.entity.EventBody;
import de.stadt.herne.eventApp.mqtt.MqttPublisher;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@RestController
@Validated
public class PublishController {

    private final MqttPublisher mqttPublisher;
    private final ObjectMapper objectMapper;

    public PublishController(MqttPublisher mqttPublisher, ObjectMapper objectMapper) {
        this.mqttPublisher = mqttPublisher;
        this.objectMapper = objectMapper;
    }

    @PostMapping(path = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> sendMessage(
            @RequestPart("event") String eventJson,
            @RequestPart("image") MultipartFile imageFile) throws IOException {

        EventBody eventBody = objectMapper.readValue(eventJson, EventBody.class);
        List<String> errors = validate(eventBody);
        if (errors.isEmpty()) {
            eventBody.setEventPhoto(Base64.getEncoder().encodeToString(imageFile.getBytes()));
            mqttPublisher.publish(objectMapper.writeValueAsString(eventBody));
            return ResponseEntity.ok("Message envoyé avec succès");
        } else {
            return ResponseEntity.badRequest().body(errors);

        }

    }

    private List<String> validate(EventBody eventBody) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EventBody>> violations = validator.validate(eventBody);
        return violations.stream().map(c -> c.getPropertyPath().toString() + " " + c.getMessage()).toList();

    }

}

