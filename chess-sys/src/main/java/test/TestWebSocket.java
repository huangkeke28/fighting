package test;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/webSocketTest/{userId}")
public class TestWebSocket {

    private int userId;
    //在客户端建立连接时调用
    @OnOpen
    public void onOpen(@PathParam("userId") String userIdStr, Session session) {
        System.out.println("建立连接" + userIdStr);
        this.userId = Integer.parseInt(userIdStr);

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("收到消息！Message=" + message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("连接异常：" + userId);
        error.getMessage();
    }

    @OnClose
    public void onClose() {
        System.out.println("断开连接：" + userId);
    }
}
