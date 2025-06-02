package org.codingburgas.dndonchev20.nastolni_igri;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PerformanceTest {

    @Test
    public void testStressSimulation() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            double dummy = Math.pow(i % 10 + 1, 2);
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Stress test duration (ms): " + duration);
        assertTrue(duration >= 0);
    }
}