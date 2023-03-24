package es.udc.ws.app.thriftservice;

import es.udc.ws.app.thrift.ThriftEventDto;
import es.udc.ws.app.model.event.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventToThriftEventDtoConversor {

    public static Event toEvent(ThriftEventDto event) {
        return new Event(event.getName(), event.getDescription(), null, LocalDateTime.parse(event.getStartDate()), event.getDuration(), event.getEventId(), event.isCancelation(), event.getNumberAssistance(), event.numberTotalResponses - event.getNumberAssistance());
    }

    public static List<ThriftEventDto> toThriftEventDtos(List<Event> events) {

        List<ThriftEventDto> dtos = new ArrayList<>(events.size());

        for (Event event : events){
            dtos.add(toThriftEventDto(event));
        }
        return dtos;
    }

    public static ThriftEventDto toThriftEventDto (Event event){
        return new ThriftEventDto(event.getEventId(), event.getName(), event.getDescription(), event.getStartDate().toString(), event.getDuration(), event.isCancelation(), event.getNumberAssitance(), event.getNumberAssitance() + event.getNumberNoAssitance());
    }
}
