package game;

import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private ConcurrentHashMap<String,Room> rooms = new ConcurrentHashMap<>();

    private static volatile RoomManager instance;
    private RoomManager(){}
    public static RoomManager getInstance() {
        if (instance == null) {
            synchronized (RoomManager.class) {
                if (instance == null) {
                    instance = new RoomManager();
                }
            }
        }
        return instance;
    }

    public void addRoom(Room room) {
        rooms.put(room.getRoomId(),room);
    }

    public void removeRoom(String roomId) {
        rooms.remove(roomId);
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }
}
