package server.config;

public final class ServerConfig {
    private ServerConfig(){

    }
    public static final int SERVER_PORT = 5000;

    public static final long MAX_BUFFER_SIZE = 500L * 1024 * 1024;
    public static final int MIN_CHUNK_SIZE = 32 * 1024;
    public static final int MAX_CHUNK_SIZE = 128 * 1024;

    public static final String STORAGE_ROOT = "server_storage";
}
