package es.udc.ws.app.model.response;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlResponseDao {
    public Response create(Connection connection, Response response);

    public List<Response> findResponse(Connection connection, String userEmail, boolean onlyAffirmative);

    public void remove(Connection connection, Long responseId) throws InstanceNotFoundException;

    public boolean existResponseForEvent(Connection connection, String userEmail, Long eventId);

}
