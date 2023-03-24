package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.eventService.exceptions.DatePassed;
import es.udc.ws.app.model.eventService.exceptions.EventAlreadyCanceled;
import es.udc.ws.app.restservice.dto.EventToRestEventDtoConversor;
import es.udc.ws.app.restservice.dto.RestEventDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestEventDtoConversor;
import es.udc.ws.app.model.event.Event;
import es.udc.ws.app.model.eventService.EventServiceFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("serial")
public class EventsServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException{

        String path = ServletUtils.normalizePath(req.getPathInfo());
        try{
            if(path == null){
                RestEventDto eventDto = JsonToRestEventDtoConversor.toRestEventDto(req.getInputStream()); //Paso 1

                Event event = EventToRestEventDtoConversor.toEvent(eventDto); //Paso 2

                event = EventServiceFactory.getService().registerEvent(event); //Paso 3

                eventDto = EventToRestEventDtoConversor.toRestEventDto(event); //Paso 4
                String eventURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + event.getEventId(); //obtiene la url de la peticion + el eventId
                Map<String, String> headers = new HashMap<>(1);
                headers.put("Location", eventURL);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED, JsonToRestEventDtoConversor.toObjectNode(eventDto), headers); //Paso 5 -> pasa la respuesta, lo que va a mostrarse como resultado
            }
            else{
                String[] partes = path.split("/");
                if(partes[partes.length -1].equals("cancel")){
                    Long eventId = Long.parseLong(partes[partes.length-2]);
                    EventServiceFactory.getService().cancelEvent(eventId);
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, null, null);
                }
            }

        }catch (DateTimeParseException ex) {
            throw new InputValidationException("El formato de fecha es incorrecto (AÑO-MES-DIATHORA:MIN)\n");
        } catch (DatePassed e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toDatePassedException(e), null);
        } catch (EventAlreadyCanceled e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toEventAlreadyCanceledException(e), null);
        } catch (InstanceNotFoundException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND, null, null);
        }
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException, InstanceNotFoundException{
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if(path != null && path.length()>1){
            Long eventId = ServletUtils.getIdFromPath(req, "event");
            Event event = EventServiceFactory.getService().findEvent(eventId);
            RestEventDto eventDto = EventToRestEventDtoConversor.toRestEventDto(event);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestEventDtoConversor.toObjectNode(eventDto), null);

        } else {
            try{
                //ServletUtils.checkEmptyPath(req);
                String keyword = req.getParameter("keyword");
                String endDate = req.getParameter("endDate");
                if(endDate != null){
                    LocalDateTime endDate2 = LocalDateTime.parse(endDate);

                    List<Event> events = EventServiceFactory.getService().findEventsByKeyword(keyword, LocalDateTime.now(), endDate2); //Paso 3

                    List<RestEventDto> eventDtos = EventToRestEventDtoConversor.toRestEventDtos(events); //Paso 4

                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestEventDtoConversor.toArrayNode(eventDtos), null); //Paso 5
                }else{
                    throw new InputValidationException("Este campo es obligatorio");
                }
            } catch (DateTimeParseException ex){
                throw new InputValidationException("El formato de fecha es incorrecto (AÑO-MES-DIATHORA:MIN)\n");
            }
        }
    }

}
