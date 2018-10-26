package XOmain;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import XOsettings.Settings;

public class Controller {

    private GameField gf;
    private GraphicsContext g;
    private char currentPlayer = 'X';
    private int resultX = 0;
    private int resultO = 0;
    private boolean gameStarted = false;
    Settings settings = Settings.getInstance();
    private int maxScore=0;

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

        if (LineIsPossible(x, y)) {
            gameCanvas.setCursor(Cursor.HAND);
        }
        else {
            gameCanvas.setCursor(Cursor.DEFAULT);
        }

    }

    private boolean LineIsPossible(int x, int y) {

        int dx = (x + 2) % 20;
        int dy = (y + 2) % 20;

        if (dx <= 4 && dy > 4) { //вертикальная линия
            int i = (x+2)/20;
            int j = y / 20;
            Square s = gf.field[i][j];
            return (checkInside(i, j) && !s.left);
        }

        else if (dy <= 4 && dx > 4) { //горизонтальная линия
            int i = x / 20;
            int j = (y+2) / 20;
            Square s = gf.field[i][j];
            return (checkInside(i, j) && !s.top);
        }

        else {
            return false;
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
                ShowSquare(i, j , gf.field[i][j]);
            }
        }

        currentPlayer = 'X';
        resultO = 0;
        resultX = 0;
        int ss = (settings.size - 1) / 2;
        maxScore = ss * ss * 2 + settings.size;
        updateResult();

    }

    public void checkMove(MouseEvent t){
        //получили координаты точки
        int x = (int) t.getX();
        int y = (int) t.getY();

        int i, j;
        boolean d1, d2;

        //попытку хода считаем в том случае, если точка +- 2 пиксела от потенциальной линии и дальше 2 пикселов от других линий
        int dx = (x+2)%20;
        int dy = (y+2)%20;

        if (dx <= 4 && dy > 4) { //вертикальная линия

            i = (x+2)/20;
            j = y / 20;

            if (!LineIsPossible(x, y)) return;

            Square s = gf.field[i][j];
            s.left = true;
            d1 = makeMove(i, j, s);

            i--;
            s = gf.field[i][j];
            s.right = true;
            d2 = makeMove(i, j, s);

            if (!(d1 || d2)) {
                switchPlayer();
            }
        }

        if (dy <= 4 && dx > 4) { //горизонтальная линия

            i = x / 20;
            j = (y+2) / 20;

            if (!LineIsPossible(x, y)) return;
            Square s = gf.field[i][j];

            s.top = true;
            d1 = makeMove(i, j, s);

            j--;
            s = gf.field[i][j];
            s.bottom = true;
            d2 = makeMove(i, j, s);

            if (!(d1 || d2)) {
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

    private boolean checkInside(int i, int j) {

        int size = gf.size-1;
        int mid = size/2;

        if (i + j < mid) return false;
        if (i + j > mid*3) return false;
        if ((i < mid) && (j-i > mid)) return false;
        if ((j < mid) && (i-j > mid)) return false;

        return true;
    }

    private boolean makeMove(int i, int j, Square s) {

        boolean done = s.checkfill(currentPlayer);
        ShowSquare(i, j, s);
        if(done){
            if (currentPlayer == 'X') {
                resultX++;
            }
            else {
                resultO++;
            }
            updateResult();
        }
        return done;
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

    private void ShowSquare(int i, int j, Square s){

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
