package com.example.tickets_app.BLL.Interface;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import java.util.List;

public interface IEventManager {
    List<Event> getAllEvents() throws ExceptionHandler;
    List<Event> getEventsForCurrentUser() throws ExceptionHandler;
    void createEvent(String name, String startDateTime, String endDateTime, String location, String locationGuidance, String notes) throws ExceptionHandler;
    void deleteEvent(int eventId) throws ExceptionHandler;
    void assignCoordinator(int eventId, int userId) throws ExceptionHandler;
    void removeCoordinator(int eventId, int userId) throws ExceptionHandler;
    List<User> getCoordinatorsForEvent(int eventId) throws ExceptionHandler;

    void editEvent(int id, String name, String startDateTime, String endDateTime, String location, String locationGuidance, String notes);
}