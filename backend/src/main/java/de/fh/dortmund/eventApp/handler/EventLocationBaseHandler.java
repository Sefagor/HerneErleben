package de.fh.dortmund.eventApp.handler;

import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.entity.Location;
import de.fh.dortmund.eventApp.requestBody.EventBody;

public class EventLocationBaseHandler extends BaseHandler {

    @Override
    public void handle(Event event, EventBody body) {
        System.out.println("loc handler");

        if (shouldHandle(event, body)) {
            event.setEventLocation(body.getEventLocation());
        }
        handleNext(event, body);
    }

    @Override
    protected boolean shouldHandle(Event event, EventBody body) {
        Location newLocation = body.getEventLocation();
        return newLocation != null
                && (event.getEventLocation() == null || !newLocation.equals(event.getEventLocation()));
    }
}
