package com.example.tickets_app.BLL.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    void testHashIsNotPlainText() {
        String plain = "myPassword123";
        String hashed = PasswordUtil.hash(plain);

        assertNotEquals(plain, hashed);
    }

    @Test
    void testHashStartsWithBCryptPrefix() {
        String hashed = PasswordUtil.hash("myPassword123");

        assertTrue(hashed.startsWith("$2a$"));
    }

    @Test
    void testVerifyCorrectPassword() {
        String plain = "myPassword123";
        String hashed = PasswordUtil.hash(plain);

        assertTrue(PasswordUtil.verify(plain, hashed));
    }

    @Test
    void testVerifyWrongPassword() {
        String hashed = PasswordUtil.hash("myPassword123");

        assertFalse(PasswordUtil.verify("wrongPassword", hashed));
    }

    @Test
    void testTwoHashesOfSamePasswordAreNotEqual() {
        String plain = "myPassword123";
        String hash1 = PasswordUtil.hash(plain);
        String hash2 = PasswordUtil.hash(plain);

        assertNotEquals(hash1, hash2);
    }
}