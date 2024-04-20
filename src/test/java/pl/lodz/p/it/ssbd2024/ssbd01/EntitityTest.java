package pl.lodz.p.it.ssbd2024.ssbd01;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EntitityTest {

    private static Role role;
    private static Role role2;
    private static Role role3;
    private static Role role4;
    private static Role role5;

    private static Account account;
    private static Account account2;
    private static Account account3;
    private static Account account4;
    private static Account account5;

    private static Room room;
    private static Room room2;
    private static Room room3;

    private static Location location;
    private static Location location2;
    private static Location location3;

    private static Speaker speaker;
    private static Speaker speaker2;
    private static Speaker speaker3;

    private static Session session;
    private static Session session2;
    private static Session session3;

    private static Event event;
    private static Event event2;
    private static Event event3;

    private static Ticket ticket;
    private static Ticket ticket2;
    private static Ticket ticket3;


    @BeforeAll
    public static void setup() {
        role = new Role("ADMIN");
        role2 = new Role("ADMIN");
        role3 = new Role("MANAGER");
        role4 = new Role();
        role5 = new Role(null);

        account = new Account("user", "password", "email@email.com", 0, "firstName", "lastName");
        account.setActive(true);
        account.setEmail("email@email.com");
        account.setGender(0);
        account.setFirstName("Jan");
        account.setLastName("Kowalski");
        account.setPassword("$2a$10$zsdqklLhmxXcZA//a3DdLOy0AU9kQhXg1znqsc/jSmjcokOYUuFr6");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        account.setRoles(roles);
        account.setLastFailedLogin(LocalDateTime.of(2021, 1, 1, 1, 1));
        account.setLastSuccessfulLogin(LocalDateTime.of(2021, 1, 1, 1, 1));
        account.setVerified(true);

        account2 = new Account("user", "password", "email@email.com", 0, "firstName", "lastName");
        account3 = new Account("user2", "password", "email@email.com", 0, "firstName", "lastName");
        account4 = new Account();
        account5 = new Account(null, null, null, 0, null, null);

        room = new Room();
        room.setName("Aula");
        room.setMaxCapacity(200);

        room2 = new Room();
        room2.setName("Aula");
        room2.setMaxCapacity(200);

        room3 = new Room();

        location = new Location();
        location.setName("LODEX");
        location.setRooms(List.of(room));

        location2 = new Location();
        location2.setName("LODEX");
        location2.setRooms(List.of(room));

        location3 = new Location();

        speaker = new Speaker();
        speaker.setFirstName("Jan");
        speaker.setLastName("Kowalski");

        speaker2 = new Speaker();
        speaker2.setFirstName("Jan");
        speaker2.setLastName("Kowalski");

        speaker3 = new Speaker();

        session = new Session();
        session.setIsActive(true);
        session.setDescription("description");
        session.setName("name");
        session.setRoom(room);
        session.setSpeaker(speaker);
        session.setStartTime(LocalDateTime.of(2021, 1, 1, 1, 1));
        session.setEndTime(LocalDateTime.of(2021, 1, 1, 1, 1));
        session.setMaxSeats(20);

        session2 = new Session();
        session2.setIsActive(true);
        session2.setDescription("description");
        session2.setName("name");
        session2.setRoom(room);
        session2.setSpeaker(speaker);
        session2.setStartTime(LocalDateTime.of(2021, 1, 1, 1, 1));
        session2.setEndTime(LocalDateTime.of(2021, 1, 1, 1, 1));
        session2.setMaxSeats(20);

        session3 = new Session();

        event = new Event();
        event.setDescription("description");
        event.setName("name");
        event.setIsNotCanceled(true);
        event.setSessions(List.of(session));

        event2 = new Event();
        event2.setDescription("description");
        event2.setName("name");
        event2.setIsNotCanceled(true);
        event2.setSessions(List.of(session));

        event3 = new Event();

        ticket = new Ticket();
        ticket.setAccount(account);
        ticket.setSession(session);
        ticket.setReservationTime(LocalDateTime.of(2021, 1, 1, 1, 1));
        ticket.setIsConfirmed(true);

        ticket2 = new Ticket();
        ticket2.setAccount(account);
        ticket2.setSession(session);
        ticket2.setReservationTime(LocalDateTime.of(2021, 1, 1, 1, 1));
        ticket2.setIsConfirmed(true);

        ticket3 = new Ticket();
    }


    @Test
    public void settersTest() {
        Assertions.assertEquals("ADMIN", role.getName());

        Assertions.assertEquals(true, account.getActive());
        Assertions.assertEquals("email@email.com", account.getEmail());
        Assertions.assertEquals(0, account.getGender());
        Assertions.assertEquals("Jan", account.getFirstName());
        Assertions.assertEquals("Kowalski", account.getLastName());
        Assertions.assertEquals("$2a$10$zsdqklLhmxXcZA//a3DdLOy0AU9kQhXg1znqsc/jSmjcokOYUuFr6", account.getPassword());
        Assertions.assertEquals(1, account.getRoles().size());
        Assertions.assertEquals(LocalDateTime.of(2021, 1, 1, 1, 1), account.getLastFailedLogin());
        Assertions.assertEquals(LocalDateTime.of(2021, 1, 1, 1, 1), account.getLastSuccessfulLogin());
        Assertions.assertEquals(true, account.getVerified());
        Assertions.assertEquals(1, account.getAuthorities().size());

        account.addRole(role3);
        Assertions.assertEquals(2, account.getRoles().size());
        account.removeRole(role3);
        Assertions.assertEquals(1, account.getRoles().size());

        Assertions.assertEquals("Aula", room.getName());
        Assertions.assertEquals(200, room.getMaxCapacity());

        Assertions.assertEquals("LODEX", location.getName());
        Assertions.assertEquals(1, location.getRooms().size());

        Assertions.assertEquals("Jan", speaker.getFirstName());
        Assertions.assertEquals("Kowalski", speaker.getLastName());

        Assertions.assertEquals(true, session.getIsActive());
        Assertions.assertEquals("description", session.getDescription());
        Assertions.assertEquals("name", session.getName());
        Assertions.assertEquals(room, session.getRoom());
        Assertions.assertEquals(speaker, session.getSpeaker());
        Assertions.assertEquals(20, session.getMaxSeats());
        Assertions.assertEquals(LocalDateTime.of(2021, 1, 1, 1, 1), session.getStartTime());
        Assertions.assertEquals(LocalDateTime.of(2021, 1, 1, 1, 1), session.getEndTime());

        Assertions.assertEquals("description", event.getDescription());
        Assertions.assertEquals("name", event.getName());
        Assertions.assertEquals(true, event.getIsNotCanceled());
        Assertions.assertEquals(1, event.getSessions().size());

        Assertions.assertEquals(account, ticket.getAccount());
        Assertions.assertEquals(session, ticket.getSession());
        Assertions.assertEquals(true, ticket.getIsConfirmed());
        Assertions.assertEquals(LocalDateTime.of(2021, 1, 1, 1, 1), ticket.getReservationTime());
    }

    @Test
    public void rolesEqualsHashCodeToStringTest() {
        Assertions.assertEquals(role, role2);
        Assertions.assertNotEquals(role, role3);
        Assertions.assertEquals(role, role);
        Assertions.assertNotEquals(role, null);
        Assertions.assertNotEquals(role, role4);
        Assertions.assertNotEquals(role5, role);

        Assertions.assertEquals(role.hashCode(), role2.hashCode());
        Assertions.assertNotEquals(role5.hashCode(), role.hashCode());

        Assertions.assertFalse(role.toString().isEmpty());
    }

    @Test
    public void accountEqualsHashCodeToStringTest() {
        Assertions.assertEquals(account, account2);
        Assertions.assertNotEquals(account, account3);
        Assertions.assertEquals(account, account);
        Assertions.assertNotEquals(account, null);
        Assertions.assertNotEquals(account, account4);
        Assertions.assertNotEquals(account5, account);

        Assertions.assertEquals(account.hashCode(), account2.hashCode());
        Assertions.assertNotEquals(account5.hashCode(), account.hashCode());

        Assertions.assertFalse(account.toString().isEmpty());
    }

    @Test
    public void locationEqualsHashCodeToStringTest() {
        Assertions.assertEquals(location, location2);
        Assertions.assertNotEquals(location, location3);

        Assertions.assertEquals(location.hashCode(), location2.hashCode());
        Assertions.assertNotEquals(location3.hashCode(), location.hashCode());

        Assertions.assertFalse(location.toString().isEmpty());
    }

    @Test
    public void speakerEqualsHashCodeToStringTest() {
        Assertions.assertEquals(speaker, speaker2);
        Assertions.assertNotEquals(speaker, speaker3);

        Assertions.assertEquals(speaker.hashCode(), speaker2.hashCode());
        Assertions.assertNotEquals(speaker3.hashCode(), speaker.hashCode());

        Assertions.assertFalse(speaker.toString().isEmpty());
    }

    @Test
    public void sessionEqualsHashCodeToStringTest() {
        Assertions.assertEquals(session, session2);
        Assertions.assertNotEquals(session, session3);

        Assertions.assertEquals(session.hashCode(), session2.hashCode());
        Assertions.assertNotEquals(session3.hashCode(), session.hashCode());

        Assertions.assertFalse(session.toString().isEmpty());
    }

    @Test
    public void eventEqualsHashCodeToStringTest() {
        Assertions.assertEquals(event, event2);
        Assertions.assertNotEquals(event, event3);

        Assertions.assertEquals(event.hashCode(), event2.hashCode());
        Assertions.assertNotEquals(event3.hashCode(), event.hashCode());

        Assertions.assertFalse(event.toString().isEmpty());
    }

    @Test
    public void ticketEqualsHashCodeToStringTest() {
        Assertions.assertEquals(ticket, ticket2);
        Assertions.assertNotEquals(ticket, ticket3);

        Assertions.assertEquals(ticket.hashCode(), ticket2.hashCode());
        Assertions.assertNotEquals(ticket3.hashCode(), ticket.hashCode());

        Assertions.assertFalse(ticket.toString().isEmpty());
    }

    @Test
    public void roomEqualsHashCodeToStringTest() {
        Assertions.assertEquals(room, room2);
        Assertions.assertNotEquals(room, room3);

        Assertions.assertEquals(room.hashCode(), room2.hashCode());
        Assertions.assertNotEquals(room3.hashCode(), room.hashCode());

        Assertions.assertFalse(room.toString().isEmpty());
    }

}
