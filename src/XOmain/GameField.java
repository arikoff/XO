package XOmain;

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
                    field[i][j] = new Square(true, true, true, false);
                }
                else if (i==0 && j==mid){
                    field[i][j] = new Square(true, true, false, true);
                }
                else if (i==mid && j==s){
                    field[i][j] = new Square(true, false, true, true);
                }
                else if (i==s && j==mid){
                    field[i][j] = new Square(false, true, true, true);
                }
                else if (i+j==mid){
                    field[i][j] = new Square(true, true, false, false);
                }
                else if (i-j==mid){
                    field[i][j] = new Square(false, true, true, false);
                }
                else if (j-i==mid){
                    field[i][j] = new Square(true, false, false, true);
                }
                else if (j+i==mid*3){
                    field[i][j] = new Square(false, false, true, true);
                }
                else{
                    field[i][j] = new Square(false, false, false, false);
                }
            }
        }

    }
}