package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.response.Response;

import java.util.ArrayList;
import java.util.List;

public class ResponseToRestResponseDtoConversor {

    public static List<RestResponseDto> toRestResponseDtos(List<Response> responses){
        List<RestResponseDto> responseDtos = new ArrayList<>(responses.size());
        for(int i = 0; i < responses.size(); i++){
            Response response = responses.get(i);
            responseDtos.add(toRestResponseDto(response));
        }
        return responseDtos;
    }

    public static RestResponseDto toRestResponseDto(Response response){
        return new RestResponseDto(response.getResponseId(), response.getEventId(), response.getUserEmail(), response.isAssistance());
    }
}
