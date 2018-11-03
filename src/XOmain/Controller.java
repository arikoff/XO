package XOmain;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import XOsettings.Settings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Controller {

    private GameField gf;
    private GraphicsContext g;
    private char currentPlayer = 'X';
    private int resultX = 0;
    private int resultO = 0;
    private boolean gameStarted = false;
    Settings settings = Settings.getInstance();
    private int maxScore=0;

    private boolean isServer = false;
    private boolean isServerMove = false;
    private boolean isNetGame = false;
    private boolean clientConnected = false;

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8992"));

    @FXML
    private Label labelCurrentPlayer;

    @FXML
    private Label labelScoreboard;

    @FXML
    private Canvas gameCanvas;

    @FXML
    public void buttonNewGame(){
        StartNewGame();
    }

    @FXML
    public void buttonStart() {
        StartNetGame();
    }

    @FXML
    public void buttonJoin() {
        JoinNetGame();
    }

    @FXML
    public void buttonSettings() throws Exception{
        OpenSettings();
    }

    @FXML
    public void mouseClickOnCanvas(MouseEvent t){
        checkMove(t);
    }

    @FXML
    public void mouseMovedCanvas(MouseEvent t) {
        mouseMovedCanvasCheck(t);
    }

    private void mouseMovedCanvasCheck(MouseEvent t) {

        if (!gameStarted) return;

        int x = (int) t.getX();
        int y = (int) t.getY();

        if ((x >= settings.size * 20 - 3) || (y >= settings.size * 20 - 3)) {
            return;
        }

        if (!(gf.LineIsPossible(x, y) == 0)) {
            gameCanvas.setCursor(Cursor.HAND);
        }
        else {
            gameCanvas.setCursor(Cursor.DEFAULT);
        }

    }

    //создает и заполняет игровое поле
    private void StartNewGame(){


        gameStarted = true;

        gf = new GameField(settings.size);
        int s = gf.size;
        g = gameCanvas.getGraphicsContext2D();
        g.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        for (int i=0; i<s; i++){
            for (int j=0; j<s; j++){
                ShowSquare(gf.field[i][j]);
            }
        }

        currentPlayer = 'X';
        resultO = 0;
        resultX = 0;
        int ss = (settings.size - 1) / 2;
        maxScore = ss * ss * 2 + settings.size;
        updateResult();

    }

    private void StartNetGame() {

        isServer = true;
        isNetGame = true;
        isServerMove = true;

        StartNewGame();

//        EventLoopGroup group = new NioEventLoopGroup();
//
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(group)
//                    .channel(NioSocketChannel.class)
//                    .handler(new SecureChatClientInitializer(sslCtx));
//
//            // Start the connection attempt.
//            Channel ch = b.connect(HOST, PORT).sync().channel();
//
//            // Read commands from the stdin.
//            ChannelFuture lastWriteFuture = null;
//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//
//            for (;;) {
//                String line = in.readLine();
//                if (line == null) {
//                    break;
//                }
//
//                // Sends the received line to the server.
//                lastWriteFuture = ch.writeAndFlush(line + "\r\n");
//
//                // If user typed the 'bye' command, wait until the server closes
//                // the connection.
//                if ("bye".equals(line.toLowerCase())) {
//                    ch.closeFuture().sync();
//                    break;
//                }
//            }
//
//            // Wait until all messages are flushed before closing the channel.
//            if (lastWriteFuture != null) {
//                lastWriteFuture.sync();
//            }
//        } finally {
//            // The connection is closed automatically on shutdown.
//            group.shutdownGracefully();
//        }

    }

    private void JoinNetGame() {

        isServer = false;
        isNetGame = true;
        StartNewGame();

    }

    public void checkMove(MouseEvent t){

        if (isNetGame && (isServer != isServerMove)) return;

        //получили координаты точки
        int x = (int) t.getX();
        int y = (int) t.getY();

        //проверяем, возможен ли ход
        int line = gf.LineIsPossible(x, y);
        if (!(line == 0)) {
            //если возможен - делаем и получаем клетки, которые нужно проверить
            ArrayList<Square> squares = gf.MakeMove(x, y, line);
            //для каждой клетки проверям, закрылась ли она
            boolean switchMove = true;
            for (int i=0; i<squares.size(); i++) {
                //если закрылась - рисуем символ и увеличиваем счет
                Square s = squares.get(i);
                if (s.IsComplete()){
                    s.Fill(currentPlayer);
                    if (currentPlayer == 'X') {
                        resultX++;
                    }
                    else {
                        resultO++;
                    }
                    updateResult();
                    switchMove = false;
                }
                ShowSquare(s);
            }
            //если не закрылась - передаем ход
            if (switchMove) {
                switchPlayer();
            }
        }
    }

    private void switchPlayer(){
        if (currentPlayer == 'X') {
            currentPlayer = 'O';
        }
        else {
            currentPlayer = 'X';
        }

        labelCurrentPlayer.setText("" + currentPlayer);

    }

    private void updateResult() {
        labelScoreboard.setText("" + resultX + ":" + resultO);
        if (resultO + resultX == maxScore) {
            if (resultX > resultO) {
                labelCurrentPlayer.setText("X won!");
            }
            else {
                labelCurrentPlayer.setText("O won!");
            }
            gameStarted = false;
            gameCanvas.setCursor(Cursor.DEFAULT);
        }
        else {
            labelCurrentPlayer.setText("" + currentPlayer);
        }
    }

    private void ShowSquare(Square s){

        int i = s.i;
        int j = s.j;

        g = gameCanvas.getGraphicsContext2D();

        g.strokeLine(i*20, j*20, i*20, j*20);
        g.strokeLine((i+1)*20, j*20, (i+1)*20, j*20);
        g.strokeLine(i*20, (j+1)*20, i*20, (j+1)*20);
        g.strokeLine((i+1)*20, (j+1)*20, (i+1)*20, (j+1)*20);

        if (s.left) {
            g.strokeLine(i*20, j*20, i*20, (j+1)*20);
        }

        if (s.right) {
            g.strokeLine((i+1)*20, j*20, (i+1)*20, (j+1)*20);
        }

        if (s.top) {
            g.strokeLine(i*20, j*20, (i+1)*20, j*20);
        }

        if (s.bottom) {
            g.strokeLine(i*20, (j+1)*20, (i+1)*20, (j+1)*20);
        }

        if (s.value == 'X') {
            g.strokeLine(i*20+2, j*20+2, (i+1)*20-2, (j+1)*20-2);
            g.strokeLine((i+1)*20-2, j*20+2, i*20+2, (j+1)*20-2);
        }

        if (s.value == 'O') {
            g.strokeOval(i*20+2, j*20+2, 16, 16);
        }
    }

    private void OpenSettings() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/XOsettings/SettingsDialog.fxml"));
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setTitle("XO game settings");
        newStage.setScene(new Scene(root, 420, 350));
        newStage.show();

    }
}
