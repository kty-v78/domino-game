package domino;
//костяшка домино 2 значения на одной от 0 до 6
public class Domino {
    private int left;
    private int right;

    public Domino(int left, int right) {
        if(left < 0 || left > 6 || right < 0 || right > 6) {
            throw new IllegalArgumentException("Значения домино должны быть от 0 до 6");
        }
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public boolean isDouble() {
        return left == right;
    }

    public int getValue(){
        return left + right;
    }

    //перевернуть костяшку, поменять местами части
    public Domino flip() {
        return new Domino(right, left);
    }

    //проверка значения
    public boolean matches(int value) {
        return left == value || right == value;
    }

    @Override
    public String toString() {
        return "[" + left + "|" + right + "]";
    }
}
