package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientEventDto;
import es.udc.ws.app.client.service.dto.ClientResponseDto;
import es.udc.ws.app.client.service.exceptions.ClientDatePassedException;
import es.udc.ws.app.client.service.exceptions.ClientDateResponseExpirationException;
import es.udc.ws.app.client.service.exceptions.ClientEventAlreadyCanceledException;
import es.udc.ws.app.client.service.exceptions.ClientExistAnsweredForEventException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientAppService {

    //FUNCIONALIDAD 1
    public Long registerEvent(ClientEventDto event)
            throws InputValidationException;

    //FUNCIONALIDAD 2
    public List<ClientEventDto> findEventsByKeyword(String keyword, LocalDateTime startDate, LocalDateTime endDate);

    //FUNCIONALIDAD 3
    public ClientEventDto findEvent(Long eventId)
            throws InstanceNotFoundException;

    //FUNCIONALIDAD 4
    public Long answerEvents(Long eventId, String userEmail, boolean assistance)
            throws InstanceNotFoundException, InputValidationException, ClientEventAlreadyCanceledException, ClientExistAnsweredForEventException, ClientDateResponseExpirationException;

    //FUNCIONALIDAD 5
    public void cancelEvent(Long eventId)
            throws InstanceNotFoundException, ClientEventAlreadyCanceledException, ClientDatePassedException;

    //FUNCIONALIDAD 6
    public List<ClientResponseDto> findAnswerEvent(String userEmail, boolean onlyAffirmative)
            throws InputValidationException;
}
