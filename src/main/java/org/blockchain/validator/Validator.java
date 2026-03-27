package org.blockchain.validator;

public class Validator {
    private static final int INT_POSITIVE_LOWER_BOUND = 0;
    private static final double DOUBLE_POSITIVE_LOWER_BOUND = 0.0;

    public static String requireNotNullNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " should not be null/blank.");
        }
        return value;
    }

    public static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " should not be null.");
        }
        return value;
    }


    public static int requirePositive(int value, String fieldName) {
        if (value <= INT_POSITIVE_LOWER_BOUND) {
            throw new IllegalArgumentException(fieldName + " should be positive.");
        }
        return value;
    }

    public static double requirePositive(double value, String fieldName) {
        if (value <= DOUBLE_POSITIVE_LOWER_BOUND) {
            throw new IllegalArgumentException(fieldName + " should be positive.");
        }
        return value;
    }
}