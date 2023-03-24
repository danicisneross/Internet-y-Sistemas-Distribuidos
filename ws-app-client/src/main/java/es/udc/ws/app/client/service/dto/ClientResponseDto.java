package es.udc.ws.app.client.service.dto;

public class ClientResponseDto {
    private Long responseId;
    private Long eventId;
    private String userEmail;
    private boolean assistance;

    public ClientResponseDto(Long responseId, Long eventId, String userEmail, boolean assistance){
        this.responseId = responseId;
        this.eventId = eventId;
        this.userEmail = userEmail;
        this.assistance = assistance;
    }

    public Long getResponseId(){return responseId;}
    public void setResponseId(Long responseId){this.responseId = responseId;}
    public Long getEventId(){return eventId;}
    public void setEventId(Long eventId){this.eventId = eventId;}

    public  String getUserEmail(){return userEmail;}
    public void setUserEmail(String userEmail){this.userEmail = userEmail;}

    public boolean isAssistance() {return assistance;}

    public void setAssistance(boolean assistance){this.assistance = assistance;}

    @Override
    public String toString() {
        return "ResponseDto [responseId=" + responseId + ", eventId=" + eventId
                + ", userEmail=" + userEmail + ", assistance= " + assistance + "]";
    }
}