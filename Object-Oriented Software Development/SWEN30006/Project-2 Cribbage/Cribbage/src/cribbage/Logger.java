package cribbage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Logger {
    private static final Logger logger = new Logger();
    private final Path OutPut = Path.of("cribbage.log");

    private Logger() {
        // Remove the existing file if need
        try {
            Files.deleteIfExists(OutPut);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static Logger getInstance() { return logger; }
    public void log(String logMessage) {
        String text = logMessage + "\n";
        try {
            Files.write(OutPut, text.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
