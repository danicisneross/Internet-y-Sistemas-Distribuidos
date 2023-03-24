package es.udc.ws.app.model.eventService.exceptions;

public class EventAlreadyCanceled extends Exception {
    public EventAlreadyCanceled(String errMess){super(errMess);}
}
