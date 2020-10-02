// 几个重要参数的解释:
// userId: 用户登陆时获取到的. (测试阶段可以先写死)
// roomId: 当前这局游戏的房间号. 通过匹配结果获取到
// isWhite: 当前这局游戏是否是白子. 通过匹配结果获取到
// 这三个属性包裹到一个 gameInfo 对象中

// 这个数字应该是登陆后从服务器获取的, 目前在页面写死

gameInfo = {
    userId: myUserId,
    roomId: null,
    isWhite: true,
}

//////////////////////////////////////////////////
// 设定界面显示相关操作
//////////////////////////////////////////////////
function onClick(userId) {
    startMatch(userId);
    // 将按钮设置为不可点击, 并修改文本
    $("#matchButton").attr('disabled', true);
    $("#matchButton").text("匹配中...");
}

function hideMatchButton() {
    $("#matchButton").hide();
}

function setScreenText(me) {
    if (me) {
        $("#screen").text("轮到你落子了!")
    } else {
        $("#screen").text("轮到对方落子了!")
    }
}

//////////////////////////////////////////////////
// 初始化 websocket
//////////////////////////////////////////////////
var webSocket = new WebSocket("ws://localhost:8080/chess/game/"+gameInfo.userId);
webSocket.onopen = function () {
    console.log("连接建立成功！" + gameInfo.userId);
}
webSocket.onclose = function () {
    console.log("连接断开！" + gameInfo.userId);
}
webSocket.onerror = function () {
    console.log("连接异常！" +gameInfo.userId);
}
webSocket.onmessage = function() {
    //TODO
    //处理服务器返回的响应
}
window.onbeforeunload = function() {
    webSocket.onclose();
}
//////////////////////////////////////////////////
// 实现匹配逻辑
//////////////////////////////////////////////////
function startMatch(userId) {
    //1.设置处理响应的函数
    webSocket.onmessage = handlerStartMatch;
    //2.构造要发送的数据内容
    var message = {
        type: "startMatch",
        userId: userId
    }
    //3.发送数据给服务器
    webSocket.send(JSON.stringify(message));

}

function handlerStartMatch(event) {
    //1.读取响应内容
    var response = JSON.parse(event.data) // 得到服务器真是的返回数据
    console.log("handlerStartMatch: " + response);
    //2.过滤非法的相应内容
    if (response.type != "startMatch") {
        //服务器响应的数据不对，直接忽略错误数据
        return;
    }
    //3.把相应的结果保存到gameInFo对象中
    gameInfo.roomId = response.roomId;
    gameInfo.isWhite = response.isWhite;
    gameInfo.otherUserId = response.otherUserId;
    //4.隐藏匹配按钮
    hideMatchButton();
    //5.设置提示信息(轮到谁落子)
    //参数为true：表示自己落子，参数为false表示对方落子
    // 白棋先走
    setScreenText(gameInfo.isWhite);
    //6.初始化一局游戏
    initGame();
}
/////////////////////////////////////////////////
// 匹配成功, 则初始化一局游戏
//////////////////////////////////////////////////
function initGame() {
    // 是我下还是对方下. 根据服务器分配的先后手情况决定
    var me = gameInfo.isWhite;
    // 游戏是否结束
    var over = false;
    var chessBoard = [];
    //初始化chessBord数组(表示棋盘的数组)
    for (var i = 0; i < 15; i++) {
        chessBoard[i] = [];
        for (var j = 0; j < 15; j++) {
            chessBoard[i][j] = 0;
        }
    }
    var chess = document.getElementById('chess');
    var context = chess.getContext('2d');
    context.strokeStyle = "#BFBFBF";
    // 背景图片
    var logo = new Image();
    logo.src = "images/sky.jpeg";
    logo.onload = function () {
        context.drawImage(logo, 0, 0, 450, 450);
        initChessBoard();
    }

    // 绘制棋盘网格
    function initChessBoard() {
        for (var i = 0; i < 15; i++) {
            context.moveTo(15 + i * 30, 15);
            context.lineTo(15 + i * 30, 435);
            context.stroke();
            context.moveTo(15, 15 + i * 30);
            context.lineTo(435, 15 + i * 30);
            context.stroke();
        }
    }

    // 绘制一个棋子, me 为 true
    function oneStep(i, j, isWhite) {
        context.beginPath();
        context.arc(15 + i * 30, 15 + j * 30, 13, 0, 2 * Math.PI);
        context.closePath();
        var gradient = context.createRadialGradient(15 + i * 30 + 2, 15 + j * 30 - 2, 13, 15 + i * 30 + 2, 15 + j * 30 - 2, 0);
        if (!isWhite) {
            gradient.addColorStop(0, "#0A0A0A");
            gradient.addColorStop(1, "#636766");
        } else {
            gradient.addColorStop(0, "#D1D1D1");
            gradient.addColorStop(1, "#F9F9F9");
        }
        context.fillStyle = gradient;
        context.fill();
    }

    chess.onclick = function (e) {
        if (over) {
            return;
        }
        if (!me) {
            return;
        }
        var x = e.offsetX;
        var y = e.offsetY;
        // 注意, 横坐标是列, 纵坐标是行
        var col = Math.floor(x / 30);
        var row = Math.floor(y / 30);
        if (chessBoard[row][col] == 0) {
            // 新增发送数据给服务器的逻辑
            send(row,col);
            // oneStep(col, row, gameInfo.isWhite);
            // chessBoard[row][col] = 1;
            // 通过这个语句控制落子轮次
            // me = !me;
        }
    }

    function send(row, col) {
        console.log("send: " + row + "," + col);
        var request = {
            type: "putChess",
            userId: gameInfo.userId,
            roomId: gameInfo.roomI,
            row: row,
            col: col
        }
        webSocket.send(JSON.stringify(request));
    }


    // 新增处理服务器返回数据的请求
    //      并绘制棋子, 以及判定胜负
    function handlerPutChess(event) {
        console.log("handlerPutChess: " + event.data);
        var response = JSON.parse(event.data);
        if (response.type != "putChess") {
            console.log("响应不匹配");
            return;
        }
        if (response.userId == gameInfo.userId) {
            oneStep(response.row,response.col, gameInfo.isWhite);
        } else {
            oneStep(response.row,response.col, !gameInfo.isWhite);
        }
        chessBoard[response.row][response.col] = 1;
        me = !me;
        if (response.winner != 0) {
            if (response.winner == gameInfo.userId) {
                alert("您赢了");
            } else {
                alert("您输了");
            }
            window.location.reload();
        }
        setScreenText(me);
    }

    webSocket.onmessage = handlerPutChess;
}


