package validation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.oceanview.util.InputValidator;


public class Inputvalidationtest {

   

    
    @Test
    @DisplayName("TC-V01: validateLoginEmail() returns true for a valid non-empty email")
    void testValidateLoginEmail_ValidEmail_ReturnsTrue() {

        assertTrue(InputValidator.validateLoginEmail("nimal@hotel.com"),
            "TC-V01 FAIL: 'nimal@hotel.com' is a valid email — should return true");
    }

   
    @Test
    @DisplayName("TC-V02: validateLoginEmail() returns false for null input")
    void testValidateLoginEmail_Null_ReturnsFalse() {

        assertFalse(InputValidator.validateLoginEmail(null),
            "TC-V02 FAIL: null email should return false");
    }

   
    @Test
    @DisplayName("TC-V03: validateLoginEmail() returns false for empty string (blank form field)")
    void testValidateLoginEmail_EmptyString_ReturnsFalse() {

        assertFalse(InputValidator.validateLoginEmail(""),
            "TC-V03 FAIL: empty email field should return false");
    }

    
    @Test
    @DisplayName("TC-V04: validateLoginPassword() returns true for a valid non-empty password")
    void testValidateLoginPassword_ValidPassword_ReturnsTrue() {

        assertTrue(InputValidator.validateLoginPassword("password123"),
            "TC-V04 FAIL: 'password123' is a valid password — should return true");
    }

    
    @Test
    @DisplayName("TC-V05: validateLoginPassword() returns false for null input")
    void testValidateLoginPassword_Null_ReturnsFalse() {

        assertFalse(InputValidator.validateLoginPassword(null),
            "TC-V05 FAIL: null password should return false");
    }

    // ── TC-V06 : validateLoginPassword() — empty string → false ──────
    @Test
    @DisplayName("TC-V06: validateLoginPassword() returns false for empty string (blank form field)")
    void testValidateLoginPassword_EmptyString_ReturnsFalse() {

        assertFalse(InputValidator.validateLoginPassword(""),
            "TC-V06 FAIL: empty password field should return false");
    }

    
    @Test
    @DisplayName("TC-V07: validateCheckOutAfterCheckIn() returns true when checkOut is after checkIn")
    void testValidateDates_CheckOutAfterCheckIn_ReturnsTrue() {

        // checkIn: 10 Aug, checkOut: 14 Aug — valid 4-night stay
        assertTrue(
            InputValidator.validateCheckOutAfterCheckIn(
                LocalDate.of(2025, 8, 10),
                LocalDate.of(2025, 8, 14)
            ),
            "TC-V07 FAIL: checkOut (14 Aug) is after checkIn (10 Aug) — should return true"
        );
    }

   
    // Source: checkOut.isAfter(checkIn) — same day returns false (0 nights)
    @Test
    @DisplayName("TC-V08: validateCheckOutAfterCheckIn() returns false when checkOut equals checkIn (same-day, 0 nights)")
    void testValidateDates_SameDay_ReturnsFalse() {

        assertFalse(
            InputValidator.validateCheckOutAfterCheckIn(
                LocalDate.of(2025, 8, 10),
                LocalDate.of(2025, 8, 10)   // same day — your code: !checkOut.isAfter(checkIn)
            ),
            "TC-V08 FAIL: same-day check-in/out should return false (0 nights not allowed)"
        );
    }

   
    @Test
    @DisplayName("TC-V09: validateCheckOutAfterCheckIn() returns false when checkOut is before checkIn")
    void testValidateDates_CheckOutBeforeCheckIn_ReturnsFalse() {

        assertFalse(
            InputValidator.validateCheckOutAfterCheckIn(
                LocalDate.of(2025, 8, 14),  // checkIn
                LocalDate.of(2025, 8, 10)   // checkOut BEFORE checkIn — invalid
            ),
            "TC-V09 FAIL: checkOut before checkIn should return false"
        );
    }

    
    @Test
    @DisplayName("TC-V10: validateCheckOutAfterCheckIn() returns false when either date is null")
    void testValidateDates_NullDates_ReturnsFalse() {

        assertFalse(
            InputValidator.validateCheckOutAfterCheckIn(null, LocalDate.of(2025, 8, 14)),
            "TC-V10 FAIL: null checkIn should return false"
        );
        assertFalse(
            InputValidator.validateCheckOutAfterCheckIn(LocalDate.of(2025, 8, 10), null),
            "TC-V10 FAIL: null checkOut should return false"
        );
    }

    

    // ── TC-V11 : validateCustomerName() — valid name → true ──────────
    @Test
    @DisplayName("TC-V11: validateCustomerName() returns true for a valid non-empty customer name")
    void testValidateCustomerName_ValidName_ReturnsTrue() {

        assertTrue(InputValidator.validateCustomerName("Nimal Perera"),
            "TC-V11 FAIL: 'Nimal Perera' is a valid name — should return true");
    }

    // ── TC-V12 : validateCustomerName() — null → false ───────────────
    @Test
    @DisplayName("TC-V12: validateCustomerName() returns false for null input")
    void testValidateCustomerName_Null_ReturnsFalse() {

        assertFalse(InputValidator.validateCustomerName(null),
            "TC-V12 FAIL: null customer name should return false");
    }

    // ── TC-V13 : validateCustomerName() — empty string → false ───────
    @Test
    @DisplayName("TC-V13: validateCustomerName() returns false for empty string (blank name field)")
    void testValidateCustomerName_EmptyString_ReturnsFalse() {

        assertFalse(InputValidator.validateCustomerName(""),
            "TC-V13 FAIL: empty customer name should return false");
    }

    // ═══════════════════════════════════════════════════════════════════
    //  PHONE VALIDATION
    //  Source: ReservationServlet — phone = request.getParameter("phone")
    // ═══════════════════════════════════════════════════════════════════

    // ── TC-V14 : validatePhone() — valid 10-digit number → true ──────
    @Test
    @DisplayName("TC-V14: validatePhone() returns true for a valid 10-digit Sri Lankan phone number")
    void testValidatePhone_ValidNumber_ReturnsTrue() {

        assertTrue(InputValidator.validatePhone("0771234567"),
            "TC-V14 FAIL: '0771234567' is a valid 10-digit phone number — should return true");
    }

    // ── TC-V15 : validatePhone() — invalid formats → false ───────────
    @ParameterizedTest(name = "TC-V15: validatePhone() rejects invalid phone: [{0}]")
    @ValueSource(strings = {
        "077123",           // too short (6 digits)
        "07712345678",      // too long (11 digits)
        "077ABC4567",       // contains letters
        "",                 // empty string
        "077 123 4567"      // contains spaces
    })
    @DisplayName("TC-V15: validatePhone() returns false for invalid phone number formats")
    void testValidatePhone_InvalidFormats_ReturnsFalse(String phone) {

        assertFalse(InputValidator.validatePhone(phone),
            "TC-V15 FAIL: '" + phone + "' should fail phone validation");
    }

    // ═══════════════════════════════════════════════════════════════════
    //  ROOM ID VALIDATION
    //  Source: ReservationServlet —
    //    roomId = Integer.parseInt(request.getParameter("roomId"))
    // ═══════════════════════════════════════════════════════════════════

    // ── TC-V16 : validateRoomId() — valid positive ID → true ─────────
    @Test
    @DisplayName("TC-V16: validateRoomId() returns true for a valid positive room ID")
    void testValidateRoomId_ValidPositiveId_ReturnsTrue() {

        assertTrue(InputValidator.validateRoomId(3),
            "TC-V16 FAIL: roomId=3 is a valid positive ID — should return true");
    }

    // ── TC-V17 : validateRoomId() — zero and negative → false ────────
    @ParameterizedTest(name = "TC-V17: validateRoomId() rejects invalid ID: [{0}]")
    @ValueSource(ints = { 0, -1, -100 })
    @DisplayName("TC-V17: validateRoomId() returns false for zero and negative room IDs")
    void testValidateRoomId_ZeroAndNegative_ReturnsFalse(int roomId) {

        assertFalse(InputValidator.validateRoomId(roomId),
            "TC-V17 FAIL: roomId=" + roomId + " should fail validation (must be > 0)");
    }

    // ═══════════════════════════════════════════════════════════════════
    //  EMAIL FORMAT VALIDATION
    //  Source: ReservationServlet — email = request.getParameter("email")
    // ═══════════════════════════════════════════════════════════════════

    // ── TC-V18 : validateEmailFormat() — valid format → true ─────────
    @Test
    @DisplayName("TC-V18: validateEmailFormat() returns true for a correctly formatted email address")
    void testValidateEmailFormat_ValidEmail_ReturnsTrue() {

        assertTrue(InputValidator.validateEmailFormat("nimal@hotel.com"),
            "TC-V18 FAIL: 'nimal@hotel.com' is a valid email format — should return true");
    }

    // ── TC-V19 : validateEmailFormat() — invalid formats → false ─────
    @ParameterizedTest(name = "TC-V19: validateEmailFormat() rejects invalid email: [{0}]")
    @ValueSource(strings = {
        "notanemail",         // no @ symbol
        "missing@",           // no domain
        "@nodomain.com",      // no local part
        "spaces in@email.com",// contains space
        ""                    // empty string
    })
    @DisplayName("TC-V19: validateEmailFormat() returns false for invalid email formats")
    void testValidateEmailFormat_InvalidFormats_ReturnsFalse(String email) {

        assertFalse(InputValidator.validateEmailFormat(email),
            "TC-V19 FAIL: '" + email + "' should fail email format validation");
    }

    // ═══════════════════════════════════════════════════════════════════
    //  ADDRESS VALIDATION
    //  Source: ReservationServlet — address = request.getParameter("address")
    // ═══════════════════════════════════════════════════════════════════

    // ── TC-V20 : validateAddress() — valid address → true ────────────
    @Test
    @DisplayName("TC-V20: validateAddress() returns true for a valid non-empty address")
    void testValidateAddress_ValidAddress_ReturnsTrue() {

        assertTrue(InputValidator.validateAddress("123 Galle Road, Colombo"),
            "TC-V20 FAIL: '123 Galle Road, Colombo' is a valid address — should return true");
    }

    // ── TC-V21 : validateAddress() — null and empty → false ──────────
    @Test
    @DisplayName("TC-V21: validateAddress() returns false for null and empty address")
    void testValidateAddress_NullAndEmpty_ReturnsFalse() {

        assertFalse(InputValidator.validateAddress(null),
            "TC-V21 FAIL: null address should return false");

        assertFalse(InputValidator.validateAddress(""),
            "TC-V21 FAIL: empty address should return false");
    }
}
