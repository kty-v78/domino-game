package domino;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Domino> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Domino> getHand() {
        return new ArrayList<>(hand);
    }

    public void addDomino(Domino domino) {
        hand.add(domino);
    }

    public boolean removeDomino(Domino domino) {
        return  hand.remove(domino);
    }

    public boolean hasPlayerDomino(int leftValue, int rightValue) {
        return hand.stream().anyMatch(d -> d.matches(leftValue) || d.matches(rightValue));
    }

    public Domino findPlayableDomino(int leftValue, int rightValue) {
        return hand.stream()
                .filter(d -> d.matches(leftValue) || d.matches(rightValue))
                .findFirst()
                .orElse(null);
    }

    public int getHandValue() {
        return hand.stream().mapToInt(Domino::getValue).sum();
    }

    public boolean hasDominoes() {
        return !hand.isEmpty();
    }

    public int getHandSize() {
        return hand.size();
    }

    @Override
    public String toString() {
        return name + " (" + hand.size() + " костяшек): " + hand;
    }
}