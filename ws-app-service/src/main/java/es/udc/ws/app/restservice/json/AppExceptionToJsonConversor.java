package es.udc.ws.app.restservice.json;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.eventService.exceptions.*;
public class AppExceptionToJsonConversor {
    public static ObjectNode toDatePassedException(DatePassed exception){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","DatePassed");
        exceptionObject.put("message",(exception.getMessage()));

        return exceptionObject;
    }

    public static ObjectNode toDateResponseExpirationException(DateResponseExpiration exception){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","DateResponseExpired");
        exceptionObject.put("message",(exception.getMessage()));
        return exceptionObject;
    }

    public static ObjectNode toEventAlreadyCanceledException(EventAlreadyCanceled exception){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","EventAlreadyCanceled");
        exceptionObject.put("message",(exception.getMessage()));
        return exceptionObject;
    }

    public static ObjectNode toExistAnsweredForEventException(ExistAnsweredForEvent exception){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","ExistAnsweredForEvent");
        exceptionObject.put("message",(exception.getMessage()));
        return exceptionObject;
    }

}
