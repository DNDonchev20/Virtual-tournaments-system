package org.codingburgas.dndonchev20.nastolni_igri.utils;

public class ModUtil {

    // Method for calculating the modulo of a Long value
    public static int mod(Long value, int divisor) {
        return value.intValue() % divisor;
    }
}