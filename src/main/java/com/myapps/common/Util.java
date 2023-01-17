package com.myapps.common;

import java.util.Collection;
import java.util.Map;

public class Util {

    public static boolean isNullOrEmpty(final Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNullOrEmpty(final Map<?, ?> m) {
        return m == null || m.isEmpty();
    }

    public static double roundOff(double num1, double num2) {
        return (double) Math.round(num1 / num2 * 100) / 100;
    }
}
