package de.fh.dortmund.eventApp.handler;

import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.requestBody.EventBody;

public class EventMaxParticipantBaseHandler extends BaseHandler {

    @Override
    public void handle(Event event, EventBody body) {
        System.out.println("Max  handler");

        if (shouldHandle(event, body)) {
            event.setMaxParticipant(body.getMaxParticipant());
        }
        handleNext(event, body);
    }

    @Override
    protected boolean shouldHandle(Event event, EventBody body) {
        int newMax = body.getMaxParticipant();
        return newMax > 0 && newMax != event.getMaxParticipant();
    }
}
