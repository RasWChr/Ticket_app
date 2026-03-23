package com.example.tickets_app.BLL.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    // Email tests
    @Test
    void testValidEmail() {
        assertTrue(ValidationUtil.isValidEmail("felix@example.com"));
    }

    @Test
    void testValidEmailWithSubdomain() {
        assertTrue(ValidationUtil.isValidEmail("felix@mail.example.com"));
    }

    @Test
    void testValidEmailWithPlusSign() {
        assertTrue(ValidationUtil.isValidEmail("felix+test@example.com"));
    }

    @Test
    void testEmailMissingAtSign() {
        assertFalse(ValidationUtil.isValidEmail("felixexample.com"));
    }

    @Test
    void testEmailMissingDomain() {
        assertFalse(ValidationUtil.isValidEmail("felix@"));
    }

    @Test
    void testEmailMissingLocalPart() {
        assertFalse(ValidationUtil.isValidEmail("@example.com"));
    }

    @Test
    void testEmailMissingTLD() {
        assertFalse(ValidationUtil.isValidEmail("felix@example"));
    }

    @Test
    void testEmailBlank() {
        assertFalse(ValidationUtil.isValidEmail(""));
    }

    @Test
    void testEmailNull() {
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    // Phone tests
    @Test
    void testValidPhone() {
        assertTrue(ValidationUtil.isValidPhone("12345678"));
    }

    @Test
    void testValidPhoneWithPlus() {
        assertTrue(ValidationUtil.isValidPhone("+4512345678"));
    }

    @Test
    void testValidPhoneMinLength() {
        assertTrue(ValidationUtil.isValidPhone("1234567"));
    }

    @Test
    void testValidPhoneMaxLength() {
        assertTrue(ValidationUtil.isValidPhone("123456789012345"));
    }

    @Test
    void testPhoneTooShort() {
        assertFalse(ValidationUtil.isValidPhone("123456"));
    }

    @Test
    void testPhoneTooLong() {
        assertFalse(ValidationUtil.isValidPhone("1234567890123456"));
    }

    @Test
    void testPhoneWithLetters() {
        assertFalse(ValidationUtil.isValidPhone("1234abc678"));
    }

    @Test
    void testPhoneWithSpaces() {
        assertFalse(ValidationUtil.isValidPhone("1234 5678"));
    }

    @Test
    void testPhoneBlankIsValid() {
        assertTrue(ValidationUtil.isValidPhone(""));
    }

}