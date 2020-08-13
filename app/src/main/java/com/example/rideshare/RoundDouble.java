package com.example.rideshare;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A class with a single static method for rounding doubles to a given number of places
 *
 * @author Sukriti
 * @version 1.0
 */
public class RoundDouble {
    /**
     * Takes a double and rounds it to the given number of places
     *
     * @param value  the double value that is to be rounded
     * @param places number of places to which the double value has to be rounded
     * @return rounded double
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}

