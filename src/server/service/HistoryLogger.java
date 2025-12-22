package server.service;

import server.config.ServerConfig;

import java.io.FileWriter;
import java.time.LocalDateTime;

public class HistoryLogger {
    public static synchronized void log(
            String user, String file, String action, boolean success) {

        String line = String.format(
                "%s | %s | %s | %s%n",
                LocalDateTime.now(), file, action,
                success ? "SUCCESS" : "FAILED"
        );

        try (FileWriter fw = new FileWriter(
                ServerConfig.STORAGE_ROOT + "/" + user + "/history.log", true)) {
            fw.write(line);
        } catch (Exception ignored) {}
    }
}
