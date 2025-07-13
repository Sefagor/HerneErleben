package de.fh.dortmund.eventApp.handler;

import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.requestBody.EventBody;

public class EventStatusBaseHandler extends BaseHandler {
    @Override
    public void handle(Event event, EventBody body) {
        System.out.println("Status handler");
        if (shouldHandle(event, body)) {
            event.setStatus(body.getStatus());
        }
        handleNext(event, body);
    }

    @Override
    protected boolean shouldHandle(Event event, EventBody body) {
        return event.getStatus() != null && body.getStatus() != null && !event.getStatus().equals(body.getStatus());
    }
}
