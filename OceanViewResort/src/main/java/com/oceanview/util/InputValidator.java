package com.oceanview.util;

import java.time.LocalDate;
import java.util.regex.Pattern;


public class InputValidator {

    // Email pattern: standard format local@domain.tld
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,6}$");

    // Phone: exactly 10 digits (Sri Lankan mobile format e.g. 0771234567)
    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^[0-9]{10}$");

    // ── LOGIN VALIDATION ─────────────────────────────────────────────
    // Source: LoginServlet.doPost()

    /** Email must not be null or blank. */
    public static boolean validateLoginEmail(String email) {
        return email != null && !email.trim().isEmpty();
    }

    /** Password must not be null or blank. */
    public static boolean validateLoginPassword(String password) {
        return password != null && !password.trim().isEmpty();
    }

    /** Email must match valid format (x@domain.tld). */
    public static boolean validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    // ── RESERVATION VALIDATION ───────────────────────────────────────
    // Source: ReservationServlet.addReservation() and handleAPIPost()

    /**
     * checkOut must be strictly AFTER checkIn.
     * Source: if (!checkOut.isAfter(checkIn)) → error shown to user
     */
    public static boolean validateCheckOutAfterCheckIn(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) return false;
        return checkOut.isAfter(checkIn);
    }

    /** checkIn must not be in the past. */
    public static boolean validateCheckInNotInPast(LocalDate checkIn) {
        if (checkIn == null) return false;
        return !checkIn.isBefore(LocalDate.now());
    }

    /** Customer name must not be null or blank. */
    public static boolean validateCustomerName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /** Phone must be exactly 10 digits. */
    public static boolean validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /** Room ID must be greater than 0. */
    public static boolean validateRoomId(int roomId) {
        return roomId > 0;
    }

    /** Address must not be null or blank. */
    public static boolean validateAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }
}
