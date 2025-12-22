package server.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryManager {
    private static final String ROOT_DIR = "server_storage";

    static {
        // Create root directory once
        File root = new File(ROOT_DIR);
        if (!root.exists()) {
            root.mkdirs();
        }
    }

    private DirectoryManager() {
        // Utility class — prevent instantiation
    }

    /**
     * Creates a directory for a user if it does not already exist.
     */
    public static void create(String username) {
        Path userDir = Paths.get(ROOT_DIR, username);

        if (!Files.exists(userDir)) {
            try {
                Files.createDirectories(userDir);
                Files.createDirectories(userDir.resolve("public"));
                Files.createDirectories(userDir.resolve("private"));
            } catch (IOException e) {
                throw new RuntimeException(
                        "Failed to create directory for user: " + username, e
                );
            }
        }
    }

    /**
     * Returns user's root directory.
     */
    public static Path getUserDir(String username) {
        return Paths.get(ROOT_DIR, username);
    }

    public static Path getPublicDir(String username) {
        return getUserDir(username).resolve("public");
    }

    public static Path getPrivateDir(String username) {
        return getUserDir(username).resolve("private");
    }

}
