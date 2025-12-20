package server.protocol;

import java.io.Serializable;

public enum CommandType implements Serializable {
    LIST_USERS,
    LIST_FILES,
    UPLOAD,
    DOWNLOAD,
    REQUEST,
    HISTORY,
    LOGOUT
}
