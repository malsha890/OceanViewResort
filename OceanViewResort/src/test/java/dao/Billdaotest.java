package dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;


@ExtendWith(MockitoExtension.class)
public class Billdaotest {

    // ── Mocked JDBC objects ───────────────────────────────────────────
    @Mock private Connection        mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet         mockResultSet;

    // ── Mocked internal DAOs ──────────────────────────────────────────
    @Mock private ReservationDAO    mockReservationDAO;
    @Mock private RoomDAO           mockRoomDAO;

    
    static class BillDAOTestable extends BillDAO {

        BillDAOTestable(ReservationDAO rDao, RoomDAO roomDao) {
            this.reservationDAO = rDao;
            this.roomDAO        = roomDao;
        }
    }

    
    @Test
    @DisplayName("TC-B01: createBill() inserts correct roomCharge, tax, serviceCharge, total for 4-night stay")
    void testCreateBill_FourNightStay_CorrectAmounts() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            // Arrange — DB
            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            // Arrange — 4-night reservation
            Reservation r = new Reservation();
            r.setReservationId(7);
            r.setRoomId(3);
            r.setCheckIn(LocalDate.of(2025, 8, 10));
            r.setCheckOut(LocalDate.of(2025, 8, 14));   // 4 nights
            when(mockReservationDAO.getReservationById(7)).thenReturn(r);
            when(mockRoomDAO.getRoomPriceById(3)).thenReturn(new BigDecimal("5000.00"));

            // Act
            BillDAOTestable dao = new BillDAOTestable(mockReservationDAO, mockRoomDAO);
            assertDoesNotThrow(() -> dao.createBill(7),
                "TC-B01 FAIL: createBill() should not throw for a valid 4-night reservation");

            // Assert — verify each BigDecimal inserted into DB
            verify(mockPreparedStatement).setInt(1, 7);                                    // reservation_id
            verify(mockPreparedStatement).setBigDecimal(2, new BigDecimal("20000.00"));    // roomCharge
            verify(mockPreparedStatement).setBigDecimal(3, new BigDecimal("1000.00"));     // tax 5%
            verify(mockPreparedStatement).setBigDecimal(4, new BigDecimal("2000.00"));     // serviceCharge 10%
            verify(mockPreparedStatement).setBigDecimal(5, new BigDecimal("23000.00"));    // total
            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }

    
    @Test
    @DisplayName("TC-B02: createBill() calculates correct amounts for a 1-night stay")
    void testCreateBill_OneNightStay_CorrectAmounts() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Reservation r = new Reservation();
            r.setReservationId(8);
            r.setRoomId(5);
            r.setCheckIn(LocalDate.of(2025, 9, 1));
            r.setCheckOut(LocalDate.of(2025, 9, 2));   // 1 night
            when(mockReservationDAO.getReservationById(8)).thenReturn(r);
            when(mockRoomDAO.getRoomPriceById(5)).thenReturn(new BigDecimal("7500.00"));

            BillDAOTestable dao = new BillDAOTestable(mockReservationDAO, mockRoomDAO);
            dao.createBill(8);

            verify(mockPreparedStatement).setBigDecimal(2, new BigDecimal("7500.00"));   // roomCharge
            verify(mockPreparedStatement).setBigDecimal(3, new BigDecimal("375.00"));    // tax 5%
            verify(mockPreparedStatement).setBigDecimal(4, new BigDecimal("750.00"));    // serviceCharge 10%
            verify(mockPreparedStatement).setBigDecimal(5, new BigDecimal("8625.00"));   // total
        }
    }

   
    @Test
    @DisplayName("TC-B03: createBill() forces days=1 when check-in equals check-out (your guard: if days<=0 days=1)")
    void testCreateBill_SameDayCheckInOut_ForcedToOneDay() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Reservation r = new Reservation();
            r.setReservationId(9);
            r.setRoomId(2);
            r.setCheckIn(LocalDate.of(2025, 9, 5));
            r.setCheckOut(LocalDate.of(2025, 9, 5));   // same day → days=0 → forced to 1
            when(mockReservationDAO.getReservationById(9)).thenReturn(r);
            when(mockRoomDAO.getRoomPriceById(2)).thenReturn(new BigDecimal("6000.00"));

            BillDAOTestable dao = new BillDAOTestable(mockReservationDAO, mockRoomDAO);
            dao.createBill(9);

            // days forced to 1 → all values based on 1 night
            verify(mockPreparedStatement).setBigDecimal(2, new BigDecimal("6000.00"));  // roomCharge
            verify(mockPreparedStatement).setBigDecimal(3, new BigDecimal("300.00"));   // tax
            verify(mockPreparedStatement).setBigDecimal(4, new BigDecimal("600.00"));   // service
            verify(mockPreparedStatement).setBigDecimal(5, new BigDecimal("6900.00"));  // total
        }
    }

    
    @Test
    @DisplayName("TC-B04: createBill() returns early without INSERT when reservation is not found (null)")
    void testCreateBill_ReservationNotFound_NoInsertExecuted() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);

            // ReservationDAO returns null
            when(mockReservationDAO.getReservationById(9999)).thenReturn(null);

            BillDAOTestable dao = new BillDAOTestable(mockReservationDAO, mockRoomDAO);

            assertDoesNotThrow(() -> dao.createBill(9999),
                "TC-B04 FAIL: createBill() should not throw when reservation is null");

            // prepareStatement should NEVER be called — early return happened
            verify(mockConnection, never()).prepareStatement(anyString());
        }
    }

   
    @Test
    @DisplayName("TC-B05: getBillByReservation() returns correct Bill object when bill exists for reservation_id")
    void testGetBillByReservation_Found_ReturnsBill() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            // DB returns one row
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("bill_id"))              .thenReturn(1);
            when(mockResultSet.getInt("reservation_id"))       .thenReturn(7);
            when(mockResultSet.getBigDecimal("room_charge"))   .thenReturn(new BigDecimal("20000.00"));
            when(mockResultSet.getBigDecimal("tax"))           .thenReturn(new BigDecimal("1000.00"));
            when(mockResultSet.getBigDecimal("service_charge")).thenReturn(new BigDecimal("2000.00"));
            when(mockResultSet.getBigDecimal("total_amount"))  .thenReturn(new BigDecimal("23000.00"));
            when(mockResultSet.getString("status"))            .thenReturn("UNPAID");

            BillDAOTestable dao = new BillDAOTestable(mockReservationDAO, mockRoomDAO);
            Bill result = dao.getBillByReservation(7);

            // Assert every field your code maps from ResultSet
            assertNotNull(result,
                "TC-B05 FAIL: getBillByReservation() should return a Bill object when found");
            assertEquals(1, result.getBillId(),
                "TC-B05 FAIL: bill_id should be 1");
            assertEquals(7, result.getReservationId(),
                "TC-B05 FAIL: reservation_id should be 7");
            assertEquals(new BigDecimal("20000.00"), result.getRoomCharge(),
                "TC-B05 FAIL: room_charge should be 20000.00");
            assertEquals(new BigDecimal("1000.00"), result.getTax(),
                "TC-B05 FAIL: tax should be 1000.00");
            assertEquals(new BigDecimal("2000.00"), result.getServiceCharge(),
                "TC-B05 FAIL: service_charge should be 2000.00");
            assertEquals(new BigDecimal("23000.00"), result.getTotalAmount(),
                "TC-B05 FAIL: total_amount should be 23000.00");
            assertEquals("UNPAID", result.getStatus(),
                "TC-B05 FAIL: status should be 'UNPAID'");

            // Verify reservation_id=7 was passed to PreparedStatement
            verify(mockPreparedStatement).setInt(1, 7);
        }
    }

    
    @Test
    @DisplayName("TC-B06: getBillByReservation() returns null when no bill exists for the given reservation_id")
    void testGetBillByReservation_NotFound_ReturnsNull() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            // ResultSet is empty
            when(mockResultSet.next()).thenReturn(false);

            BillDAOTestable dao = new BillDAOTestable(mockReservationDAO, mockRoomDAO);
            Bill result = dao.getBillByReservation(9999);

            assertNull(result,
                "TC-B06 FAIL: getBillByReservation() should return null when no bill found");
        }
    }

    
    @Test
    @DisplayName("TC-B07: markAsPaid() with valid billId executes UPDATE SET status=PAID without exception")
    void testMarkAsPaid_ValidBillId_ExecutesUpdate() throws Exception {

        try (MockedStatic<DBConnection> dbMock = mockStatic(DBConnection.class)) {

            dbMock.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString()))
                .thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            BillDAOTestable dao = new BillDAOTestable(mockReservationDAO, mockRoomDAO);

            assertDoesNotThrow(() -> dao.markAsPaid(1),
                "TC-B07 FAIL: markAsPaid() should not throw for a valid bill ID");

            // Verify bill_id=1 was set as parameter 1
            verify(mockPreparedStatement).setInt(1, 1);
            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }
}