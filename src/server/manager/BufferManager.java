package server.manager;

import server.config.ServerConfig;

import java.util.Random;

public class BufferManager {
    private static long used = 0;

    public synchronized static boolean canAccept(long size) {
        return used + size <= ServerConfig.MAX_BUFFER_SIZE;
    }

    public synchronized static void reserve(long size) {
        used += size;
    }

    public synchronized static void release(long size) {
        used -= size;
    }

    public static int randomChunkSize() {
        return new Random().nextInt(
                ServerConfig.MAX_CHUNK_SIZE - ServerConfig.MIN_CHUNK_SIZE
        ) + ServerConfig.MIN_CHUNK_SIZE;
    }
}
