package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.response.Response;
import es.udc.ws.app.thrift.ThriftResponseDto;

public class ResponseToThriftResponseDtoConversor {

    public static ThriftResponseDto toThriftResponseDto (Response response){
        return new ThriftResponseDto(response.getResponseId(), response.getEventId(), response.getUserEmail(), response.isAssistance());
    }
}
