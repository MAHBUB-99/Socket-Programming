package server.manager;

import server.model.ClientSession;

import java.net.Socket;
import java.util.*;

public class SessionManager {
    private static final Map<String, ClientSession> online =
            Collections.synchronizedMap(new HashMap<>());
    private static final Set<String> allUsers =
            Collections.synchronizedSet(new HashSet<>());

    public static synchronized boolean login(String user, Socket socket) {
        if (online.containsKey(user)) return false;

        try {
            ClientSession session = new ClientSession(user, socket);
            online.put(user, session);
            allUsers.add(user);
            DirectoryManager.create(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static synchronized void logout(String user) {
        ClientSession session = online.remove(user);
        if (session != null) {
            session.disconnect();
        }
    }

    public static Map<String, Boolean> listUsers() {
        Map<String, Boolean> map = new HashMap<>();
        for (String u : allUsers)
            map.put(u, online.containsKey(u));
        return map;
    }
}
