package es.udc.ws.app.model.event;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface SqlEventDao {

    public Event create(Connection connection, Event event);

    public Event findEvent(Connection connection, Long eventId)
            throws InstanceNotFoundException;

    public List<Event> findEventsByKeyword(Connection connection, String keyword, LocalDateTime dateFrom, LocalDateTime dateTo)
            throws InstanceNotFoundException;

    public void update(Connection connection, Event event)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long eventId)
            throws InstanceNotFoundException;
}