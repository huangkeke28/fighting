package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

//处理websocket相关通信逻辑
@ServerEndpoint("/game/{userId}")
public class GameAPI {
    static class Request {
        public String type;
        public int userId;
        public String roomId;
        public int row;
        public int col;
    }
    private int userId;
    @OnOpen
    public void onOpen(@PathParam("userId") String userIdStr, Session session) {
        userId = Integer.parseInt(userIdStr);
        System.out.println("玩家建立连接: " + userId);
        //把玩家加入到在线列表中
        OnlineUserManager.getInstance().online(userId,session);
    }

    @OnClose
    public void onClose() {
        System.out.println("玩家断开连接: " + userId);

        //把玩家从在线列表中剔除
        OnlineUserManager.getInstance().offline(userId);

    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("玩家断开连接: " + userId);
        OnlineUserManager.getInstance().offline(userId);
        error.printStackTrace();

    }


    @OnMessage
    public void onMessage(String message, Session session) throws InterruptedException {
        System.out.printf("收到玩家 %d 的消息: %s\n",userId,message);
        //使用Gson解析数据
        //实例化Gson对象
        Gson gson = new GsonBuilder().create();
        Request request = gson.fromJson(message,Request.class);
        //处理请求的逻辑
        if (request.type.equals("startMatch")) {
            //搞一个匹配队列
            Matcher.getInstance().addMatchqueue(request);
        } else if (request.type.equals("putChess")) {

        } else {
            System.out.println("非法的type值！" + request.type);
        }

    }
}
