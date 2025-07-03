package de.fh.dortmund.eventApp.handler;

import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.requestBody.EventBody;

public class EventNameBaseHandler extends BaseHandler {

    @Override
    public void handle(Event event, EventBody body) {
        System.out.println("Name handler");
        if (shouldHandle(event, body)) {
            System.out.println(body.getEventName());
            event.setEventName(body.getEventName());
        }
        handleNext(event, body);
    }

    @Override
    protected boolean shouldHandle(Event event, EventBody body) {
        String newName = body.getEventName();
        return newName != null
                && (event.getEventName() == null || !newName.equals(event.getEventName()));
    }
}
