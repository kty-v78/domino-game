package domino;

import java.util.ArrayList;
import java.util.List;

public class DominoBoard {
    private List<Domino> board;
    private int leftEnd;
    private int rightEnd;

    public DominoBoard() {
        board = new ArrayList<>();
        leftEnd = -1;
        rightEnd = -1;
    }

    public boolean addToLeft(Domino domino) {
        if (board.isEmpty()) {
            board.add(domino);
            leftEnd = domino.getLeft();
            rightEnd = domino.getRight();
            return true;
        }

        if(domino.matches(leftEnd)) {
            Domino toAdd = domino;
            if (domino.getRight() == leftEnd) {
                toAdd = domino.flip();
            }
            board.add(0, toAdd);
            leftEnd = toAdd.getLeft();
            return true;
        }
        return false;
    }

    public boolean addToRight(Domino domino) {
        if (board.isEmpty()) {
            return addToLeft(domino);
        }
        if (domino.matches(rightEnd)) {
            Domino toAdd = domino;
            if (domino.getLeft() == rightEnd) {
                toAdd = domino.flip();
            }
            board.add(toAdd);
            rightEnd = toAdd.getRight();
            return true;
        }
        return false;
    }
    public boolean playDomino(Domino domino) {
        return addToLeft(domino) || addToRight(domino);
    }

    public int getLeftEnd() { return leftEnd; }
    public int getRightEnd() { return rightEnd; }
    public boolean isEmpty() { return board.isEmpty(); }
    public List<Domino> getBoard() { return new ArrayList<>(board); }

    @Override
    public String toString() {
        if (board.isEmpty()) {
            return "Доска пуста";
        }

        StringBuilder sb = new StringBuilder();
        for (Domino domino : board) {
            sb.append(domino.toString()).append(" ");
        }
        return sb.toString().trim();
    }
}
