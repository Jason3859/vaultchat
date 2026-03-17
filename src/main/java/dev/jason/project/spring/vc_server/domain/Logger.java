package dev.jason.project.spring.vc_server.domain;

import java.io.*;
import java.util.Arrays;

public class Logger {

    private static final File logFile = new File("logs.txt");
    private static final PrintStream stream;

    static {
        if (!logFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                logFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            stream = new PrintStream(logFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static File getLogFile() {
        return logFile;
    }

    public static void write(Throwable throwable) {
        StackTraceElement[] stacktrace = throwable.getStackTrace();
        stream.println(throwable.getMessage());
        Arrays.stream(stacktrace).forEach(e -> stream.printf("\t%s\n", e));
        stream.println();
    }
}
