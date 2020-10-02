package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Matcher {
    private Gson gson = new GsonBuilder().create();
    static class MatchResponse {
        public String type;
        public String roomId;
        public boolean isWhite;
        public int otherUserId;
    }
    //实现一个匹配队列
    private BlockingQueue<GameAPI.Request> queue = new LinkedBlockingQueue<>();
    //实现插入到阻塞队列中的方法
    public void addMatchqueue(GameAPI.Request request) throws InterruptedException {
        queue.put(request);
    }

    //创建一个扫描线程，尝试进行匹配功能
    //构造实例的时候创建一个线程
    private Matcher(){
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    //这个方法完成一次匹配过程
                    handlerMatch();
                }
            }
        };
        t.start();
    }
    private void handlerMatch() {
        //实现一次匹配的过程
        try {
            GameAPI.Request player1 = queue.take();
            GameAPI.Request player2 = queue.take();
            System.out.println("匹配到两个玩家：" + player1.userId + "," + player2.userId);
            OnlineUserManager manager = OnlineUserManager.getInstance();
            Session session1 = manager.getSession(player1.userId);
            Session session2 = manager.getSession(player2.userId);
            if (session1 == null) {
                queue.put(player2);
                System.out.println("玩家1不在线");
                return;
            }
            if (session2 == null) {
                queue.put(player1);
                System.out.println("玩家2不在线");
                return;
            }
            if (session1 == session2) {
                queue.put(player1);
                System.out.println("自己匹配到自己");
                return;
            }
            //把两个玩家放到同一个房间中
            Room room = new Room();
            room.setPlayerId1(player1.userId);
            room.setPlayerId2(player2.userId);
            //引入一个房间管理器对象 组织房间
            RoomManager.getInstance().addRoom(room);
            System.out.println("玩家进入房间成功！" + room.getRoomId());
            //返回数据
            MatchResponse response1 = new MatchResponse();
            response1.isWhite = true;
            response1.type = "startMatch";
            response1.roomId = room.getRoomId();
            response1.otherUserId = player2.userId;
            String respJson1 = gson.toJson(response1);
            session1.getBasicRemote().sendText(respJson1);
            System.out.println("给玩家1响应！" + respJson1);
            MatchResponse response2 = new MatchResponse();
            response2.type = "startMatch";
            response2.isWhite = false;
            response2.roomId = room.getRoomId();
            response2.otherUserId = player2.userId;
            String respJson2 = gson.toJson(response2);
            session2.getBasicRemote().sendText(respJson2);
            System.out.println("给玩家2响应！" + respJson2);

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static volatile Matcher instance;
    public static Matcher getInstance() {
        if (instance == null) {
            synchronized(Matcher.class) {
                if (instance == null) {
                    instance = new Matcher();
                }
            }
        }
        return instance;
    }
}
