package com.example.tickets_app.BE;

public class Event {
    private int id;
    private String name;
    private String startDateTime;
    private String endDateTime;
    private String location;
    private String locationGuidance;
    private String notes;

    public Event(int id, String name, String startDateTime, String endDateTime, String location, String locationGuidance, String notes) {
        this.id = id;
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
        this.locationGuidance = locationGuidance;
        this.notes = notes;
    }

    public Event(String name, String startDateTime, String endDateTime, String location, String locationGuidance, String notes) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
        this.locationGuidance = locationGuidance;
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return this.id == event.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }


    public String getLocationGuidance() {
        return locationGuidance;
    }

    public void setLocationGuidance(String locationGuidance) {
        this.locationGuidance = locationGuidance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return name;
    }
}
