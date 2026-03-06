package dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanview.dao.StaffDAO;
import com.oceanview.model.Staff;



@ExtendWith(MockitoExtension.class)
public class Staffdaotest {

    // ── Mocked JDBC objects ──────────────────────────────────────────────
    @Mock private Connection        mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet         mockResultSet;

    // Static mocks — intercept DBConnection and PasswordUtil
    private MockedStatic<com.oceanview.util.DBConnection> mockDBConnection;
    private MockedStatic<com.oceanview.util.PasswordUtil>  mockPasswordUtil;

    private StaffDAO staffDAO;

    // ── Set up before EACH test ──────────────────────────────────────────
    @BeforeEach
    void setUp() throws Exception {
        // Open static mocks BEFORE constructing StaffDAO
        mockDBConnection = mockStatic(com.oceanview.util.DBConnection.class);
        mockPasswordUtil  = mockStatic(com.oceanview.util.PasswordUtil.class);

        // Redirect DBConnection.getConnection() → our mock Connection
        mockDBConnection
            .when(com.oceanview.util.DBConnection::getConnection)
            .thenReturn(mockConnection);

        // StaffDAO constructor now uses the mock connection
        staffDAO = new StaffDAO();
    }

    // ── Tear down after EACH test — static mocks MUST be closed ─────────
    @AfterEach
    void tearDown() {
        mockDBConnection.close();
        mockPasswordUtil.close();
    }

    
    @Test
    @DisplayName("TC-S01: login() with valid credentials returns a Staff object")
    void testLogin_ValidCredentials_ReturnsStaff() throws Exception {

        // Arrange — DB finds the email row
        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password"))  .thenReturn("$2a$10$hashedvalue");
        when(mockResultSet.getInt("staff_id"))     .thenReturn(1);
        when(mockResultSet.getString("full_name")) .thenReturn("John Silva");
        when(mockResultSet.getString("email"))     .thenReturn("john@hotel.com");
        when(mockResultSet.getString("role"))      .thenReturn("STAFF");

        // PasswordUtil.checkPassword returns true (correct BCrypt match)
        mockPasswordUtil
            .when(() -> com.oceanview.util.PasswordUtil
                .checkPassword("Pass@123", "$2a$10$hashedvalue"))
            .thenReturn(true);

        // Act
        Staff result = staffDAO.login("john@hotel.com", "Pass@123");

        // Assert
        assertNotNull(result,
            "TC-S01 FAIL: login() should return a Staff object for valid credentials");
        assertEquals(1,            result.getStaffId(),
            "TC-S01 FAIL: staff_id should be 1");
        assertEquals("John Silva", result.getFullName(),
            "TC-S01 FAIL: full_name should be 'John Silva'");
        assertEquals("STAFF",      result.getRole(),
            "TC-S01 FAIL: role should be 'STAFF'");

        // Verify email was bound to PreparedStatement parameter 1
        verify(mockPreparedStatement).setString(1, "john@hotel.com");
    }

    @Test
    @DisplayName("TC-S02: login() with wrong password returns null")
    void testLogin_WrongPassword_ReturnsNull() throws Exception {

        // Arrange — row found but BCrypt check fails
        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password")).thenReturn("$2a$10$hashedvalue");

        // PasswordUtil.checkPassword returns false (wrong password)
        mockPasswordUtil
            .when(() -> com.oceanview.util.PasswordUtil
                .checkPassword("wrongPass", "$2a$10$hashedvalue"))
            .thenReturn(false);

        // Act
        Staff result = staffDAO.login("john@hotel.com", "wrongPass");

        // Assert
        assertNull(result,
            "TC-S02 FAIL: login() should return null when BCrypt check fails");
    }

    
    @Test
    @DisplayName("TC-S03: login() with an unregistered email returns null")
    void testLogin_EmailNotFound_ReturnsNull() throws Exception {

        // Arrange — ResultSet is empty
        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);   // no matching row

        // Act
        Staff result = staffDAO.login("nobody@hotel.com", "anyPassword");

        // Assert
        assertNull(result,
            "TC-S03 FAIL: login() should return null when email is not in the database");
    }

  
    @Test
    @DisplayName("TC-S04: getAllStaff() returns a List of 2 Staff when 2 rows exist")
    void testGetAllStaff_TwoRows_ReturnsListOfTwo() throws Exception {

        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);

        // Simulate two rows then end of ResultSet
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("staff_id"))    .thenReturn(1, 2);
        when(mockResultSet.getString("full_name")).thenReturn("John Silva", "Alice Perera");
        when(mockResultSet.getString("email"))    .thenReturn("john@h.com", "alice@h.com");
        when(mockResultSet.getString("role"))     .thenReturn("STAFF", "ADMIN");

        // Act
        List<Staff> result = staffDAO.getAllStaff();

        // Assert
        assertNotNull(result,
            "TC-S04 FAIL: getAllStaff() must never return null");
        assertEquals(2, result.size(),
            "TC-S04 FAIL: should return exactly 2 Staff objects");
        assertEquals("John Silva",   result.get(0).getFullName(),
            "TC-S04 FAIL: first staff should be 'John Silva'");
        assertEquals("Alice Perera", result.get(1).getFullName(),
            "TC-S04 FAIL: second staff should be 'Alice Perera'");
    }

    
    @Test
    @DisplayName("TC-S05: getAllStaff() returns an empty list when the staff table is empty")
    void testGetAllStaff_EmptyTable_ReturnsEmptyList() throws Exception {

        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        List<Staff> result = staffDAO.getAllStaff();

        assertNotNull(result,
            "TC-S05 FAIL: getAllStaff() should return an empty List, not null");
        assertTrue(result.isEmpty(),
            "TC-S05 FAIL: List should be empty when no staff records exist");
    }

   
    @Test
    @DisplayName("TC-S06: addStaff() with a valid Staff object executes INSERT without exception")
    void testAddStaff_ValidStaff_ExecutesInsert() throws Exception {

        // Arrange
        Staff newStaff = new Staff();
        newStaff.setFullName("Alice Perera");
        newStaff.setEmail("alice@hotel.com");
        newStaff.setPassword("$2a$10$hashedAlice");
        newStaff.setRole("STAFF");

        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act & Assert
        assertDoesNotThrow(() -> staffDAO.addStaff(newStaff),
            "TC-S06 FAIL: addStaff() should not throw for a valid Staff object");

        // Verify parameters match INSERT INTO staff (full_name, email, password, role)
        verify(mockPreparedStatement).setString(1, "Alice Perera");
        verify(mockPreparedStatement).setString(2, "alice@hotel.com");
        verify(mockPreparedStatement).setString(3, "$2a$10$hashedAlice");
        verify(mockPreparedStatement).setString(4, "STAFF");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }


    @Test
    @DisplayName("TC-S07: getStaffById() with a valid ID returns the correct Staff object")
    void testGetStaffById_ValidId_ReturnsStaff() throws Exception {

        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("staff_id"))    .thenReturn(3);
        when(mockResultSet.getString("full_name")).thenReturn("Kamal Fernando");
        when(mockResultSet.getString("email"))    .thenReturn("kamal@hotel.com");
        when(mockResultSet.getString("role"))     .thenReturn("STAFF");

        // Act
        Staff result = staffDAO.getStaffById(3);

        // Assert
        assertNotNull(result,
            "TC-S07 FAIL: getStaffById() should return a Staff for a valid ID");
        assertEquals(3,                result.getStaffId(),
            "TC-S07 FAIL: staff_id should be 3");
        assertEquals("Kamal Fernando", result.getFullName(),
            "TC-S07 FAIL: full_name should be 'Kamal Fernando'");

        // Verify ID was correctly bound to parameter 1
        verify(mockPreparedStatement).setInt(1, 3);
    }

    
    @Test
    @DisplayName("TC-S08: getStaffById() with a non-existent ID returns null")
    void testGetStaffById_IdNotFound_ReturnsNull() throws Exception {

        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Staff result = staffDAO.getStaffById(9999);

        assertNull(result,
            "TC-S08 FAIL: getStaffById() should return null for an ID that does not exist");
    }


    @Test
    @DisplayName("TC-S09: updateStaff() with a valid Staff object executes UPDATE without exception")
    void testUpdateStaff_ValidStaff_ExecutesUpdate() throws Exception {

        // Arrange
        Staff updated = new Staff();
        updated.setStaffId(3);
        updated.setFullName("Kamal Updated");
        updated.setEmail("kamal.new@hotel.com");
        updated.setRole("ADMIN");

        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act & Assert
        assertDoesNotThrow(() -> staffDAO.updateStaff(updated),
            "TC-S09 FAIL: updateStaff() should not throw for a valid Staff");

        // Verify parameters match UPDATE staff SET full_name=?, email=?, role=? WHERE staff_id=?
        verify(mockPreparedStatement).setString(1, "Kamal Updated");
        verify(mockPreparedStatement).setString(2, "kamal.new@hotel.com");
        verify(mockPreparedStatement).setString(3, "ADMIN");
        verify(mockPreparedStatement).setInt(4, 3);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    
    @Test
    @DisplayName("TC-S10: deleteStaff() with a valid ID executes DELETE without exception")
    void testDeleteStaff_ValidId_ExecutesDelete() throws Exception {

        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act & Assert
        assertDoesNotThrow(() -> staffDAO.deleteStaff(3),
            "TC-S10 FAIL: deleteStaff() should not throw for a valid staff ID");

        // Verify ID was bound and DELETE was executed
        verify(mockPreparedStatement).setInt(1, 3);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

  
    @Test
    @DisplayName("TC-S11: searchStaff() with a matching keyword returns a non-empty List")
    void testSearchStaff_MatchingKeyword_ReturnsList() throws Exception {

        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);   // 1 matching row
        when(mockResultSet.getInt("staff_id"))    .thenReturn(1);
        when(mockResultSet.getString("full_name")).thenReturn("John Silva");
        when(mockResultSet.getString("email"))    .thenReturn("john@hotel.com");
        when(mockResultSet.getString("role"))     .thenReturn("STAFF");

        // Act
        List<Staff> result = staffDAO.searchStaff("John");

        // Assert
        assertNotNull(result,
            "TC-S11 FAIL: searchStaff() should not return null");
        assertEquals(1, result.size(),
            "TC-S11 FAIL: should return 1 result for keyword 'John'");
        assertEquals("John Silva", result.get(0).getFullName(),
            "TC-S11 FAIL: matched staff name should be 'John Silva'");

        // Verify LIKE wildcard wrapping: %keyword%
        verify(mockPreparedStatement).setString(1, "%John%");
        verify(mockPreparedStatement).setString(2, "%John%");
        verify(mockPreparedStatement).setString(3, "%John%");
    }

    
    @Test
    @DisplayName("TC-S12: searchStaff() with a non-matching keyword returns an empty List")
    void testSearchStaff_NoMatch_ReturnsEmptyList() throws Exception {

        when(mockConnection.prepareStatement(anyString()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);   // nothing matched

        // Act
        List<Staff> result = staffDAO.searchStaff("XYZNOTEXIST");

        // Assert
        assertNotNull(result,
            "TC-S12 FAIL: searchStaff() should return an empty List, not null");
        assertTrue(result.isEmpty(),
            "TC-S12 FAIL: List should be empty when keyword matches no staff records");
    }
}
