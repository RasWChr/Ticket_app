package com.example.tickets_app.DAL.DAO;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.Interface.IEventDAO;

import java.util.List;

public class EventDAO implements IEventDAO {
    @Override
    public List<Event> getAllEvents() throws ExceptionHandler {
        return List.of();
    }

    @Override
    public List<Event> getEventsByCoordinator(int userId) throws ExceptionHandler {
        return List.of();
    }

    @Override
    public void assignCoordinator(int eventId, int userId) throws ExceptionHandler {

    }

    @Override
    public void removeCoordinator(int eventId, int userId) throws ExceptionHandler {

    }

    @Override
    public List<User> getCoordinatorForEvent(int eventId) throws ExceptionHandler {
        return List.of();
    }

    @Override
    public boolean isCoordinatorAssigned(int eventId, int userId) throws ExceptionHandler {
        return false;
    }
}
