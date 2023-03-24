package es.udc.ws.app.client.service.dto;

import javax.xml.stream.events.EndDocument;
import java.time.LocalDateTime;

public class ClientEventDto {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long eventId;
    private boolean cancelation;
    private int numberAssistance;
    private int numberTotalResponses;

    public ClientEventDto(String name, String description, LocalDateTime startDate, LocalDateTime endDate){
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public ClientEventDto(String name, String description, LocalDateTime startDate, LocalDateTime endDate, Long eventId, boolean cancelation, int numberAssistance, int numberTotalResponses){
        this(name,description,startDate,endDate);
        this.eventId = eventId;
        this.cancelation = cancelation;
        this.numberAssistance = numberAssistance;
        this.numberTotalResponses = numberTotalResponses;
    }
    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}

    public LocalDateTime getStartDate(){return startDate;}
    public void setStartDate(LocalDateTime startDate){this.startDate = startDate;}

    public LocalDateTime getEndDate(){return endDate;}
    public void setEndDate(LocalDateTime startDate){this.endDate = endDate;}

    public Long getEventId(){return eventId;}
    public void setEventId(Long eventId){this.eventId = eventId;}

    public boolean getCancelation(){return cancelation;}
    public void setCancelation(boolean cancelation){this.cancelation = cancelation;}

    public int getNumberAssistance(){return numberAssistance;}
    public void setNumberAssistance(int numberAssistance){this.numberAssistance = numberAssistance;}

    public int getNumberTotalResponses(){return numberTotalResponses;}
    public void setNumberTotalResponses(int numberTotalResponses){this.numberTotalResponses = numberTotalResponses;}

    @Override
    public String toString() {
        return "EventDto [name=" + name + ", desciption=" + description
                + ", startDate=" + startDate + ", endDate= " + endDate + ", eventId= "
                + eventId + ", cancelation=" + cancelation + ", numberAssistance=" + numberAssistance
                + ", numberTotalResponses=" + numberTotalResponses +"]";
    }
}
