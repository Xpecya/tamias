package com.xpecya.tamias.core;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class Logger {

    public static LoggingLevel LEVEL = LoggingLevel.ERROR;

    private static final String LONG_PATTERN = "[%s] %s %s";
    private static final String SHORT_PATTERN = "[%s]  %s %s";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")
            .withZone(ZoneId.of("Asia/Shanghai"));

    private Logger() {}

    private static void print(LoggingLevel level, String pattern, String message) {
        int levelCode = LEVEL.levelCode;
        int requestLevelCode = level.levelCode;
        if (levelCode <= requestLevelCode) {
            String levelName = level.levelName;
            System.out.println(String.format(pattern, levelName, FORMATTER.format(Instant.now()), message));
        }
    }

    public static void info(String message) {
        print(LoggingLevel.INFO, SHORT_PATTERN, message);
    }

    public static void warn(String message) {
        print(LoggingLevel.WARN, SHORT_PATTERN, message);
    }

    public static void error(String message) {
        print(LoggingLevel.ERROR, LONG_PATTERN, message);
    }

    public static void debug(String message) {
        print(LoggingLevel.DEBUG, LONG_PATTERN, message);
    }

    public enum LoggingLevel {
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
