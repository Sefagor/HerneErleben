package de.fh.dortmund.eventApp.handler;

import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.requestBody.EventBody;

public class EventDescriptionBaseHandler extends BaseHandler {

    @Override
    public void handle(Event event, EventBody body) {
        System.out.println("Des handler");

        if (shouldHandle(event, body)) {
            event.setEventDescription(body.getEventDescription());
        }
        handleNext(event, body);
    }

    @Override
    protected boolean shouldHandle(Event event, EventBody body) {
        String newDescription = body.getEventDescription();
        return newDescription != null
                && (event.getEventDescription() == null
                || !newDescription.equals(event.getEventDescription()));
    }
}
