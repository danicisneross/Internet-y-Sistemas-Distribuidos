package es.udc.ws.app.test.model.appservice;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import es.udc.ws.app.model.eventService.exceptions.DatePassed;
import es.udc.ws.app.model.eventService.exceptions.DateResponseExpiration;
import es.udc.ws.app.model.eventService.exceptions.EventAlreadyCanceled;
import es.udc.ws.app.model.eventService.exceptions.ExistAnsweredForEvent;
import es.udc.ws.app.model.response.Response;
import es.udc.ws.app.model.event.SqlEventDaoFactory;
import es.udc.ws.app.model.response.SqlResponseDao;
import es.udc.ws.app.model.response.SqlResponseDaoFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import es.udc.ws.app.model.event.Event;
import es.udc.ws.app.model.eventService.EventService;
import es.udc.ws.app.model.eventService.EventServiceFactory;
import es.udc.ws.app.model.event.SqlEventDao;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;

public class AppServiceTest {
    private final long NON_EXISTENT_EVENT_ID = -1;

    private final String VALID_USER_EMAIL = "ejemplo@ejemplo.com";
    private final String USER_ID = "ws-user";
    private final String INVALID_USER_EMAIL = "";
    private static EventService eventService = null;
    private static SqlEventDao eventDao = null;

    private static SqlResponseDao responseDao = null;

    private static DataSource dataSource = null;

    @BeforeAll
    public static void init() {

        dataSource = new SimpleDataSource();

        /* Add "dataSource" to "DataSourceLocator". */
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

        eventService = EventServiceFactory.getService();

        eventDao = SqlEventDaoFactory.getDao();

        responseDao = SqlResponseDaoFactory.getDao();
    }

    private Event getValidEvent() {
        return new Event("name", "description", LocalDateTime.now(), LocalDateTime.of(2023, 10, 21, 11, 24, 0), 4, 5L, false, 0, 0);
    }
    private Event getValidEvent(String name) {
        return new Event(name, "description", LocalDateTime.now(), LocalDateTime.of(2023, 10, 21, 11, 24, 0), 4, 5L, false, 0, 0);
    }

    private Event createEvent(Event event){

        Event addedEvent = null;
        try {
            addedEvent = eventService.registerEvent(event);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedEvent;
    }

    private Event findEvent(Long eventId){
        try(Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Event foundEvent = eventDao.findEvent(connection, eventId);
                connection.commit();

                return foundEvent;
            } catch (InstanceNotFoundException e) {
                throw new RuntimeException(e);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void removeEvent(Long eventId) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try(Connection connection = dataSource.getConnection()) {
            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                eventDao.remove(connection, eventId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeResponse(Long responseId) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                responseDao.remove(connection, responseId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRegisterEventAndFindEvent() throws InputValidationException, InstanceNotFoundException {

        Event event = getValidEvent();
        Event addedEvent = null;

        try {

            // Create Event
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

            addedEvent = eventService.registerEvent(event);

            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            //Find Event
            Event foundEvent = eventService.findEvent(addedEvent.getEventId());

            assertEquals(addedEvent, foundEvent);
            assertEquals(foundEvent.getName(), event.getName());
            assertEquals(foundEvent.getDescription(), event.getDescription());
            assertTrue((foundEvent.getCreationDate().compareTo(beforeCreationDate) >= 0)
                    && (foundEvent.getCreationDate().compareTo(afterCreationDate) <= 0));
            assertEquals(foundEvent.getDuration(), event.getDuration());
            assertTrue(foundEvent.getStartDate().compareTo(foundEvent.getCreationDate()) >= 0);
            assertEquals(foundEvent.isCancelation(), event.isCancelation());
            assertEquals(foundEvent.getNumberAssitance(), event.getNumberAssitance());
            assertEquals(foundEvent.getNumberNoAssitance(), event.getNumberNoAssitance());

        } finally {
            // Clear Database
            if (addedEvent!=null) {
                removeEvent(addedEvent.getEventId());
            }
        }
    }
    @Test
    public void testRegisterInvalidEvent() {

        // Check event name not null
        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setName(null);
            Event addedEvent = eventService.registerEvent(event);
            removeEvent(addedEvent.getEventId());
        });

        // Check event name not empty
        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setName("");
            Event addedEvent = eventService.registerEvent(event);
            removeEvent(addedEvent.getEventId());
        });

        // Check event description not null
        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setDescription(null);
            Event addedEvent = eventService.registerEvent(event);
            removeEvent(addedEvent.getEventId());
        });

        // Check event description not empty
        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setDescription("");
            Event addedEvent = eventService.registerEvent(event);
            removeEvent(addedEvent.getEventId());
        });

        // Check event startDate not null
        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setStartDate(null);
            Event addedEvent = eventService.registerEvent(event);
            removeEvent(addedEvent.getEventId());
        });

        // Check event duration >= 0
        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setDuration(-1.0);
            Event addedEvent = eventService.registerEvent(event);
            removeEvent(addedEvent.getEventId());
        });
    }

    @Test
    public void testFindNonExistedEvent() {
        assertThrows(InstanceNotFoundException.class, () -> eventService.findEvent(NON_EXISTENT_EVENT_ID));
    }

    @Test
    public void testCancelEvent() throws InputValidationException, InstanceNotFoundException, EventAlreadyCanceled, DatePassed {

        Event event = getValidEvent();
        try {

            // Create Event
            event = createEvent(event);
            Event foundEvent = findEvent(event.getEventId());
            eventService.cancelEvent(foundEvent.getEventId());

            //Find Event
            Event canceledEvent = eventService.findEvent(foundEvent.getEventId());

            assertEquals(canceledEvent.getEventId(), foundEvent.getEventId());
            assertEquals(canceledEvent.getName(), foundEvent.getName());
            assertEquals(canceledEvent.getDescription(), foundEvent.getDescription());
            assertEquals(canceledEvent.getNumberAssitance(), foundEvent.getNumberAssitance());
            assertEquals(canceledEvent.getNumberNoAssitance(), foundEvent.getNumberNoAssitance());
            assertTrue(canceledEvent.isCancelation());

        } catch (DatePassed e) {
            throw new RuntimeException(e);
        } finally {
            // Clear Database
            removeEvent(event.getEventId());
        }
    }

    @Test
    public void testFindEvents() throws InputValidationException, InstanceNotFoundException, EventAlreadyCanceled {
        // Add Events
        List<Event> events = new LinkedList<Event>();
        //0
        Event event1 =createEvent(getValidEvent("Charla Igualdad de Genero"));
        event1.setDescription("Charla sobre la igualdad de genero en el ambito laboral");
        events.add(event1);
        //1
        Event event2 = createEvent(getValidEvent("Conociendo Coruña"));
        event2.setDescription("Paseo desde paseo maritimo de Coruña y visita a la Torre de Hercules");
        events.add(event2);
        //2
        Event event3 = createEvent(getValidEvent("Cultura"));
        event3.setDescription("Visita museos de Coruña");
        events.add(event3);

        try {
            List<Event> foundEvents = eventService.findEventsByKeyword(null,LocalDateTime.of(2021,1,1,1,0,0),LocalDateTime.of(2050,12,31,12,59,0));
            assertEquals(events.size(), foundEvents.size());


        }finally {
            // Clear Database
            for (Event event: events) {
                removeEvent(event.getEventId());
            }
        }
    }

    @Test
    public void testAnswerEvent() throws InstanceNotFoundException, InputValidationException, DateResponseExpiration, EventAlreadyCanceled, ExistAnsweredForEvent {

        Event event = createEvent(getValidEvent());
        LocalDateTime fechaCreationDate = LocalDateTime.now().withNano(0);
        LocalDateTime fechaStartDate = LocalDateTime.now().withNano(0).plusDays(8);
        Event event1 = createEvent(new Event("Cumple Ana", "Fiesta de 20", fechaCreationDate, fechaStartDate, 5, 8L, false, 0, 0));
        List<Response> responses = new LinkedList<Response>();

        try {

            // Answer to an event
            Response response0 = eventService.answerEvents(event.getEventId(), VALID_USER_EMAIL, true);
            Long eventoId = response0.getEventId();
            Event evento = eventService.findEvent(eventoId);
            int numeroA = evento.getNumberAssitance();
            responses.add(response0);
            Response response1 = eventService.answerEvents(event1.getEventId(), VALID_USER_EMAIL, false);
            Long eventoId2 = response1.getEventId();
            Event evento2 = eventService.findEvent(eventoId2);
            int numeroA2 = evento2.getNumberNoAssitance();
            responses.add(response1);

            // Find Response
            List<Response> foundResponse = eventService.findAnswerEvent(VALID_USER_EMAIL, false);

            // Check Answers
            assertEquals(foundResponse.get(0).getEventId(), responses.get(0).getEventId());
            assertEquals(foundResponse.get(0).getUserEmail(), responses.get(0).getUserEmail());
            assertEquals(foundResponse.get(0).getResponseId(), responses.get(0).getResponseId());
            assertEquals(foundResponse.get(0).getDateAnswer(), responses.get(0).getDateAnswer());
            assertEquals(foundResponse.get(0).isAssistance(), responses.get(0).isAssistance());
            assertEquals( 1, numeroA);
            assertEquals(1, numeroA2);

        } finally {
            // Clear database: remove sale (if created) and movie
            removeResponse(responses.get(0).getResponseId());
            removeResponse(responses.get(1).getResponseId());
            removeEvent(event.getEventId());
            removeEvent(event1.getEventId());
        }
    }


    @Test
    public void testFindAnswerEvent() throws InstanceNotFoundException, InputValidationException, EventAlreadyCanceled, ExistAnsweredForEvent, DateResponseExpiration {

        String userEmail1 = "email1@email.com";
        String userEmail2 = "email2@email.com";

        LocalDateTime fechaCreationDate = LocalDateTime.now().withNano(0);
        LocalDateTime fechaStartDate = LocalDateTime.now().withNano(0).plusDays(3);
        Event event1 = createEvent(getValidEvent());
        Event event2 = createEvent(new Event("event2", "evento2", fechaCreationDate, fechaStartDate, 2, 1L, false, 2, 0));
        Event event3 = createEvent(new Event("event3", "evento3", fechaCreationDate, fechaStartDate.plusDays(5), 2, 2L, true, 29, 3));
        Event event4 = createEvent(new Event("event4", "evento4", fechaCreationDate, fechaStartDate.plusDays(7), 2, 3L, false, 17, 8));

        Response response0 = eventService.answerEvents(event4.getEventId(), userEmail1, true);
        Response response1 = eventService.answerEvents(event1.getEventId(), userEmail1, true);
        Response response2 = eventService.answerEvents(event2.getEventId(), userEmail1, false);
        Response response3 = eventService.answerEvents(event3.getEventId(), userEmail1, false);

        List<Response> todasResponses = new LinkedList<>();
        List<Response> responsesAffirmatives = new LinkedList<>();


        try {
            // lista de todas las respuestas (afirmativas y negativas)
            todasResponses.add(response0);
            todasResponses.add(response1);
            todasResponses.add(response2);
            todasResponses.add(response3);

            //lista de respuestas afirmativas
            responsesAffirmatives.add(response0);
            responsesAffirmatives.add(response1);


            List<Response> ListFoundAnswerAffirmatives = eventService.findAnswerEvent(userEmail1, true);

            // Check Answers Affirmatives
            assertEquals(responsesAffirmatives, ListFoundAnswerAffirmatives);
            assertEquals(responsesAffirmatives.get(0).getEventId(), ListFoundAnswerAffirmatives.get(0).getEventId());
            assertEquals(responsesAffirmatives.get(0).getUserEmail(), ListFoundAnswerAffirmatives.get(0).getUserEmail());
            assertEquals(responsesAffirmatives.get(0).getResponseId(), ListFoundAnswerAffirmatives.get(0).getResponseId());
            assertEquals(responsesAffirmatives.get(0).getDateAnswer(), ListFoundAnswerAffirmatives.get(0).getDateAnswer());
            assertEquals(responsesAffirmatives.get(0).isAssistance(), ListFoundAnswerAffirmatives.get(0).isAssistance());

            assertEquals(responsesAffirmatives.get(1).getEventId(), ListFoundAnswerAffirmatives.get(1).getEventId());
            assertEquals(responsesAffirmatives.get(1).getUserEmail(), ListFoundAnswerAffirmatives.get(1).getUserEmail());
            assertEquals(responsesAffirmatives.get(1).getResponseId(), ListFoundAnswerAffirmatives.get(1).getResponseId());
            assertEquals(responsesAffirmatives.get(1).getDateAnswer(), ListFoundAnswerAffirmatives.get(1).getDateAnswer());
            assertEquals(responsesAffirmatives.get(1).isAssistance(), ListFoundAnswerAffirmatives.get(1).isAssistance());


            List<Response> ListFoundAnswer = eventService.findAnswerEvent(userEmail1, false);

            // Check Answers Affirmatives
            assertEquals(todasResponses, ListFoundAnswer);
            assertEquals(todasResponses.get(0).getEventId(), ListFoundAnswer.get(0).getEventId());
            assertEquals(todasResponses.get(0).getUserEmail(), ListFoundAnswer.get(0).getUserEmail());
            assertEquals(todasResponses.get(0).getResponseId(), ListFoundAnswer.get(0).getResponseId());
            assertEquals(todasResponses.get(0).getDateAnswer(), ListFoundAnswer.get(0).getDateAnswer());
            assertEquals(todasResponses.get(0).isAssistance(), ListFoundAnswer.get(0).isAssistance());

            assertEquals(todasResponses.get(1).getEventId(), ListFoundAnswer.get(1).getEventId());
            assertEquals(todasResponses.get(1).getUserEmail(), ListFoundAnswer.get(1).getUserEmail());
            assertEquals(todasResponses.get(1).getResponseId(), ListFoundAnswer.get(1).getResponseId());
            assertEquals(todasResponses.get(1).getDateAnswer(), ListFoundAnswer.get(1).getDateAnswer());
            assertEquals(todasResponses.get(1).isAssistance(), ListFoundAnswer.get(1).isAssistance());

            assertEquals(todasResponses.get(2).getEventId(), ListFoundAnswer.get(2).getEventId());
            assertEquals(todasResponses.get(2).getUserEmail(), ListFoundAnswer.get(2).getUserEmail());
            assertEquals(todasResponses.get(2).getResponseId(), ListFoundAnswer.get(2).getResponseId());
            assertEquals(todasResponses.get(2).getDateAnswer(), ListFoundAnswer.get(2).getDateAnswer());
            assertEquals(todasResponses.get(2).isAssistance(), ListFoundAnswer.get(2).isAssistance());

            assertEquals(todasResponses.get(3).getEventId(), ListFoundAnswer.get(3).getEventId());
            assertEquals(todasResponses.get(3).getUserEmail(), ListFoundAnswer.get(3).getUserEmail());
            assertEquals(todasResponses.get(3).getResponseId(), ListFoundAnswer.get(3).getResponseId());
            assertEquals(todasResponses.get(3).getDateAnswer(), ListFoundAnswer.get(3).getDateAnswer());
            assertEquals(todasResponses.get(3).isAssistance(), ListFoundAnswer.get(3).isAssistance());

            //si el email del usuario es un null
            assertThrows(InputValidationException.class, () ->{
                eventService.findAnswerEvent(null, true);
            });

        } finally {
            removeResponse(response0.getResponseId());
            removeResponse(response1.getResponseId());
            removeResponse(response2.getResponseId());
            removeResponse(response3.getResponseId());
            removeEvent(event1.getEventId());
            removeEvent(event2.getEventId());
            removeEvent(event3.getEventId());
            removeEvent(event4.getEventId());

        }
    }

}
