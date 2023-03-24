package es.udc.ws.app.model.eventService;


import es.udc.ws.app.model.event.Event;
import es.udc.ws.app.model.eventService.exceptions.DatePassed;
import es.udc.ws.app.model.eventService.exceptions.DateResponseExpiration;
import es.udc.ws.app.model.eventService.exceptions.EventAlreadyCanceled;
import es.udc.ws.app.model.eventService.exceptions.ExistAnsweredForEvent;
import es.udc.ws.app.model.response.Response;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    //FUNCIONALIDAD 1
    public Event registerEvent(Event e) throws InputValidationException;

    //FUNCIONALIDAD 2
    public List<Event> findEventsByKeyword(String keyword, LocalDateTime startDate, LocalDateTime endDate);

    //FUNCIONALIDAD 3
    public Event findEvent(Long eventId) throws InstanceNotFoundException;

    //FUNCIONALIDAD 4
    public Response answerEvents(Long eventId, String userEmail, boolean assistance)
            throws InstanceNotFoundException, InputValidationException, EventAlreadyCanceled, ExistAnsweredForEvent, DateResponseExpiration;

    //FUNCIONALIDAD 5
    public void cancelEvent(Long eventId) throws InstanceNotFoundException, EventAlreadyCanceled, DatePassed;

    //FUNCIONALIDAD 6
    public List<Response> findAnswerEvent(String userEmail, boolean onlyAffirmative) throws InputValidationException;
}
