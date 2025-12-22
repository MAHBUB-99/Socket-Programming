package server.handler;

import server.config.ServerConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class FileDownloadHandler {
    public static void send(File file, OutputStream out) throws Exception {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[ServerConfig.MAX_CHUNK_SIZE];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
        }
    }
}
