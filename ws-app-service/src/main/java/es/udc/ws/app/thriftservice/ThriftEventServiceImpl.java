package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.event.Event;
import es.udc.ws.app.model.eventService.EventServiceFactory;
import es.udc.ws.app.model.eventService.exceptions.DateResponseExpiration;
import es.udc.ws.app.model.eventService.exceptions.EventAlreadyCanceled;
import es.udc.ws.app.model.eventService.exceptions.ExistAnsweredForEvent;
import es.udc.ws.app.model.response.Response;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;


public class ThriftEventServiceImpl implements ThriftEventService.Iface {

    @Override
    public ThriftEventDto registerEvent(ThriftEventDto eventDto) throws ThriftInputValidationException {

        Event event = EventToThriftEventDtoConversor.toEvent(eventDto);

        try {
            Event addedEvent = EventServiceFactory.getService().registerEvent(event);
            return EventToThriftEventDtoConversor.toThriftEventDto(addedEvent);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }
     
    public List<ThriftEventDto> findEventsByKeyword(String keyword, String endDate) {

        List<Event> events = EventServiceFactory.getService().findEventsByKeyword(keyword, LocalDateTime.now(), LocalDateTime.parse(endDate));
        return EventToThriftEventDtoConversor.toThriftEventDtos(events);
    }

    @Override
    public ThriftEventDto findEvent(long eventId) throws ThriftInstanceNotFoundException{
        try{
            Event event = EventServiceFactory.getService().findEvent(eventId);
            return EventToThriftEventDtoConversor.toThriftEventDto(event);
        }
        catch (InstanceNotFoundException e){
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }
    }
    @Override
    public ThriftResponseDto answerEvent(long eventId, String userEmail, boolean assistance) throws ThriftInstanceNotFoundException,
            ThriftInputValidationException, ThriftEventAlreadyCanceled, ThriftExistAnsweredForEvent, ThriftDateResponseExpiration{
        try{
            Response response = EventServiceFactory.getService().answerEvents(eventId, userEmail, assistance);
            return ResponseToThriftResponseDtoConversor.toThriftResponseDto(response);
        }
        catch (InstanceNotFoundException e){
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }
        catch (InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }
        catch (EventAlreadyCanceled e){
            throw new ThriftEventAlreadyCanceled(e.getMessage());
        }
        catch (ExistAnsweredForEvent e){
            throw new ThriftExistAnsweredForEvent(e.getMessage());
        }
        catch (DateResponseExpiration e){
            throw new ThriftDateResponseExpiration(e.getMessage());
        }
    }
}
