package br.usp.raulmello.ui;

public class Logger {

    private static final boolean DEBUG_ENABLED = false;

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

    private static String replaceCurlyBraces(final String message, final Object... args) {
        String result = message;
        for (final Object arg : args) {
            result = result.replaceFirst("\\{}", arg.toString());
        }

        return result;
    }
}
