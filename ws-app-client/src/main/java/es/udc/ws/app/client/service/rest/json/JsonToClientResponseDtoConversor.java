package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientResponseDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientResponseDtoConversor {
    public static ClientResponseDto toClientResponseDto(InputStream jsonResponse) throws ParsingException{
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected");
            } else {
                ObjectNode eventObject = (ObjectNode) rootNode;

                JsonNode responseIdNode = eventObject.get("responseId");
                Long responseId = (responseIdNode != null) ? responseIdNode.longValue() : null;

                Long eventId = eventObject.get("eventId").longValue();
                String userEmail = eventObject.get("userEmail").textValue().trim();
                boolean assistance = eventObject.get("assistance").booleanValue();

                return new ClientResponseDto(responseId, eventId, userEmail, assistance);
            }
        } catch (ParsingException ex) {
            throw ex;
        }catch (Exception e){
            throw new ParsingException(e);
        }
    }
    public static List<ClientResponseDto> toClientResponseDtos(InputStream jsonResponses) throws ParsingException {
        try{

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonResponses);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY){
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode responsesArray = (ArrayNode) rootNode;
                List<ClientResponseDto> responseDtos = new ArrayList<>(responsesArray.size());
                for (JsonNode responseNode : responsesArray) {
                    responseDtos.add(toClientResponseDto(responseNode));
                }
                return responseDtos;
            }

        } catch (ParsingException ex){
            throw ex;
        } catch (Exception e){
            throw new ParsingException(e);
        }
    }

    private static ClientResponseDto toClientResponseDto(JsonNode responseNode) throws ParsingException {
        if (responseNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode eventObject = (ObjectNode) responseNode;

            JsonNode responseIdNode = eventObject.get("responseId");
            Long responseId = (responseIdNode != null) ? responseIdNode.longValue() : null;

            Long eventId = eventObject.get("eventId").longValue();
            String userEmail = eventObject.get("userEmail").textValue().trim();
            boolean assistance = eventObject.get("assistance").booleanValue();

            return new ClientResponseDto(responseId, eventId, userEmail, assistance);
        }
    }
}
