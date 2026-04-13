package com.example.tickets_app.DAL.DAO;

import com.example.tickets_app.BE.CustomerTicket;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DB.DBConnector;
import com.example.tickets_app.DAL.Interface.ICustomerTicketDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerTicketDAO implements ICustomerTicketDAO {

    @Override
    public int issueTicket(CustomerTicket ct) throws ExceptionHandler {
        String sql = """
                INSERT INTO CustomerTickets
                    (TicketID, EventID, FirstName, LastName, Email, Phone, IsGlobal, TicketUUID, IsUsed)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)
                """;
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ct.getTicketId());
            if (ct.isGlobal() || ct.getEventId() == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, ct.getEventId());
            ps.setString(3, ct.getFirstName());
            ps.setString(4, ct.getLastName());
            ps.setString(5, ct.getEmail());
            ps.setString(6, ct.getPhone());
            ps.setBoolean(7, ct.isGlobal());
            ps.setString(8, ct.getTicketUUID());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            return keys.next() ? keys.getInt(1) : -1;
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("issueTicket", e);
            return -1;
        }
    }

    @Override
    public List<CustomerTicket> getAllIssuedTickets() throws ExceptionHandler {
        return query("""
                SELECT ct.ID, ct.TicketID, ct.EventID, ct.FirstName, ct.LastName, ct.Email,
                       ct.Phone, ct.IssuedAt, ct.IsGlobal, ct.TicketUUID, ct.IsUsed,
                       t.Tickettype, t.Price, t.Discount, e.Name AS EventName
                FROM CustomerTickets ct
                JOIN  Tickets t    ON ct.TicketID = t.ID
                LEFT JOIN Events e ON ct.EventID  = e.Id
                ORDER BY ct.IssuedAt DESC
                """);
    }

    @Override
    public List<CustomerTicket> getIssuedTicketsForEvent(int eventId) throws ExceptionHandler {
        String sql = """
                SELECT ct.ID, ct.TicketID, ct.EventID, ct.FirstName, ct.LastName, ct.Email,
                       ct.Phone, ct.IssuedAt, ct.IsGlobal, ct.TicketUUID, ct.IsUsed,
                       t.Tickettype, t.Price, t.Discount, e.Name AS EventName
                FROM CustomerTickets ct
                JOIN  Tickets t    ON ct.TicketID = t.ID
                LEFT JOIN Events e ON ct.EventID  = e.Id
                WHERE ct.EventID = ? OR ct.IsGlobal = 1
                ORDER BY ct.IssuedAt DESC
                """;
        List<CustomerTicket> result = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            result = mapResults(ps.executeQuery());
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("getIssuedTicketsForEvent", e);
        }
        return result;
    }

    @Override
    public List<CustomerTicket> getIssuedTicketsByTicketId(int ticketId) throws ExceptionHandler {
        String sql = """
                SELECT ct.ID, ct.TicketID, ct.EventID, ct.FirstName, ct.LastName, ct.Email,
                       ct.Phone, ct.IssuedAt, ct.IsGlobal, ct.TicketUUID, ct.IsUsed,
                       t.Tickettype, t.Price, t.Discount, e.Name AS EventName
                FROM CustomerTickets ct
                JOIN  Tickets t    ON ct.TicketID = t.ID
                LEFT JOIN Events e ON ct.EventID  = e.Id
                WHERE ct.TicketID = ?
                ORDER BY ct.IssuedAt DESC
                """;
        List<CustomerTicket> result = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            result = mapResults(ps.executeQuery());
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("getIssuedTicketsByTicketId", e);
        }
        return result;
    }

    @Override
    public List<CustomerTicket> getIssuedTicketsByEmail(String email) throws ExceptionHandler {
        String sql = """
                SELECT ct.ID, ct.TicketID, ct.EventID, ct.FirstName, ct.LastName, ct.Email,
                       ct.Phone, ct.IssuedAt, ct.IsGlobal, ct.TicketUUID, ct.IsUsed,
                       t.Tickettype, t.Price, t.Discount, e.Name AS EventName
                FROM CustomerTickets ct
                JOIN  Tickets t    ON ct.TicketID = t.ID
                LEFT JOIN Events e ON ct.EventID  = e.Id
                WHERE ct.Email = ?
                ORDER BY ct.IssuedAt DESC
                """;
        List<CustomerTicket> result = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            result = mapResults(ps.executeQuery());
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("getIssuedTicketsByEmail", e);
        }
        return result;
    }

    @Override
    public CustomerTicket getByUUID(String uuid) throws ExceptionHandler {
        String sql = """
                SELECT ct.ID, ct.TicketID, ct.EventID, ct.FirstName, ct.LastName, ct.Email,
                       ct.Phone, ct.IssuedAt, ct.IsGlobal, ct.TicketUUID, ct.IsUsed,
                       t.Tickettype, t.Price, t.Discount, e.Name AS EventName
                FROM CustomerTickets ct
                JOIN  Tickets t    ON ct.TicketID = t.ID
                LEFT JOIN Events e ON ct.EventID  = e.Id
                WHERE ct.TicketUUID = ?
                """;
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid);
            List<CustomerTicket> r = mapResults(ps.executeQuery());
            return r.isEmpty() ? null : r.get(0);
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("getByUUID", e);
            return null;
        }
    }

    @Override
    public boolean markAsUsed(String uuid) throws ExceptionHandler {
        // Atomic: only updates if IsUsed is still 0
        String sql = "UPDATE CustomerTickets SET IsUsed = 1 WHERE TicketUUID = ? AND IsUsed = 0";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("markAsUsed", e);
            return false;
        }
    }

    @Override
    public void deleteIssuedTicket(int customerTicketId) throws ExceptionHandler {
        String sql = "DELETE FROM CustomerTickets WHERE ID = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerTicketId);
            ps.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("deleteIssuedTicket", e);
        }
    }

    @Override
    public String generateUniqueUUID() throws ExceptionHandler {
        String uuid;
        do { uuid = UUID.randomUUID().toString(); }
        while (uuidExistsInDB(uuid));
        return uuid;
    }

    private boolean uuidExistsInDB(String uuid) throws ExceptionHandler {
        String sql = "SELECT COUNT(*) FROM CustomerTickets WHERE TicketUUID = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("uuidExistsInDB", e);
            return false;
        }
    }

    private List<CustomerTicket> query(String sql) throws ExceptionHandler {
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return mapResults(ps.executeQuery());
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("query", e);
            return new ArrayList<>();
        }
    }

    private List<CustomerTicket> mapResults(ResultSet rs) throws SQLException {
        List<CustomerTicket> list = new ArrayList<>();
        while (rs.next()) {
            int rawEventId = rs.getInt("EventID");
            Integer eventId = rs.wasNull() ? null : rawEventId;
            Timestamp ts = rs.getTimestamp("IssuedAt");
            LocalDateTime issuedAt = ts != null ? ts.toLocalDateTime() : null;

            CustomerTicket ct = new CustomerTicket(
                    rs.getInt("ID"), rs.getInt("TicketID"), eventId,
                    rs.getString("FirstName"), rs.getString("LastName"),
                    rs.getString("Email"), rs.getString("Phone"),
                    issuedAt, rs.getBoolean("IsGlobal")
            );
            ct.setTicketType(rs.getString("Tickettype"));
            ct.setPrice(rs.getDouble("Price"));
            ct.setDiscount(rs.getDouble("Discount"));
            ct.setEventName(rs.getString("EventName"));
            ct.setTicketUUID(rs.getString("TicketUUID"));
            ct.setUsed(rs.getBoolean("IsUsed"));
            list.add(ct);
        }
        return list;
    }
}