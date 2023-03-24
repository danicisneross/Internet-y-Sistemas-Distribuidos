package es.udc.ws.app.model.eventService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import es.udc.ws.app.model.event.Event;
import es.udc.ws.app.model.event.SqlEventDao;
import es.udc.ws.app.model.event.SqlEventDaoFactory;
import es.udc.ws.app.model.eventService.exceptions.DateResponseExpiration;
import es.udc.ws.app.model.eventService.exceptions.EventAlreadyCanceled;
import es.udc.ws.app.model.eventService.exceptions.DatePassed;
import es.udc.ws.app.model.eventService.exceptions.ExistAnsweredForEvent;
import es.udc.ws.app.model.response.Response;
import es.udc.ws.app.model.response.SqlResponseDao;
import es.udc.ws.app.model.response.SqlResponseDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import static es.udc.ws.app.model.util.ModelConstants.*;

public class EventServiceImpl implements EventService {
    private final DataSource dataSource;
    private SqlEventDao eventDao = null;
    private SqlResponseDao responseDao = null;

    public EventServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        eventDao = SqlEventDaoFactory.getDao();
        responseDao = SqlResponseDaoFactory.getDao();
    }

    private void validateEvent(Event event) throws InputValidationException {

        PropertyValidator.validateMandatoryString("name", event.getName());
        PropertyValidator.validateMandatoryString("description", event.getDescription());
        PropertyValidator.validateDouble("duration",event.getDuration(), 0, MAX_DURATION);

    }

    private void validateStartDate(String name, LocalDateTime startdate, LocalDateTime creationDate) throws InputValidationException{
        if (startdate == null){
            throw new InputValidationException("Invalid " + name + " value can't be null");
        }
        if (startdate.isBefore(creationDate)){
            throw new InputValidationException("You can't create an Event with the startDate \"" +
                    startdate + "\" prior to its creationDate.");
        }
    }


    @Override
    public Event registerEvent(Event event) throws InputValidationException {

        validateEvent(event);
        event.setCreationDate(LocalDateTime.now().withNano(0));
        validateStartDate("startDate", event.getStartDate(), event.getCreationDate());
        event.setNumberAssitance(0);
        event.setNumberNoAssitance(0);
        event.setCancelation(false);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Event createdEvent = eventDao.create(connection, event);

                /* Commit. */
                connection.commit();

                return createdEvent;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Event findEvent(Long eventId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            return eventDao.findEvent(connection, eventId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> findEventsByKeyword(String keyword, LocalDateTime dateIni, LocalDateTime dateFin){
        try (Connection connection = dataSource.getConnection()){
            return eventDao.findEventsByKeyword(connection, keyword, dateIni, dateFin);
        } catch (SQLException | InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelEvent(Long eventId) throws InstanceNotFoundException, EventAlreadyCanceled, DatePassed{

        try(Connection connection = dataSource.getConnection()){

            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);
            Event foundEvent = eventDao.findEvent(connection,eventId);

            try {


                if (foundEvent.isCancelation()) {
                    throw new EventAlreadyCanceled("This event is already canceled");
                }
                if (foundEvent.getStartDate().compareTo(LocalDateTime.now().withNano(0)) < 0) {
                    throw new DatePassed("It is not possible to cancel the event because it has already happened");
                }

                foundEvent.setCancelation(true);

                eventDao.update(connection, foundEvent);

                connection.commit();


            }catch (InstanceNotFoundException | EventAlreadyCanceled | DatePassed err){
                connection.commit();
                throw err;
            }catch (SQLException | RuntimeException | Error err){
                connection.rollback();
                throw new RuntimeException(err);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response answerEvents(Long eventId, String userEmail, boolean assistance) throws InstanceNotFoundException, InputValidationException, DateResponseExpiration, EventAlreadyCanceled, ExistAnsweredForEvent {

        if((eventId != null) && (userEmail != null)){
            PropertyValidator.validateNotNegativeLong("eventId", eventId);
            PropertyValidator.validateMandatoryString("userEmail", userEmail);


            try (Connection connection = dataSource.getConnection()){
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                Event foundEvent = eventDao.findEvent(connection,eventId);
                LocalDateTime dateAnswer = LocalDateTime.now().withNano(0);

                try{
                    if(!(foundEvent.isCancelation())){
                        if(!(responseDao.existResponseForEvent(connection, userEmail, eventId))){
                            if((foundEvent.getStartDate().isAfter(dateAnswer.plusDays(1)))){
                                Response response = responseDao.create(connection, new Response(eventId, userEmail, dateAnswer, assistance));
                                if(response.isAssistance()){
                                    foundEvent.setNumberAssitance(foundEvent.getNumberAssitance() + 1);
                                } else {
                                    foundEvent.setNumberNoAssitance(foundEvent.getNumberNoAssitance() + 1);
                                }
                                eventDao.update(connection,foundEvent);
                                connection.commit();
                                return response;
                            } else {
                                throw new DateResponseExpiration("The time to respond to this event is already passed");
                            }
                        } else {
                            throw new ExistAnsweredForEvent("It is not possible to respond to which you have already responded");
                        }
                    } else{
                        throw new EventAlreadyCanceled("It is not possible to respond to an event that is canceled");
                    }
                }catch (DateResponseExpiration | ExistAnsweredForEvent | EventAlreadyCanceled  error){
                    connection.commit();
                    throw error;
                }catch (RuntimeException | Error error){
                    connection.rollback();
                    throw new RuntimeException(error);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new InputValidationException("The eventId or userMail entered does not exist");
        }
    }

    @Override
    public List<Response> findAnswerEvent(String userEmail, boolean onlyAffirmative) throws InputValidationException{

        if (userEmail != null)
            PropertyValidator.validateMandatoryString("userEmail", userEmail);

        else throw new InputValidationException("The userMail entered do not exist");

        try(Connection connection = dataSource.getConnection()){
            return responseDao.findResponse(connection, userEmail, onlyAffirmative);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}