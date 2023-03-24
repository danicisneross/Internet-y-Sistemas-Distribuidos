package es.udc.ws.app.model.response;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlResponseDao implements SqlResponseDao{
    protected AbstractSqlResponseDao(){}

    @Override
    public List<Response> findResponse(Connection connection, String userEmail, boolean onlyAffirmative) {

        String queryString = "SELECT responseId, eventId, userEmail, dateAnswer, " +
                "assistance FROM Response WHERE userEmail=? ";

        if (onlyAffirmative) {
            queryString += " AND assistance = ?";
        }

        queryString += " ORDER BY dateAnswer";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setString(i++, userEmail);
            if(onlyAffirmative)
                preparedStatement.setBoolean(i++, onlyAffirmative);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Response> responsesUser = new ArrayList<Response>();

            while(resultSet.next()){
                int j = 1;
                Long responseId = resultSet.getLong(j++);
                Long eventId = resultSet.getLong(j++);
                String email = resultSet.getString(j++);
                Timestamp dateAnswerAsTimestamp = resultSet.getTimestamp(j++);
                LocalDateTime dateAnswer = dateAnswerAsTimestamp.toLocalDateTime();
                Boolean assist = resultSet.getBoolean(j++);

                Response response = new Response(responseId, eventId, email, dateAnswer, assist);
                responsesUser.add(response);
            }
            return responsesUser;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(Connection connection, Long responseId) throws InstanceNotFoundException{
        String queryString = "DELETE FROM Response WHERE" + " responseId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, responseId);


            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(responseId,
                        Response.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existResponseForEvent(Connection connection, String userEmail, Long eventId){

        /* Create "queryString". */
        String queryString = "SELECT COUNT(*) FROM Response WHERE eventId = ? AND userEmail = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, eventId.longValue());
            preparedStatement.setString(i++, userEmail);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                throw new SQLException("Error");
            }

            /* Get results. */
            i = 1;
            Long contOfResponses = resultSet.getLong(i++);

            /* Return result. */
            return contOfResponses > 0;

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

}
