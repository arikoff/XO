package XOmain;

import java.io.Serializable;

public class Square implements Serializable {

    boolean left;
    boolean right;
    boolean top;
    boolean bottom;
    int i;
    int j;

    char value;

    Square(boolean left, boolean top, boolean right, boolean bottom, int i, int j){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.i = i;
        this.j = j;
    }

    public boolean IsComplete(){
        return(this.left && this.bottom && this.right && this.top);
    }

    public void Fill(char value){
        this.value = value;
    }
}
