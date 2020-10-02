package game;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineUserManager {
    //使用一个hash表来保存用户在线信息
    //使用ConcurrentHashMap保证线程安全
    ConcurrentHashMap<Integer, Session> users = new ConcurrentHashMap<>();

    public void online(int userId, Session session) {
        users.put(userId,session);
    }

    public void offline (int userId) {
        users.remove(userId);
    }

    public Session getSession (int userId) {
        return users.get(userId);
    }
    private static volatile OnlineUserManager instance = null;

    public static OnlineUserManager getInstance() {
        if (instance == null) {
            synchronized (OnlineUserManager.class) {
                if (instance == null) {
                    instance = new OnlineUserManager();
                }
            }
        }
        return instance;
    }

    private OnlineUserManager(){}
}
