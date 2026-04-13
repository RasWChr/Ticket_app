package com.example.tickets_app.BLL;

import com.example.tickets_app.BE.CustomerTicket;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.Interface.ICustomerTicketDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CustomerTicketManager.
 * Made by none other than Claude
 * Uses an in-memory stub DAO — no database required.
 */
class CustomerTicketManagerTest {

    // ── In-memory stub DAO ────────────────────────────────────────────────────

    private static class StubDAO implements ICustomerTicketDAO {

        final List<CustomerTicket> store = new ArrayList<>();
        private final AtomicInteger idSeq = new AtomicInteger(1);

        @Override
        public int issueTicket(CustomerTicket ct) throws ExceptionHandler {
            int id = idSeq.getAndIncrement();
            ct.setId(id);
            store.add(ct);
            return id;
        }

        @Override
        public List<CustomerTicket> getAllIssuedTickets()  { return new ArrayList<>(store); }

        @Override
        public List<CustomerTicket> getIssuedTicketsForEvent(int eventId) {
            return store.stream()
                    .filter(c -> c.isGlobal() || (c.getEventId() != null && c.getEventId() == eventId))
                    .toList();
        }

        @Override
        public List<CustomerTicket> getIssuedTicketsByTicketId(int ticketId) {
            return store.stream().filter(c -> c.getTicketId() == ticketId).toList();
        }

        @Override
        public List<CustomerTicket> getIssuedTicketsByEmail(String email) {
            return store.stream().filter(c -> email.equals(c.getEmail())).toList();
        }

        @Override
        public CustomerTicket getByUUID(String uuid) {
            return store.stream()
                    .filter(c -> uuid.equals(c.getTicketUUID()))
                    .findFirst().orElse(null);
        }

        @Override
        public boolean markAsUsed(String uuid) {
            CustomerTicket ct = getByUUID(uuid);
            if (ct == null || ct.isUsed()) return false;
            ct.setUsed(true);
            return true;
        }

        @Override
        public void deleteIssuedTicket(int id) {
            store.removeIf(c -> c.getId() == id);
        }

        @Override
        public String generateUniqueUUID() {
            String uuid;
            boolean exists;

            do {
                uuid = UUID.randomUUID().toString();
                final String finalUuid = uuid;
                exists = store.stream().anyMatch(c -> Objects.equals(finalUuid, c.getTicketUUID()));
            } while (exists);

            return uuid;
        }
    }

    private CustomerTicketManager manager;
    private StubDAO stub;

    @BeforeEach
    void setUp() {
        stub    = new StubDAO();
        manager = new CustomerTicketManager(stub);
    }

    // ── Single ticket issue ───────────────────────────────────────────────────

    @Test
    void issueTicket_createsOneRecord() throws ExceptionHandler {
        int id = manager.issueTicket(1, 10, "Alice", "Smith",
                "alice@example.com", null, false);

        assertTrue(id > 0);
        assertEquals(1, stub.store.size());
    }

    @Test
    void issueTicket_assignsUniqueUUID() throws ExceptionHandler {
        manager.issueTicket(1, 10, "Alice", "Smith", "alice@example.com", null, false);
        manager.issueTicket(1, 10, "Alice", "Smith", "alice@example.com", null, false);

        String uuid1 = stub.store.get(0).getTicketUUID();
        String uuid2 = stub.store.get(1).getTicketUUID();

        assertNotNull(uuid1);
        assertNotNull(uuid2);
        // Each issued ticket gets its own UUID — even for the same customer/template
        assertNotEquals(uuid1, uuid2, "Every issued ticket must have a unique UUID");
    }

    @Test
    void issueTicket_sameEmailAllowed_multipleTickets() throws ExceptionHandler {
        // The same email can have N tickets — groups / bulk purchases
        for (int i = 0; i < 5; i++) {
            manager.issueTicket(1, 10, "Bob", "Jones", "bob@example.com", null, false);
        }

        List<CustomerTicket> byEmail = manager.getIssuedTicketsByEmail("bob@example.com");
        assertEquals(5, byEmail.size(), "Customer should have 5 independent tickets");
    }

    // ── Bulk issue ────────────────────────────────────────────────────────────

    @Test
    void issueMultipleTickets_correctCount() throws ExceptionHandler {
        List<Integer> ids = manager.issueMultipleTickets(
                1, 10, "Group", "Leader", "group@example.com", null, false, 3);

        assertEquals(3, ids.size());
        assertEquals(3, stub.store.size());
    }

    @Test
    void issueMultipleTickets_allUUIDsDistinct() throws ExceptionHandler {
        manager.issueMultipleTickets(
                1, 10, "Group", "Leader", "group@example.com", null, false, 10);

        long distinct = stub.store.stream()
                .map(CustomerTicket::getTicketUUID)
                .distinct()
                .count();
        assertEquals(10, distinct, "Every ticket in a bulk issue must have a unique UUID");
    }

    @Test
    void issueMultipleTickets_quantityZeroThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.issueMultipleTickets(
                        1, 10, "A", "B", "a@b.com", null, false, 0));
    }

    @Test
    void issueMultipleTickets_quantityOver100Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.issueMultipleTickets(
                        1, 10, "A", "B", "a@b.com", null, false, 101));
    }

    // ── One-time use validation ───────────────────────────────────────────────

    @Test
    void validateAndUse_validUnusedTicket_marksAsUsed() throws ExceptionHandler {
        manager.issueTicket(1, 10, "Carol", "White", "carol@example.com", null, false);
        String uuid = stub.store.get(0).getTicketUUID();

        CustomerTicket result = manager.validateAndUse(uuid);

        assertNotNull(result);
        assertTrue(result.isUsed());
        assertTrue(stub.store.get(0).isUsed());
    }

    @Test
    void validateAndUse_unknownUUID_returnsNull() throws ExceptionHandler {
        CustomerTicket result = manager.validateAndUse("00000000-0000-0000-0000-000000000000");
        assertNull(result);
    }

    @Test
    void validateAndUse_alreadyUsed_throwsIllegalState() throws ExceptionHandler {
        manager.issueTicket(1, 10, "Dave", "Black", "dave@example.com", null, false);
        String uuid = stub.store.get(0).getTicketUUID();

        manager.validateAndUse(uuid); // first use — OK

        assertThrows(IllegalStateException.class,
                () -> manager.validateAndUse(uuid),  // second use — must throw
                "Reusing an already-used ticket should throw IllegalStateException");
    }

    @Test
    void validateAndUse_emptyUUID_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> manager.validateAndUse(""));
    }

    // ── Input validation ──────────────────────────────────────────────────────

    @Test
    void issueTicket_missingFirstName_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.issueTicket(1, 10, "", "Smith", "a@b.com", null, false));
    }

    @Test
    void issueTicket_invalidEmail_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.issueTicket(1, 10, "A", "B", "not-an-email", null, false));
    }

    @Test
    void issueTicket_notGlobalNoEvent_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.issueTicket(1, null, "A", "B", "a@b.com", null, false));
    }

    @Test
    void issueTicket_globalWithNullEvent_succeeds() throws ExceptionHandler {
        int id = manager.issueTicket(1, null, "A", "B", "a@b.com", null, true);
        assertTrue(id > 0);
        assertNull(stub.store.get(0).getEventId(),
                "Global ticket should have null eventId");
    }
}