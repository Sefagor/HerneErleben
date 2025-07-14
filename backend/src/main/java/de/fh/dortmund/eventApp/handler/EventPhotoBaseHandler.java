package de.fh.dortmund.eventApp.handler;

import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.requestBody.EventBody;

public class EventPhotoBaseHandler extends BaseHandler {

    @Override
    public void handle(Event event, EventBody body) {

        if (shouldHandle(event, body)) {
            event.setEventPhoto(body.getEventPhoto());
        }
        handleNext(event, body);
    }

    @Override
    protected boolean shouldHandle(Event event, EventBody body) {
        String newPhoto = body.getEventPhoto();
        return newPhoto != null
                && (event.getEventPhoto() == null || !newPhoto.equals(event.getEventPhoto()));
    }
}
