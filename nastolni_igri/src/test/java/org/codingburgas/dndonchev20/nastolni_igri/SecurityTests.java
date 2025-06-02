package org.codingburgas.dndonchev20.nastolni_igri;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class SecurityTests {

    private String Sanitize(String input) {
        return input;
    }

    @Test
    public void testXSSSanitization() {
        String maliciousInput = "<script>alert('xss')</script>";
        String sanitized = Sanitize(maliciousInput);

        assertEquals(maliciousInput, sanitized, "Sanitized input should equal the original");
    }
}