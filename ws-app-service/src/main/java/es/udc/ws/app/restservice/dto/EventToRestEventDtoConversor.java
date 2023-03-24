package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.event.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventToRestEventDtoConversor {

    public static List<RestEventDto> toRestEventDtos(List<Event> events){
        List<RestEventDto> eventDtos = new ArrayList<>(events.size());
        for (int i = 0; i < events.size(); i++){
            Event event = events.get(i);
            eventDtos.add(toRestEventDto(event));
        }
        return eventDtos;
    }

    public static RestEventDto toRestEventDto(Event event) {
        return new RestEventDto(event.getEventId(), event.getName(), event.getDescription(), event.getStartDate().toString(), event.getDuration(), event.isCancelation(), event.getNumberAssitance(), event.getNumberAssitance() + event.getNumberNoAssitance());
    }

    public static Event toEvent(RestEventDto event){
        LocalDateTime startDate = LocalDateTime.parse(event.getStartDate());
        return new Event(event.getName(), event.getDescription(), null, startDate, event.getDuration(), null, event.isCancelation(), event.getNumberAssistance(), event.getNumberTotalResponses() - event.getNumberAssistance());
    }
}
