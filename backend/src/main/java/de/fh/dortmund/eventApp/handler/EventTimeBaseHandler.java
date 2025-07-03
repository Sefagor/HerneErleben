package de.fh.dortmund.eventApp.handler;

import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.requestBody.EventBody;

public class EventTimeBaseHandler extends BaseHandler {

    @Override
    public void handle(Event event, EventBody body) {
        System.out.println("Event time handler");
        if (shouldHandle(event, body)) {
            event.setStartTime(body.getStartTime());
            event.setEndTime(body.getEndTime());
        }
        handleNext(event, body);
    }

    @Override
    protected boolean shouldHandle(Event event, EventBody body) {
        return (body.getStartTime() != null && !body.getStartTime().equals(event.getStartTime()) && body.getEndTime() != null)
                || (body.getEndTime() != null && !body.getEndTime().equals(event.getEndTime()) && body.getStartTime() != null);
    }
}
