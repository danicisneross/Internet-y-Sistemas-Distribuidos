package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientEventDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientEventDtoConversor {
    public static ObjectNode toObjectNode(ClientEventDto event) throws IOException {

        ObjectNode eventObject = JsonNodeFactory.instance.objectNode();

        eventObject.put("name",event.getName()).
                put("description",event.getDescription()).
                put("startDate", event.getStartDate().toString()).
                put("duration", Duration.between(event.getStartDate(), event.getEndDate()).toHours());

        return eventObject;
    }

    public static ClientEventDto toClientEventDto(InputStream jsonEvent) throws ParsingException{
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonEvent);
            if(rootNode.getNodeType()!= JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            }else{
                return toClientEventDto(rootNode);
            }
        }catch (ParsingException e){
            throw e;
        }catch (Exception e){
            throw new ParsingException(e);
        }

    }
    public static List<ClientEventDto> toClientEventDtos(InputStream jsonEvents)throws ParsingException{
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonEvents);
            if(rootNode.getNodeType()!= JsonNodeType.ARRAY){
                throw new ParsingException("Unrecognized JSON (array expected)");
            }else{
                ArrayNode eventsArray = (ArrayNode) rootNode;
                List<ClientEventDto> eventDtos = new ArrayList<>(eventsArray.size());
                for(JsonNode eventNode : eventsArray){
                    eventDtos.add(toClientEventDto(eventNode));
                }
                return eventDtos;
            }

        }catch (ParsingException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    private static ClientEventDto toClientEventDto(JsonNode eventNode) throws ParsingException {
        if (eventNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode eventObject = (ObjectNode) eventNode;

            JsonNode eventIdNode = eventObject.get("eventId");
            Long eventId = eventIdNode.longValue();

            String name = eventObject.get("name").textValue().trim();
            String description = eventObject.get("description").textValue().trim();
            String startDateString = eventObject.get("startDate").textValue().trim();
            LocalDateTime startDate = LocalDateTime.parse(startDateString);
            double duration = eventObject.get("duration").doubleValue();
            LocalDateTime endDate = startDate.plusSeconds((long) (duration*3600));

            boolean cancelation = eventObject.get("cancelation").booleanValue();
            int numberAssistance = eventObject.get("numberAssistance").intValue();
            int numberTotalResponses = eventObject.get("numberTotalResponses").intValue();

            return new ClientEventDto(name, description, startDate, endDate, eventId, cancelation, numberAssistance ,numberTotalResponses);

        }
    }
}