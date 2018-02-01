package io.github.buraksarp.urlsigner.util;

public final class Assert {

    private Assert() {}

    public static boolean hasText(String text) {
        if (text == null || text.trim().length() == 0) {
            return false;
        }
        return true;
    }

    public static void notNullorEmpty(String text, String message) {
        if (text == null || text.trim().length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if(object == null) {
            throw new IllegalArgumentException(message);
        }
    }

}
