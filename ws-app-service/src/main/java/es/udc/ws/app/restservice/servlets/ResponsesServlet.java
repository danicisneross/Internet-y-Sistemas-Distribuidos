package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.restservice.dto.ResponseToRestResponseDtoConversor;
import es.udc.ws.app.restservice.dto.RestResponseDto;
import es.udc.ws.app.model.eventService.EventServiceFactory;
import es.udc.ws.app.model.eventService.exceptions.DateResponseExpiration;
import es.udc.ws.app.model.eventService.exceptions.EventAlreadyCanceled;
import es.udc.ws.app.model.eventService.exceptions.ExistAnsweredForEvent;
import es.udc.ws.app.model.response.Response;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestResponseDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class ResponsesServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, InstanceNotFoundException, InputValidationException{
        //ServletUtils.checkEmptyPath(req);
        Long eventId = ServletUtils.getMandatoryParameterAsLong(req, "eventId");
        String userEmail = ServletUtils.getMandatoryParameter(req, "userEmail");
        boolean assistance = Boolean.parseBoolean(ServletUtils.getMandatoryParameter(req, "assistance"));

        try {
            Response response = EventServiceFactory.getService().answerEvents(eventId, userEmail, assistance);

            RestResponseDto responseDto = ResponseToRestResponseDtoConversor.toRestResponseDto(response);
            String responseURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + response.getResponseId();
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Location", responseURL);

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED, JsonToRestResponseDtoConversor.toObjectNode(responseDto), headers);
        } catch (EventAlreadyCanceled ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toEventAlreadyCanceledException(ex), null);
        } catch (DateResponseExpiration ex){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toDateResponseExpirationException(ex), null);
        } catch (ExistAnsweredForEvent ex){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toExistAnsweredForEventException(ex), null);
        }
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException{
        //ServletUtils.checkEmptyPath(req);
        String userEmail = req.getParameter("userEmail");
        String onlyAffirmative = req.getParameter("onlyAffirmative");

        if(userEmail != null || onlyAffirmative != null){
            /*Pattern pattern = Pattern
                    .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher mather = pattern.matcher(userEmail);
            if(mather.find()){*/
                if(onlyAffirmative.equals("false") || onlyAffirmative.equals("true")){
                    List<Response> responses = EventServiceFactory.getService().findAnswerEvent(userEmail, Boolean.parseBoolean(onlyAffirmative));

                    List<RestResponseDto> responseDtos = ResponseToRestResponseDtoConversor.toRestResponseDtos(responses);

                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestResponseDtoConversor.toArrayNode(responseDtos), null);
                } else {
                    throw new InputValidationException("La respuesta tiene que ser: true o false. No se admite otro formato\n");
                }
            /*} else{
                throw new InputValidationException("El email no es valido\n");
            }*/
        } else {
            throw new InputValidationException("Este campo(s) es obligatorio\n");
            }
    }
}
