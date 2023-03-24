package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientAppService;
import es.udc.ws.app.client.service.dto.ClientEventDto;
import es.udc.ws.app.client.service.dto.ClientResponseDto;
import es.udc.ws.app.client.service.exceptions.ClientDatePassedException;
import es.udc.ws.app.client.service.exceptions.ClientDateResponseExpirationException;
import es.udc.ws.app.client.service.exceptions.ClientEventAlreadyCanceledException;
import es.udc.ws.app.client.service.exceptions.ClientExistAnsweredForEventException;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftClientAppService implements ClientAppService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "ThriftClientAppService.endpointAddress";

    private final static String endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    @Override
    public Long registerEvent(ClientEventDto event) throws InputValidationException {
        ThriftEventService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {
            transport.open();
            return client.registerEvent(ClientEventDtoToThriftEventDtoConversor.toThriftEventDto(event)).getEventId();

        } catch (ThriftInputValidationException e){
            throw new InputValidationException(e.getMessage());
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public List<ClientEventDto> findEventsByKeyword(String keyword, LocalDateTime startDate, LocalDateTime endDate) throws InputValidationException{
        ThriftEventService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();
        try {
            transport.open();
            return ClientEventDtoToThriftEventDtoConversor.toClientEventDtos(client.findEventsByKeyword(keyword, endDate.toString()));

	} catch (ThriftInputValidationException e){
            throw new InputValidationException(e.getMessage());
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public ClientEventDto findEvent(Long eventId) throws InstanceNotFoundException{

        ThriftEventService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try{
            transport.open();
            return ClientEventDtoToThriftEventDtoConversor.toClientEventDto(client.findEvent(eventId));
        }
        catch (ThriftInstanceNotFoundException e){
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        finally {
            transport.close();
        }
    }
    @Override
    public Long answerEvents(Long eventId, String userEmail, boolean assistance) throws InstanceNotFoundException,
            InputValidationException, ClientEventAlreadyCanceledException, ClientExistAnsweredForEventException, ClientDateResponseExpirationException {

        ThriftEventService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try{
            transport.open();
            return client.answerEvent(eventId, userEmail, assistance).getResponseId();
        }
        catch (ThriftInstanceNotFoundException e){
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }
        catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        }
        catch (ThriftEventAlreadyCanceled e){
            throw new ClientEventAlreadyCanceledException(e.getMessage());
        }
        catch (ThriftExistAnsweredForEvent e){
            throw new ClientExistAnsweredForEventException(e.getMessage());
        }
        catch (ThriftDateResponseExpiration e){
            throw new ClientDateResponseExpirationException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        finally {
            transport.close();
        }
    }

    @Override
    public void cancelEvent(Long eventId) throws InstanceNotFoundException, ClientEventAlreadyCanceledException, ClientDatePassedException {}

    @Override
    public List<ClientResponseDto> findAnswerEvent(String userEmail, boolean onlyAffirmative) throws InputValidationException {
        return null;
    }


    private ThriftEventService.Client getClient(){
        try{
            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftEventService.Client(protocol);
        }
        catch (TTransportException e){
            throw new RuntimeException(e);
        }
    }
}
