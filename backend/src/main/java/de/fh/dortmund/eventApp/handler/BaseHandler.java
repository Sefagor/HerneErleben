package de.fh.dortmund.eventApp.handler;

import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.requestBody.EventBody;

public abstract class BaseHandler implements Handler {
    private BaseHandler next;

    public abstract void handle(Event event, EventBody body);

    public BaseHandler next(BaseHandler nextBaseHandler) {
        this.next = nextBaseHandler;
        if (nextBaseHandler != null) {
            return nextBaseHandler;
        }
        return this;

    }

    protected abstract boolean shouldHandle(Event event, EventBody body);

    protected void handleNext(Event event, EventBody body) {
        if (next != null) {
            next.handle(event, body);
        }
    }
}
