package test;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value="/webSocketTest/{userId}")
public class TestWebSocket {

    private int userId;
    //在客户端建立连接时调用
    @OnOpen
    public void onOpen(@PathParam("userId") String userIdStr, Session session) {
        System.out.println("建立连接" + userIdStr);
        this.userId = Integer.parseInt(userIdStr);

        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    String resp = "" + System.currentTimeMillis();
                    try {
                        session.getBasicRemote().sendText(resp);
                        Thread.sleep(1000);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("收到消息！Message=" + message);
        //服务器给客户端返回一个消息
        //返回一个当前的时间戳
        String resp = "" + System.currentTimeMillis();
        session.getBasicRemote().sendText(resp);
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
