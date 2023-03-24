package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestResponseDto;

import java.util.List;

public class JsonToRestResponseDtoConversor {

    public static ObjectNode toObjectNode(RestResponseDto response){

        ObjectNode responseNode = JsonNodeFactory.instance.objectNode();

        if(response.getResponseId() != null){
            responseNode.put("responseId", response.getResponseId());
        }
        responseNode.put("eventId", response.getEventId()).
                put("userEmail", response.getUserEmail()).
                put("assistance", response.isAssistance());

        return responseNode;
    }

    public static ArrayNode toArrayNode(List<RestResponseDto> responses){

        ArrayNode responsesNode = JsonNodeFactory.instance.arrayNode();
        for(int i = 0; i < responses.size(); i++){
            RestResponseDto responseDto = responses.get(i);
            ObjectNode responseObject = toObjectNode(responseDto);
            responsesNode.add(responseObject);
        }
        return responsesNode;
    }
}
