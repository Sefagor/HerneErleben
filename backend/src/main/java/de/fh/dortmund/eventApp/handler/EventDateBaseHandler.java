package de.fh.dortmund.eventApp.handler;

import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.requestBody.EventBody;

import java.time.LocalDateTime;

public class EventDateBaseHandler extends BaseHandler {

    @Override
    public void handle(Event event, EventBody body) {
        System.out.println("date handler");

        if (shouldHandle(event, body)) {
            event.setEventDate(body.getEventDate());
        }
        handleNext(event, body);
    }

    @Override
    protected boolean shouldHandle(Event event, EventBody body) {
        LocalDateTime newDate = body.getEventDate().atStartOfDay();
        return newDate.isAfter(LocalDateTime.now()) && (event.getEventDate() == null || !body.getEventDate().equals(event.getEventDate()));
    }
}
