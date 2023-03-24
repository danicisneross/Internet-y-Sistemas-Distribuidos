package es.udc.ws.app.model.response;

import java.time.LocalDateTime;
import java.util.Objects;

public class Response {

    private long responseId;

    private long eventId;

    private String userEmail;

    private LocalDateTime dateAnswer;

    private boolean assistance;

    public Response(Long responseId, Long eventId, String userEmail, LocalDateTime dateAnswers, boolean assistance){
        this.responseId = responseId;
        this.eventId = eventId;
        this.userEmail = userEmail;
        this.dateAnswer = (dateAnswers != null) ? dateAnswers.withNano(0) : null;
        this.assistance = assistance;
    }

    public Response(Long eventId, String userEmail, LocalDateTime dateAnswers, boolean assistance){
        this.eventId = eventId;
        this.userEmail = userEmail;
        this.dateAnswer = (dateAnswers != null) ? dateAnswers.withNano(0) : null;
        this.assistance = assistance;
    }

    public long getResponseId() {
        return responseId;
    }

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getDateAnswer() {
        return dateAnswer;
    }

    public void setDateAnswer(LocalDateTime dateAnswer) {
        this.dateAnswer = (dateAnswer != null) ? dateAnswer.withNano(0) : null;
    }

    public boolean isAssistance() {
        return assistance;
    }

    public void setAssistance(boolean assistance) {
        this.assistance = assistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return responseId == response.responseId && eventId == response.eventId && assistance == response.assistance && userEmail.equals(response.userEmail) && dateAnswer.equals(response.dateAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseId, eventId, userEmail, dateAnswer, assistance);
    }
}
