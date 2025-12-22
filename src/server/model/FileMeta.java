package server.model;

import server.config.ServerConfig;

import java.io.File;

public class FileMeta {
    public final String fileId;
    public final String fileName;
    public final String owner;
    public final boolean isPublic;
    public final long originalSize;
    public long receivedSize = 0;

    public FileMeta(String fileId, String fileName,
                    String owner, boolean isPublic, long originalSize) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.owner = owner;
        this.isPublic = isPublic;
        this.originalSize = originalSize;
    }

    public File resolveFile() {
        return new File(ServerConfig.STORAGE_ROOT + "/" + owner + "/" + fileName);
    }
}
