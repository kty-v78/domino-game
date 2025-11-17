package domino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DominoSet {
    private List<Domino> dominoes;

    public DominoSet(List<Domino> dominoes) {
        dominoes = new ArrayList<>();
        generate();
    }

    private void generate() {
        for (int i = 0; i < 7; i++) {
            for (int j = i; j < 7; j++) {
                dominoes.add(new Domino(i, j));
            }

        }
    }

    //перемешать наюор костяшек
    public void shuffle(){
        Collections.shuffle(dominoes);
    }

    public Domino draw() {
        if(dominoes.isEmpty()) {
            return null;
        }
        return dominoes.remove(dominoes.size() - 1);
    }

    public  List<Domino> draw(int count) {
        List<Domino> drawn = new ArrayList<>();
        for (int i = 0; i <= count && !dominoes.isEmpty(); i++) {
            drawn.add(draw());
        }
        return drawn;
    }
}
