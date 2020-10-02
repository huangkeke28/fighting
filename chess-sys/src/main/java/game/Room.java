package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.annotation.Generated;
import java.util.UUID;


public class Room {
    private String roomId;
    private int playerId1;
    private int playerId2;

    private static final int MAX_ROW = 15;
    private static final int MAX_COL = 15;

    int[][] chessBoard = new int[MAX_ROW][MAX_COL];
    private Gson gson = new GsonBuilder().create();

    public Room() {
        //生成房间ID
        roomId = UUID.randomUUID().toString();
    }

    public String getRoomId() {
        return roomId;
    }

    public int getPlayerId1() {
        return playerId1;
    }

    public void setPlayerId1(int playerId1) {
        this.playerId1 = playerId1;
    }

    public int getPlayerId2() {
        return playerId2;
    }

    public void setPlayerId2(int playerId2) {
        this.playerId2 = playerId2;
    }
}
