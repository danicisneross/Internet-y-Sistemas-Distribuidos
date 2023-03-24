package es.udc.ws.app.restservice.dto;

import java.util.Objects;

public class RestResponseDto {

    private Long responseId;
    private Long eventId;
    private String userEmail;
    private boolean assistance;

    public RestResponseDto(){

    }

    public RestResponseDto(Long responseId, Long eventId, String userEmail, boolean assistance){
        this.responseId = responseId;
        this.eventId = eventId;
        this.userEmail = userEmail;
        this.assistance = assistance;
    }

    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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
        RestResponseDto that = (RestResponseDto) o;
        return assistance == that.assistance && Objects.equals(responseId, that.responseId) && Objects.equals(eventId, that.eventId) && Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseId, eventId, userEmail, assistance);
    }

    @Override
    public String toString() {
        return "RestResponseDto{" +
                "responseId=" + responseId +
                ", eventId=" + eventId +
                ", userEmail='" + userEmail + '\'' +
                ", assistance=" + assistance +
                '}';
    }
}
