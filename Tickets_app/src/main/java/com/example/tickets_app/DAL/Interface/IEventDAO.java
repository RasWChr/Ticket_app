package com.example.tickets_app.DAL.Interface;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.util.ExceptionHandler;

import java.util.List;

public interface IEventDAO {
    List<Event> getAllEvents() throws ExceptionHandler;
    List<Event> getEventsByCoordinator (int userId) throws ExceptionHandler;
    void createEvent(Event event) throws ExceptionHandler;
    void deleteEvent(int eventId) throws ExceptionHandler;
    void editEvent(int eventId, String name, String startDateTime, String endDateTime, String location, String locationGuidance, String notes) throws ExceptionHandler;
    void assignCoordinator(int eventId, int userId) throws ExceptionHandler;
    void removeCoordinator(int eventId, int userId) throws ExceptionHandler;
    List<User> getCoordinatorForEvent(int eventId) throws ExceptionHandler;
    boolean isCoordinatorAssigned(int eventId, int userId) throws ExceptionHandler;

}
