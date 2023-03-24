package es.udc.ws.app.restservice.dto;

import java.util.Objects;

public class RestEventDto {

    private Long eventId;
    private String name;
    private String description;
    private String startDate;
    private double duration;
    private boolean cancelation;
    private int numberAssistance;
    private int numberTotalResponses;

    public RestEventDto(){

    }

    public RestEventDto(Long eventId, String name, String description, String startDate, double duration, boolean cancelation, int numberAssistance, int numberTotalResponses){
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.duration = duration;
        this.cancelation = cancelation;
        this.numberAssistance = numberAssistance;
        this.numberTotalResponses = numberTotalResponses;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public boolean isCancelation() {
        return cancelation;
    }

    public void setCancelation(boolean cancelation) {
        this.cancelation = cancelation;
    }

    public int getNumberAssistance() {
        return numberAssistance;
    }

    public void setNumberAssistance(int numberAssistance) {
        this.numberAssistance = numberAssistance;
    }

    public int getNumberTotalResponses() {
        return numberTotalResponses;
    }

    public void setNumberTotalResponses(int numberTotalResponses) {
        this.numberTotalResponses = numberTotalResponses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestEventDto that = (RestEventDto) o;
        return Double.compare(that.duration, duration) == 0 && cancelation == that.cancelation && numberAssistance == that.numberAssistance && numberTotalResponses == that.numberTotalResponses && Objects.equals(eventId, that.eventId) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, name, description, startDate, duration, cancelation, numberAssistance, numberTotalResponses);
    }

    @Override
    public String toString(){
        return "EventDto [eventId=" + eventId + ", description=" + description
                + ", startDate=" + startDate + ", duration=" + duration
                + ", cancelation=" + cancelation + ", numberAssistance=" + numberAssistance
                + ", numberTotalResponses" + numberTotalResponses + "]";
    }
}
