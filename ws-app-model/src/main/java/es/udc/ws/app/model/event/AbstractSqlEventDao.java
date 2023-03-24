package es.udc.ws.app.model.event;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlEventDao implements SqlEventDao {

    protected AbstractSqlEventDao(){}

    @Override
    public Event findEvent(Connection connection, Long eventId) throws InstanceNotFoundException {

        /* Create "queryString" */
        String queryString = "SELECT name, description, "
                + "creationDate, startDate, duration, cancelation, numberAssistance, numberNoAssistance FROM Event WHERE eventId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement" */
            int i = 1;
            preparedStatement.setLong(i++, eventId.longValue());

            /* Execute query */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(eventId, Event.class.getName());
            }

            /* Get results */
            i = 1;
            String name = resultSet.getString(i++);
            String description = resultSet.getString(i++);
            Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime creationDate = creationDateAsTimestamp.toLocalDateTime();
            Timestamp startDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime starDate = startDateAsTimestamp.toLocalDateTime();
            double duration = resultSet.getDouble(i++);
            boolean cancelation = resultSet.getBoolean(i++);
            int numberAssistance = resultSet.getInt(i++);
            int numberNoAssistance = resultSet.getInt(i++);

            /* Return Event */
            return new Event(name, description, creationDate, starDate, duration, eventId, cancelation, numberAssistance, numberNoAssistance);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Event> findEventsByKeyword(Connection connection, String keyword, LocalDateTime dateFrom, LocalDateTime dateTo) {

        String queryString = "Select  name, description, creationDate, startDate, duration, eventId, cancelation, numberAssistance, numberNoAssistance " +
                "FROM Event WHERE startDate >= ? AND startDate <= ?";

        if(keyword != null){
            queryString += " AND LOWER(description) LIKE LOWER(?) ";
        }

        Timestamp dateFromTimestamp = Timestamp.valueOf(dateFrom.withNano(0));
        Timestamp dateToTimestamp = Timestamp.valueOf(dateTo.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1).withNano(0));

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setTimestamp(i++,dateFromTimestamp);
            preparedStatement.setTimestamp(i++,dateToTimestamp);
            if(keyword != null){
                preparedStatement.setString(i++,"%" + keyword + "%");
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Event> events = new ArrayList<Event>();

            while (resultSet.next()) {

                i = 1;
                String name = resultSet.getString(i++);
                String description = resultSet.getString(i++);
                Timestamp creationDateAsStamp = resultSet.getTimestamp(i++);
                LocalDateTime creationDate = creationDateAsStamp.toLocalDateTime();
                Timestamp startDateAsStamp = resultSet.getTimestamp(i++);
                LocalDateTime startDate = startDateAsStamp.toLocalDateTime();
                double duration = resultSet.getDouble(i++);
                Long eventId = resultSet.getLong(i++);
                boolean cancelation = resultSet.getBoolean(i++);
                int numAssistance = resultSet.getInt(i++);
                int numNoAssistance = resultSet.getInt(i++);


                events.add(new Event(name, description, creationDate, startDate, duration, eventId, cancelation, numAssistance,numNoAssistance));

            }

            return events;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Event event) throws InstanceNotFoundException {
        String query = "UPDATE Event SET name = ?,description = ?, " +
                "creationDate = ?, startDate = ?, duration = ?, cancelation = ?, numberAssistance = ?, numberNoAssistance = ?" +
                " WHERE eventId = ? ";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            int i = 1;

            preparedStatement.setString(i++,event.getName());
            preparedStatement.setString(i++,event.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(event.getCreationDate()));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(event.getStartDate()));
            preparedStatement.setDouble(i++,event.getDuration());
            preparedStatement.setBoolean(i++,event.isCancelation());
            preparedStatement.setInt(i++,event.getNumberAssitance());
            preparedStatement.setInt(i++,event.getNumberNoAssitance());
            preparedStatement.setLong(i++,event.getEventId());

            int updatedRows = preparedStatement.executeUpdate();

            if(updatedRows== 0){
                throw new InstanceNotFoundException(event.getEventId(),Event.class.getName());
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long eventId) throws InstanceNotFoundException{
            String query = "DELETE FROM Event WHERE" + " eventId = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            int i = 1;

            preparedStatement.setLong(i++,eventId);

            //Ejecutar query
            int removedRows = preparedStatement.executeUpdate();

            if(removedRows == 0){
                throw new InstanceNotFoundException(eventId,Event.class.getName());
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
