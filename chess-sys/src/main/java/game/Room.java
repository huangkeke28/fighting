package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Session;
import java.io.IOException;
import java.util.UUID;


public class Room {
    public static class PutChessResponse {
        public String type = "putChess";
        public int userId;
        public int row;
        public int col;
        public int winner;
    }
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

    public void putChess(GameAPI.Request request) throws IOException {
        int chess = request.userId == playerId1 ? 1 : 2;
        int row = request.row;
        int col = request.col;
        if (chessBoard[row][col] != 0) {
            System.out.println("落子位置有误！" + request);
            return;
        }
        chessBoard[row][col] = chess;
        printChessBoard();
        //检查游戏是否结束
        int winner = checkWinner(row,col,chess);
        //把响应写回客户端
        PutChessResponse response = new PutChessResponse();
        response.userId = request.userId;
        response.row = row;
        response.col = col;
        response.winner = winner;
        OnlineUserManager manager = OnlineUserManager.getInstance();
        Session session1 = manager.getSession(playerId1);
        Session session2 = manager.getSession(playerId2);
        if (session1 == null) {
            response.winner = playerId2;
        } else if (session2 == null) {
            response.winner = playerId1;
        }
        String respJson = gson.toJson(response);
        if (session1 != null) {
            session1.getBasicRemote().sendText(respJson);
        }
        if (session2 != null) {
            session2.getBasicRemote().sendText(respJson);
        }
        if (response.winner != 0) {
            RoomManager.getInstance().removeRoom(roomId);
            System.out.println("游戏结束，房间已经销毁！" + roomId);
        }
    }

    private int checkWinner(int row, int col, int chess) {
        boolean done = false;
        for (int c = col - 4; c <= col; c++) {
            if (c <= 0 || c >= MAX_COL) {
                continue;
            }
            if (chessBoard[row][c] == chess
                    && chessBoard[row][c+1] == chess
                    && chessBoard[row][c+2] == chess
                    && chessBoard[row][c+3] == chess
                    && chessBoard[row][c+4] == chess) {
                    done = true;
            }
        }
        for (int r = row - 4; r <= row; r++) {
            if (r <= 0 || r >= MAX_ROW) {
                continue;
            }
            if (chessBoard[r][col] == chess
                    && chessBoard[r+1][col] == chess
                    && chessBoard[r+2][col] == chess
                    && chessBoard[r+3][col] == chess
                    && chessBoard[r+4][col] == chess
            ) {
                done = true;
            }
        }
        for (int r = row - 4, c = col - 4; r <= row && c <= col; r++,c++) {
            if (r < 0 || r >= MAX_ROW || c < 0 || c >= MAX_COL) {
                continue;
            }
            if (chessBoard[r][c] == chess
                    &&chessBoard[r + 1][c + 1] == chess
                    &&chessBoard[r + 2][c + 2] == chess
                    &&chessBoard[r + 3][c + 3] == chess
                    &&chessBoard[r + 4][c + 4] == chess) {
                done = true;
            }
        }
        for (int r = row - 4, c = col + 4; r <= row && c >= col; r++,c--) {
            if (r < 0 || r >= MAX_ROW || c < 0 || c >= MAX_COL) {
                continue;
            }
            if (chessBoard[r][c] == chess
                    &&chessBoard[r + 1][c - 1] == chess
                    &&chessBoard[r + 2][c - 2] == chess
                    &&chessBoard[r + 3][c - 3] == chess
                    &&chessBoard[r + 4][c - 4] == chess
            ) {
                done = true;
            }
        }
        if (!done) {
            return 0;
        }
        return chess == 1 ? playerId1 : playerId2;
    }

    private void printChessBoard() {
        System.out.println("打印棋盘信息");
        System.out.println("===============================");
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                System.out.print(chessBoard[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("===============================");
    }
}
