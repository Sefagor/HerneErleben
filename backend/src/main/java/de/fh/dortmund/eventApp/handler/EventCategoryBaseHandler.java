package de.fh.dortmund.eventApp.handler;

import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.requestBody.EventBody;
import de.fh.dortmund.eventApp.utils.Utils;


public class EventCategoryBaseHandler extends BaseHandler {

    @Override
    public void handle(Event event, EventBody body) {
        System.out.println("Cate handler");

        if (shouldHandle(event, body)) {
            event.setCategory(body.getCategories().stream()
                    .map(Utils::mapCategoryDTOToCategory)
                    .toList());
        }
         handleNext(event, body);
    }


    @Override
    protected boolean shouldHandle(Event event, EventBody body) {
        return body.getCategories() != null
                && !body.getCategories().isEmpty();
    }
}
