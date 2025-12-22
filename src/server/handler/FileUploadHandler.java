package server.handler;

import server.manager.BufferManager;
import server.model.FileMeta;
import server.service.HistoryLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FileUploadHandler {
    private static final Map<String, FileMeta> activeUploads = new ConcurrentHashMap<>();

    public static FileMeta initiate(String user, String fileName, long size, boolean isPublic) {
        if (!BufferManager.canAccept(size)) return null;

        BufferManager.reserve(size);

        String fileId = UUID.randomUUID().toString();
        FileMeta meta = new FileMeta(fileId, fileName, user, isPublic, size);
        activeUploads.put(fileId, meta);

        return meta;
    }

    public static void receiveChunk(String fileId, byte[] data) throws Exception {
        FileMeta meta = activeUploads.get(fileId);
        if (meta == null) throw new IllegalStateException("Invalid fileId");

        File file = meta.resolveFile();
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(data);
        }
        meta.receivedSize += data.length;
    }

    public static boolean complete(String fileId) {
        FileMeta meta = activeUploads.remove(fileId);
        if (meta == null) return false;

        BufferManager.release(meta.originalSize);
        boolean ok = meta.receivedSize == meta.originalSize;

        HistoryLogger.log(meta.owner, meta.fileName, "UPLOAD", ok);
        if (!ok) meta.resolveFile().delete();

        return ok;
    }

    public static void discard(String fileId) {
        FileMeta meta = activeUploads.remove(fileId);
        if (meta != null) BufferManager.release(meta.originalSize);
    }
}
