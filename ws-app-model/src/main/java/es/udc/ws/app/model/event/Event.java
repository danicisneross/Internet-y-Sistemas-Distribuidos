package es.udc.ws.app.model.event;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Event {
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private LocalDateTime startDate;
    private double duration;
    private Long eventId;
    private boolean cancelation;
    private int numberAssitance;
    private int numberNoAssitance;

    public Event(String name, String description, LocalDateTime creationDate, LocalDateTime startDate, double duration, Long eventId, boolean cancelation, int numberAssitance, int numberNoAssitance){
        this.name = name;
        this.description = description;
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
        this.startDate = (startDate != null) ? startDate.withNano(0) : null;
        this.duration = duration;
        this.eventId = eventId;
        this.cancelation = cancelation;
        this.numberAssitance = numberAssitance;
        this.numberNoAssitance = numberNoAssitance;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = (startDate != null) ? startDate.withNano(0) : null;
    }

    public boolean isCancelation() {
        return cancelation;
    }

    public void setCancelation(boolean cancelation) {
        this.cancelation = cancelation;
    }

    public int getNumberAssitance() {
        return numberAssitance;
    }

    public void setNumberAssitance(int numberAssitance) {
        this.numberAssitance = numberAssitance;
    }

    public int getNumberNoAssitance() {
        return numberNoAssitance;
    }

    public void setNumberNoAssitance(int numberNoAssitance) {
        this.numberNoAssitance = numberNoAssitance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Event event = (Event) o;
        return duration == event.duration && cancelation == event.cancelation && numberAssitance == event.numberAssitance && numberNoAssitance == event.numberNoAssitance && eventId.equals(event.eventId) && name.equals(event.name) && description.equals(event.description) && startDate.equals(event.startDate) && creationDate.equals(event.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, name, description, creationDate, startDate, duration, cancelation, numberAssitance, numberNoAssitance);
    }
}
