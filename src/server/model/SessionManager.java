package server.model;

import java.net.Socket;
import java.util.*;

public class SessionManager {
    private static final Map<String, ClientSession> online =
            Collections.synchronizedMap(new HashMap<>());
    private static final Set<String> allUsers =
            Collections.synchronizedSet(new HashSet<>());

    public static synchronized boolean login(String user, Socket socket) {
        if (online.containsKey(user)) return false;
        online.put(user, new ClientSession(user, socket));
        allUsers.add(user);
        DirectoryManager.create(user);
        return true;
    }

    public static synchronized void logout(String user) {
        online.remove(user);
    }

    public static Map<String, Boolean> listUsers() {
        Map<String, Boolean> map = new HashMap<>();
        for (String u : allUsers)
            map.put(u, online.containsKey(u));
        return map;
    }
}
