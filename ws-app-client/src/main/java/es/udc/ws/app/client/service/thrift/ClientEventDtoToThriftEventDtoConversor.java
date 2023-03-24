package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientEventDto;
import es.udc.ws.app.thrift.ThriftEventDto;
import es.udc.ws.app.thrift.ThriftInputValidationException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ClientEventDtoToThriftEventDtoConversor {

    public static ClientEventDto toClientEventDto (ThriftEventDto event){
        long duration = (long) event.getDuration();
        LocalDateTime endDate = LocalDateTime.parse(event.getStartDate()).plusHours(duration);

        return new ClientEventDto(
                event.getName(),
                event.getDescription(),
                LocalDateTime.parse(event.getStartDate()),
                LocalDateTime.parse(endDate.toString()),
                event.getEventId(),
                event.isCancelation(),
                event.getNumberAssistance(),
                event.getNumberTotalResponses());
    }

    public static ThriftEventDto toThriftEventDto(ClientEventDto clientEventDto) throws ThriftInputValidationException {
        Long eventId = clientEventDto.getEventId();

        try {
            double duration = (double) Duration.between(clientEventDto.getStartDate(), clientEventDto.getEndDate()).toHours();
            boolean canceled = clientEventDto.getCancelation();
            int numberAssistance = clientEventDto.getNumberAssistance();
            int numberTotalResponses = clientEventDto.getNumberTotalResponses();
            return new ThriftEventDto(
                    eventId == null ? -1 : eventId.longValue(),
                    clientEventDto.getName(), clientEventDto.getDescription(), clientEventDto.getStartDate().toString(),
                    duration, canceled, numberAssistance == 0 ? -1 : numberAssistance, numberTotalResponses == 0 ? -1 : numberTotalResponses
            );
        } catch (DateTimeParseException e){
            throw new ThriftInputValidationException("El formato de fecha es incorrecto (AÑO-MES-DIATHORA:MIN)\n");
        }
    }

    public static List<ClientEventDto> toClientEventDtos(List<ThriftEventDto> eventos){
        List<ClientEventDto> clientEventDtos = new ArrayList<>(eventos.size());

        for (ThriftEventDto event : eventos){
            clientEventDtos.add(toClientEventDto(event));
        }
        return clientEventDtos;
    }
}
