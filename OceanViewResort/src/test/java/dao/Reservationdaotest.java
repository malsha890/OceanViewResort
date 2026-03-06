package dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;


@ExtendWith(MockitoExtension.class)
public class Reservationdaotest {

    // ── Mocked JDBC objects ───────────────────────────────────────────
    @Mock private Connection        mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private Statement         mockStatement;      // used only by getAllReservations()
    @Mock private ResultSet         mockResultSet;

   
    @Test
    @DisplayName("TC-R01: addReservation() with a valid Reservation object executes INSERT without exception")
    void testAddReservation_ValidReservation_ExecutesInsert() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            // Arrange
            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            // Build Reservation — fields match your INSERT columns:
            // customer_name, customer_phone, customer_email,
            // room_id, check_in, check_out, status, room_type, customer_address
            Reservation r = new Reservation();
            r.setCustomerName("Nimal Perera");
            r.setCustomerPhone("0771234567");
            r.setCustomerEmail("nimal@email.com");
            r.setRoomId(3);
            r.setCheckIn(LocalDate.of(2025, 8, 10));
            r.setCheckOut(LocalDate.of(2025, 8, 14));
            r.setStatus("CONFIRMED");
            r.setRoomType("DELUXE");
            r.setCustomerAddress("123 Galle Road, Colombo");

            ReservationDAO dao = new ReservationDAO();

            // Act & Assert
            assertDoesNotThrow(() -> dao.addReservation(r),
                "TC-R01 FAIL: addReservation() should not throw for a valid Reservation");

            // Verify all 9 PreparedStatement parameters were set correctly
            verify(mockPreparedStatement).setString(1, "Nimal Perera");
            verify(mockPreparedStatement).setString(2, "0771234567");
            verify(mockPreparedStatement).setString(3, "nimal@email.com");
            verify(mockPreparedStatement).setInt(4, 3);
            verify(mockPreparedStatement).setDate(5, Date.valueOf(LocalDate.of(2025, 8, 10)));
            verify(mockPreparedStatement).setDate(6, Date.valueOf(LocalDate.of(2025, 8, 14)));
            verify(mockPreparedStatement).setString(7, "CONFIRMED");
            verify(mockPreparedStatement).setString(8, "DELUXE");
            verify(mockPreparedStatement).setString(9, "123 Galle Road, Colombo");
            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }

    
    @Test
    @DisplayName("TC-R02: addReservation() uses default status CONFIRMED when status field is null")
    void testAddReservation_NullStatus_DefaultsToConfirmed() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Reservation r = new Reservation();
            r.setCustomerName("Kamal Silva");
            r.setCustomerPhone("0712345678");
            r.setCustomerEmail("kamal@email.com");
            r.setRoomId(5);
            r.setCheckIn(LocalDate.of(2025, 9, 1));
            r.setCheckOut(LocalDate.of(2025, 9, 3));
            r.setStatus(null);          // ← null status
            r.setRoomType("STANDARD");
            r.setCustomerAddress("Kandy Road");

            ReservationDAO dao = new ReservationDAO();
            dao.addReservation(r);

            // Verify parameter 7 was set to "CONFIRMED" (the null-safe default)
            verify(mockPreparedStatement).setString(7, "CONFIRMED");
        }
    }

   
    @Test
    @DisplayName("TC-R03: cancelReservation() with valid reservationId sets status to CANCELLED")
    void testCancelReservation_ValidId_ExecutesUpdate() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            ReservationDAO dao = new ReservationDAO();

            // reservationId=7, roomId=3
            assertDoesNotThrow(() -> dao.cancelReservation(7, 3),
                "TC-R03 FAIL: cancelReservation() should not throw for a valid reservation ID");

            // Verify reservation_id=7 was passed as parameter 1
            verify(mockPreparedStatement).setInt(1, 7);
            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }

    
    @Test
    @DisplayName("TC-R04: getAllReservations() returns a List of size 2 when DB has 2 rows")
    void testGetAllReservations_TwoRows_ReturnsSizeTwo() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);

            // getAllReservations() uses createStatement(), not prepareStatement()
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

            // Simulate 2 rows then ResultSet ends
            when(mockResultSet.next()).thenReturn(true, true, false);

            when(mockResultSet.getInt("reservation_id"))
                .thenReturn(1, 2);
            when(mockResultSet.getString("customer_name"))
                .thenReturn("Nimal Perera", "Alice Silva");
            when(mockResultSet.getString("customer_phone"))
                .thenReturn("0771234567", "0712345678");
            when(mockResultSet.getString("customer_email"))
                .thenReturn("nimal@email.com", "alice@email.com");
            when(mockResultSet.getInt("room_id"))
                .thenReturn(3, 5);
            when(mockResultSet.getString("status"))
                .thenReturn("CONFIRMED", "CONFIRMED");
            when(mockResultSet.getString("room_type"))
                .thenReturn("DELUXE", "STANDARD");
            when(mockResultSet.getString("customer_address"))
                .thenReturn("Galle Road", "Kandy Road");
            when(mockResultSet.getDate("check_in"))
                .thenReturn(Date.valueOf(LocalDate.of(2025, 8, 10)),
                            Date.valueOf(LocalDate.of(2025, 9, 1)));
            when(mockResultSet.getDate("check_out"))
                .thenReturn(Date.valueOf(LocalDate.of(2025, 8, 14)),
                            Date.valueOf(LocalDate.of(2025, 9, 3)));

            // created_at returns null — skips the if block cleanly
            when(mockResultSet.getTimestamp("created_at")).thenReturn(null);

            ReservationDAO dao = new ReservationDAO();
            List<Reservation> result = dao.getAllReservations();

            assertNotNull(result,
                "TC-R04 FAIL: getAllReservations() should not return null");
            assertEquals(2, result.size(),
                "TC-R04 FAIL: list size should be 2");
            assertEquals("Nimal Perera", result.get(0).getCustomerName(),
                "TC-R04 FAIL: first customer name should be 'Nimal Perera'");
            assertEquals("STANDARD", result.get(1).getRoomType(),
                "TC-R04 FAIL: second room type should be 'STANDARD'");
            assertEquals(LocalDate.of(2025, 8, 10), result.get(0).getCheckIn(),
                "TC-R04 FAIL: first check_in should be 2025-08-10");
        }
    }

 
    @Test
    @DisplayName("TC-R05: getReservationById() with valid existing ID returns correct Reservation")
    void testGetReservationById_ValidId_ReturnsReservation() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("reservation_id")) .thenReturn(7);
            when(mockResultSet.getString("customer_name")).thenReturn("Nimal Perera");
            when(mockResultSet.getInt("room_id"))        .thenReturn(3);
            when(mockResultSet.getString("room_type"))   .thenReturn("DELUXE");
            when(mockResultSet.getDate("check_in"))
                .thenReturn(Date.valueOf(LocalDate.of(2025, 8, 10)));
            when(mockResultSet.getDate("check_out"))
                .thenReturn(Date.valueOf(LocalDate.of(2025, 8, 14)));

            ReservationDAO dao = new ReservationDAO();
            Reservation result = dao.getReservationById(7);

            assertNotNull(result,
                "TC-R05 FAIL: getReservationById() should return a Reservation when ID=7 exists");
            assertEquals(7, result.getReservationId(),
                "TC-R05 FAIL: reservation_id should be 7");
            assertEquals("Nimal Perera", result.getCustomerName(),
                "TC-R05 FAIL: customer_name should be 'Nimal Perera'");
            assertEquals(LocalDate.of(2025, 8, 10), result.getCheckIn(),
                "TC-R05 FAIL: check_in should be 2025-08-10");
            assertEquals(LocalDate.of(2025, 8, 14), result.getCheckOut(),
                "TC-R05 FAIL: check_out should be 2025-08-14");

            // Verify ID=7 was passed to PreparedStatement
            verify(mockPreparedStatement).setInt(1, 7);
        }
    }

  
    @Test
    @DisplayName("TC-R06: getReservationById() returns null when ID does not exist in DB")
    void testGetReservationById_NotFound_ReturnsNull() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

            // ResultSet empty — no matching row
            when(mockResultSet.next()).thenReturn(false);

            ReservationDAO dao = new ReservationDAO();
            Reservation result = dao.getReservationById(9999);

            assertNull(result,
                "TC-R06 FAIL: getReservationById() should return null for non-existent ID=9999");
        }
    }

    
    @Test
    @DisplayName("TC-R07: isRoomAvailable() returns true when COUNT(*)=0 (no overlapping booking)")
    void testIsRoomAvailable_NoOverlap_ReturnsTrue() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(0);  // COUNT(*) = 0 → available

            ReservationDAO dao = new ReservationDAO();
            boolean result = dao.isRoomAvailable(
                3,
                LocalDate.of(2025, 8, 10),
                LocalDate.of(2025, 8, 14)
            );

            assertTrue(result,
                "TC-R07 FAIL: isRoomAvailable() should return true when COUNT(*)=0");

            // Verify parameters match your SQL:
            // param1=roomId, param2=checkOut, param3=checkIn
            verify(mockPreparedStatement).setInt(1, 3);
            verify(mockPreparedStatement).setDate(2, Date.valueOf(LocalDate.of(2025, 8, 14)));
            verify(mockPreparedStatement).setDate(3, Date.valueOf(LocalDate.of(2025, 8, 10)));
        }
    }

    
    @Test
    @DisplayName("TC-R08: isRoomAvailable() returns false when COUNT(*)=1 (overlapping booking exists)")
    void testIsRoomAvailable_OverlapExists_ReturnsFalse() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(1);  // COUNT(*) = 1 → conflict

            ReservationDAO dao = new ReservationDAO();
            boolean result = dao.isRoomAvailable(
                3,
                LocalDate.of(2025, 8, 12),  // overlaps existing 10–14 Aug booking
                LocalDate.of(2025, 8, 15)
            );

            assertFalse(result,
                "TC-R08 FAIL: isRoomAvailable() should return false when COUNT(*)=1");
        }
    }
}
