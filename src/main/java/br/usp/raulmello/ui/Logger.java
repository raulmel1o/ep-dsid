package br.usp.raulmello.ui;

import java.util.Arrays;

public class Logger {

    private static final boolean DEBUG_ENABLED = true;

    private Logger() {}

    public static void info(final String message, final Object... args) {
        System.out.println(replaceCurlyBraces(message, args));
    }

    public static void debug(final String message, final Object... args) {
        if (!DEBUG_ENABLED) {
            return;
        }

        System.out.println(replaceCurlyBraces(message, args));
    }

    public static void debug(final String message, final Throwable throwable, final String... args) {
        if (!DEBUG_ENABLED) {
            return;
        }

        System.out.println(replaceCurlyBraces(message, args) + "\n\n" + throwable.getMessage() + "\n\n" + Arrays.toString(throwable.getStackTrace()));
    }

    private static String replaceCurlyBraces(final String message, final Object... args) {
        String result = message;
        for (final Object arg : args) {
            result = result.replaceFirst("\\{}", arg.toString());
        }

        return result;
    }
}
