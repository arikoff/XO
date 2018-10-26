package XOmain;

public class Square {

    boolean left;
    boolean right;
    boolean top;
    boolean bottom;
    char value;

    Square(boolean left, boolean top, boolean right, boolean bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean checkfill(char value){
        if(this.left && this.bottom && this.right && this.top){
            this.value = value;
            return true;
        }
        return false;
    }
}
