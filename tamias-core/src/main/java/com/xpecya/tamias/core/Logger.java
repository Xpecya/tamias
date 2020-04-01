package com.xpecya.tamias.core;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public final class Logger {

    public static LoggingLevel LEVEL = LoggingLevel.ERROR;

    private static final String PATTERN = "[%s] %s %s";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    private Logger() {}

    private static void print(LoggingLevel level, String message) {
        int levelCode = LEVEL.levelCode;
        int requestLevelCode = level.levelCode;
        if (levelCode > requestLevelCode) {
            String levelName = level.levelName;
            System.out.println(String.format(PATTERN, levelName, FORMATTER.format(Instant.now()), message));
        }
    }

    public static void info(String message) {
        print(LoggingLevel.INFO, message);
    }

    public static void warn(String message) {
        print(LoggingLevel.WARN, message);
    }

    public static void error(String message) {
        print(LoggingLevel.ERROR, message);
    }

    public static void debug(String message) {
        print(LoggingLevel.DEBUG, message);
    }

    private enum LoggingLevel {
        DEBUG(0, "DEBUG"),
        ERROR(1, "ERROR"),
        WARN(2, "WARN"),
        INFO(3, "INFO");

        private int levelCode;
        private String levelName;

        LoggingLevel(int levelCode, String levelName) {
            this.levelCode = levelCode;
            this.levelName = levelName;
        }
    }
}
