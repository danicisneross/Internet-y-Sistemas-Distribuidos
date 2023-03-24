package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientAppService;
import es.udc.ws.app.client.service.dto.ClientEventDto;
import es.udc.ws.app.client.service.dto.ClientResponseDto;
import es.udc.ws.app.client.service.exceptions.ClientDatePassedException;
import es.udc.ws.app.client.service.exceptions.ClientDateResponseExpirationException;
import es.udc.ws.app.client.service.exceptions.ClientEventAlreadyCanceledException;
import es.udc.ws.app.client.service.exceptions.ClientExistAnsweredForEventException;
import es.udc.ws.app.client.service.rest.json.JsonToClientEventDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientResponseDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.module.Configuration;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

public class RestClientAppService implements ClientAppService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientAppService.endpointAddress";
    private String endpointAddress;

    //FUNC-1
    public Long registerEvent(ClientEventDto event) throws InputValidationException{

        try{

            HttpResponse response = Request.Post(getEndpointAddress() + "events").
                    bodyStream(toInputStream(event), ContentType.create("aplication/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientEventDtoConversor.toClientEventDto(response.getEntity().getContent()).getEventId();
        } catch (InputValidationException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //FUNC-3
    @Override
    public ClientEventDto findEvent(Long eventId) throws InstanceNotFoundException {

        try{

            HttpResponse response = Request.Get(getEndpointAddress() + "events/" + eventId).
                    execute().returnResponse();
            
            validateStatusCode(HttpStatus.SC_OK, response);
            
            return JsonToClientEventDtoConversor.toClientEventDto(response.getEntity().getContent());
            
        } catch (InstanceNotFoundException e){
            throw e;
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //FUNC-4
    @Override
    public Long answerEvents(Long eventId, String userEmail, boolean assistance) throws InstanceNotFoundException,
            InputValidationException, ClientEventAlreadyCanceledException, ClientExistAnsweredForEventException, ClientDateResponseExpirationException{

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "responses").
                    bodyForm(
                            Form.form().
                                    add("eventId", Long.toString(eventId)).
                                    add("userEmail", userEmail).
                                    add("assistance", Boolean.toString(assistance)).build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientResponseDtoConversor.toClientResponseDto(response.getEntity().getContent()).getEventId();

        } catch (InstanceNotFoundException | InputValidationException | ClientEventAlreadyCanceledException |
                 ClientExistAnsweredForEventException | ClientDateResponseExpirationException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //FUNC-2
    @Override
    public List<ClientEventDto> findEventsByKeyword(String keyword, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            if (keyword == null){
                HttpResponse response = Request.Get(getEndpointAddress() + "events?endDate="+ URLEncoder.encode(endDate.toString(), "UTF-8")).execute().returnResponse();

                validateStatusCode(HttpStatus.SC_OK, response);

                return JsonToClientEventDtoConversor.toClientEventDtos(response.getEntity()
                        .getContent());
            }
            else{
                HttpResponse response = Request.Get(getEndpointAddress() + "events?keyword="+ URLEncoder.encode(keyword, "UTF-8") +"&endDate="+ URLEncoder.encode(endDate.toString(), "UTF-8")).execute().returnResponse();

                validateStatusCode(HttpStatus.SC_OK, response);

                return JsonToClientEventDtoConversor.toClientEventDtos(response.getEntity()
                        .getContent());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //FUNC-5
    @Override
    public void cancelEvent(Long eventId) throws InstanceNotFoundException, ClientEventAlreadyCanceledException, ClientDatePassedException {
        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "events/"+ eventId +"/cancel").execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

        } catch (InstanceNotFoundException | ClientEventAlreadyCanceledException | ClientDatePassedException ex){
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //FUNC-6
    @Override
    public List<ClientResponseDto> findAnswerEvent(String userEmail, boolean onlyAffirmative) throws InputValidationException{

        try{

            HttpResponse response = Request.Get(getEndpointAddress() + "responses/"+ "?userEmail=" + userEmail + "&onlyAffirmative=" + onlyAffirmative).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientResponseDtoConversor.toClientResponseDtos(response.getEntity().getContent());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress() {
        if(endpointAddress == null){
            endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientEventDto event) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientEventDtoConversor.toObjectNode(event));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, org.apache.http.HttpResponse response) throws Exception {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_FORBIDDEN:
                    throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = " + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
