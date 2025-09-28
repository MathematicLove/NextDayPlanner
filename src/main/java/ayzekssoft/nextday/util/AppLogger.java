package ayzekssoft.nextday.util;

import ayzekssoft.nextday.config.ApplicationConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppLogger {
    public static Path logException(String context, Throwable t) {
        try {
            Path dir = ApplicationConfig.getStorageDirectory().resolve("logs");
            Files.createDirectories(dir);
            Path file = dir.resolve("nextday.log");
            StringWriter sw = new StringWriter();
            sw.append("[" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "] ")
              .append(context == null ? "" : context).append('\n');
            t.printStackTrace(new PrintWriter(sw));
            Files.writeString(file, sw.toString() + System.lineSeparator(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
            return file;
        } catch (IOException e) {
            return null;
        }
    }
}


