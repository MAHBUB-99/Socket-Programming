package server.protocol;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    public final boolean success;
    public final String message;

    private ServerResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ServerResponse success(String msg) {
        return new ServerResponse(true, msg);
    }

    public static ServerResponse error(String msg) {
        return new ServerResponse(false, msg);
    }
}
