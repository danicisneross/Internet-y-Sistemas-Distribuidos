package es.udc.ws.app.model.response;

import java.sql.*;

public class Jdbc3CcSqlResponseDao extends AbstractSqlResponseDao{

    @Override
    public Response create (Connection connection, Response response){

        /* Create "queryString". */
        String queryString = "INSERT INTO Response" +
                "(responseId, eventId, userEmail, dateAnswer, assistance)"
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString,Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, response.getResponseId());
            preparedStatement.setLong(i++, response.getEventId());
            preparedStatement.setString(i++, response.getUserEmail());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(response.getDateAnswer()));
            preparedStatement.setBoolean(i++, response.isAssistance());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(!resultSet.next()){
                throw new SQLException("JDBC driver did not return generated key");
            }

            Long responseId = resultSet.getLong(1);

            /* Return response. */
            return new Response(responseId, response.getEventId(), response.getUserEmail(), response.getDateAnswer(), response.isAssistance());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
