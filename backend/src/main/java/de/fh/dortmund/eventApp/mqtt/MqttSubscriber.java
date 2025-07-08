package de.fh.dortmund.eventApp.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fh.dortmund.eventApp.mqtt.observer.MqttMessageObserver;
import de.fh.dortmund.eventApp.mqtt.subject.MqttSubject;
import de.fh.dortmund.eventApp.service.EventService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttSubscriber {

    @Autowired
    private MqttSubject subject;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EventService eventService;

    @PostConstruct
    public void subscribe() {
        MqttMessageObserver observer = new MqttMessageObserver();
        observer.setEventService(eventService);
        observer.setMapper(mapper);
        subject.addObserver(observer);
        subject.connect();
    }
}