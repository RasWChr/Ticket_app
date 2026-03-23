package com.example.tickets_app.BLL;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.Interface.IEventManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.Interface.IEventDAO;
import com.example.tickets_app.GUI.util.SessionManager;

import java.util.List;

public class EventManager implements IEventManager {

    private final IEventDAO eventDAO;

    public EventManager(IEventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @Override
    public List<Event> getAllEvents() throws ExceptionHandler {
        try {
            return eventDAO.getAllEvents();
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not retrieve events: " + e.getMessage(), e);
        }

    }

    @Override
    public List<Event> getEventsForCurrentUser() throws ExceptionHandler {
        try {
            User currentUser = SessionManager.getLoggedInUser();
            if (currentUser.getRole().equals("Admin")) {
                return eventDAO.getAllEvents();
            } else {
                return eventDAO.getEventsByCoordinator(currentUser.getId());
            }
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not retrieve events for user: " + e.getMessage(), e);
        }
    }

    @Override
    public void assignCoordinator(int eventId, int userId) throws ExceptionHandler {
        try {
            eventDAO.assignCoordinator(eventId, userId);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not assign coordinator: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeCoordinator(int eventId, int userId) throws ExceptionHandler {
        try {
            eventDAO.removeCoordinator(eventId, userId);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not remove coordinator: " + e.getMessage(), e);
        }

    }

    @Override
    public List<User> getCoordinatorsForEvent(int eventId) throws ExceptionHandler {
        try {
            return eventDAO.getCoordinatorForEvent(eventId);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not retrieve coordinators for event: " + e.getMessage(), e);
        }
    }
}
