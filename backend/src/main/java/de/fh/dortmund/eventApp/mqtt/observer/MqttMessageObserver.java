package de.fh.dortmund.eventApp.mqtt.observer;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fh.dortmund.eventApp.requestBody.EventBody;
import de.fh.dortmund.eventApp.service.EventService;
import lombok.Setter;

@Setter
public class MqttMessageObserver implements Observer {
    private EventService eventService;
    private ObjectMapper mapper;
    @Override
    public void onMessage(String topic, String payload)  {
        if (topic.equals("infos/servertest")) {
            try {
                EventBody eventBody = mapper.readValue(payload, EventBody.class);
                eventService.updateOrCreateEventFromHerne(eventBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
