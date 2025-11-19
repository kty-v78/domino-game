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

        if (domino.getRight() == leftEnd) {
            board.add(0, domino);
            leftEnd = domino.getLeft();
            return true;
        }

        if (domino.getLeft() == leftEnd) {
            Domino flipped = domino.flip();
            board.add(0, flipped);
            leftEnd = flipped.getLeft();
            return true;
        }

        return false;
    }

    public boolean addToRight(Domino domino) {
        if (board.isEmpty()) {
            return addToLeft(domino);
        }

        if (domino.getLeft() == rightEnd) {
            board.add(domino);
            rightEnd = domino.getRight();
            return true;
        }

        if (domino.getRight() == rightEnd) {
            Domino flipped = domino.flip();
            board.add(flipped);
            rightEnd = flipped.getRight();
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
