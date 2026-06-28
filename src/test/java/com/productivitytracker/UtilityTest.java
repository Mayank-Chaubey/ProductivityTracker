package com.productivitytracker;

import com.productivitytracker.util.JsonUtil;
import com.productivitytracker.util.SecurityTokenUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @Test
    void generatedTokensAreUniqueAndHashable() {
        String first = SecurityTokenUtil.generateToken();
        String second = SecurityTokenUtil.generateToken();

        assertNotEquals(first, second);
        assertEquals(64, SecurityTokenUtil.sha256(first).length());
        assertEquals(SecurityTokenUtil.sha256(first), SecurityTokenUtil.sha256(first));
    }

    @Test
    void jsonQuoteEscapesSpecialCharacters() {
        assertEquals("\"hello\\n\\\"world\\\"\"", JsonUtil.quote("hello\n\"world\""));
        assertEquals("null", JsonUtil.quote(null));
    }
}
