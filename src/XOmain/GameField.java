package XOmain;

import java.util.ArrayList;

public class GameField {

    int size;
    Square[][] field;

    GameField(int size){

        this.size = size;
        this.field = new Square[size][size];
        int s = size-1;
        int mid = s/2;

        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                if (i==mid && j==0){
                    field[i][j] = new Square(true, true, true, false, i, j);
                }
                else if (i==0 && j==mid){
                    field[i][j] = new Square(true, true, false, true, i, j);
                }
                else if (i==mid && j==s){
                    field[i][j] = new Square(true, false, true, true, i, j);
                }
                else if (i==s && j==mid){
                    field[i][j] = new Square(false, true, true, true, i, j);
                }
                else if (i+j==mid){
                    field[i][j] = new Square(true, true, false, false, i, j);
                }
                else if (i-j==mid){
                    field[i][j] = new Square(false, true, true, false, i, j);
                }
                else if (j-i==mid){
                    field[i][j] = new Square(true, false, false, true, i, j);
                }
                else if (j+i==mid*3){
                    field[i][j] = new Square(false, false, true, true, i, j);
                }
                else{
                    field[i][j] = new Square(false, false, false, false, i, j);
                }
            }
        }

    }

    public boolean checkInside(int i, int j) {

        int size = this.size-1;
        int mid = size/2;

        if (i + j < mid) return false;
        if (i + j > mid*3) return false;
        if ((i < mid) && (j-i > mid)) return false;
        if ((j < mid) && (i-j > mid)) return false;

        return true;
    }

/**
 * Параметры:
 *   x, y - координаты точки (в пикселях)
 * Возвращаемое значение:
 *   0 - линия невозможна (уже есть или за границей поля)
 *   1 - возможна вертикальная линия
 *   2 - возможна горизонтальная линия
 */
    public int LineIsPossible(int x, int y) {

        int dx = (x + 2) % 20;
        int dy = (y + 2) % 20;

        if (dx <= 4 && dy > 4) { //вертикальная линия
            int i = (x+2)/20;
            int j = y / 20;
            Square s = this.field[i][j];
            if (checkInside(i, j) && !s.left) {
                return 1;
            }
        }

        else if (dy <= 4 && dx > 4) { //горизонтальная линия
            int i = x / 20;
            int j = (y+2) / 20;
            Square s = this.field[i][j];
            if (checkInside(i, j) && !s.top){
                return 2;
            }
        }

        return 0;

    }

    public ArrayList<Square> MakeMove(int x, int y, int line){

        int i, j;

        ArrayList<Square> squares = new ArrayList<>();

        //попытку хода считаем в том случае, если точка +- 2 пиксела от потенциальной линии и дальше 2 пикселов от других линий
        if (line == 1) { //вертикальная линия

            i = (x+2)/20;
            j = y / 20;

            Square s = this.field[i][j];
            s.left = true;
            squares.add(s);

            i--;
            s = this.field[i][j];
            s.right = true;
            squares.add(s);

        }

        else { //горизонтальная линия

            i = x / 20;
            j = (y+2) / 20;

            Square s = this.field[i][j];
            s.top = true;
            squares.add(s);

            j--;
            s = this.field[i][j];
            s.bottom = true;
            squares.add(s);

        }
        return squares;
    }
}
