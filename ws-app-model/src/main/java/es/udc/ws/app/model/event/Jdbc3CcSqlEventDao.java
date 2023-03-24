package es.udc.ws.app.model.event;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

public class Jdbc3CcSqlEventDao extends AbstractSqlEventDao{

    @Override
    public Event create(Connection connection, Event event) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Event"
                + " (name, description, creationDate, startDate, duration, cancelation, numberAssistance, numberNoAssistance)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, event.getName());
            preparedStatement.setString(i++, event.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(event.getCreationDate()));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(event.getStartDate()));
            preparedStatement.setDouble(i++, event.getDuration());
            preparedStatement.setBoolean(i++, event.isCancelation());
            preparedStatement.setInt(i++, event.getNumberAssitance());
            preparedStatement.setInt(i++, event.getNumberNoAssitance());


            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(!resultSet.next()){
                throw new SQLException("JDBC driver did not return generated key.");
            }
            Long eventId = resultSet.getLong(1);

            /* Return event. */
            return new Event(event.getName(), event.getDescription(), event.getCreationDate(),
                    event.getStartDate(), event.getDuration(), eventId, event.isCancelation(),
                    event.getNumberAssitance(), event.getNumberNoAssitance());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}