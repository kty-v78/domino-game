package domino;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Консольная демонстрация игры в домино.
 */
public class Console {
    private DominoGame game;
    private Scanner scanner;

    /**
     * Конструктор консольной демонстрации.
     * Инициализирует сканер для ввода пользователя.
     */
    public Console() {
        scanner = new Scanner(System.in);
    }

    /**
     * Запускает консольную демонстрацию игры.
     * Управляет основным игровым циклом и отображением состояния.
     */
    public void start() {
        System.out.println("=== ДОМИНО ===");

        List<String> playerNames = Arrays.asList("Игрок 1", "Игрок 2");
        game = new DominoGame(playerNames);
        game.startGame();

        System.out.println("\nНачальное состояние игры:");
        displayGameState();

        while (!game.isGameOver()) {
            playTurn();
            if (!game.isGameOver()) {
                displayGameState();
            }
        }

        displayWinner();
        scanner.close();
    }
    /**
     * Отображает текущее состояние игры.
     */
    private void displayGameState() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Доска: " + game.getBoard());
        System.out.println("Базар: " + game.getRemainingDominoes() + " костяшек");
        System.out.println();

        for (Player player : game.getPlayers()) {
            String marker = player.equals(game.getCurrentPlayer()) ? " -> " : "    ";
            System.out.println(marker + player);
        }
    }
    private void playTurn() {
        Player currentPlayer = game.getCurrentPlayer();
        System.out.println("\nХод " + currentPlayer.getName());

        boolean hasPlayableDomino = currentPlayer.hasPlayerDomino(
                game.getBoard().getLeftEnd(),
                game.getBoard().getRightEnd()
        );

        if (!hasPlayableDomino && game.getRemainingDominoes() == 0) {
            System.out.println("Нет подходящих костяшек и базар пуст. Пропускаем ход.");
            game.makeMove(null);
            return;
        }

        // Показываем возможные ходы
        List<Domino> hand = currentPlayer.getHand();
        System.out.println("Ваши костяшки: ");
        for (int i = 0; i < hand.size(); i++) {
            Domino domino = hand.get(i);
            boolean canPlay = domino.matches(game.getBoard().getLeftEnd()) ||
                    domino.matches(game.getBoard().getRightEnd());
            String playable = canPlay ? " +" : " -";
            System.out.println((i + 1) + ". " + domino + playable);
        }

        if (game.getRemainingDominoes() > 0) {
            System.out.println("0. Взять из базара");
        } else {
            System.out.println("0. Пропустить ход (базар пуст)");
        }

        int choice = getPlayerChoice(hand.size());

        Domino selectedDomino = (choice == 0) ? null : hand.get(choice - 1);
        game.makeMove(selectedDomino);
    }
    /**
     * Получает выбор игрока с валидацией ввода.
     *
     * @param maxChoice максимально допустимый выбор
     * @return выбранный номер костяшки
     */
    private int getPlayerChoice(int maxChoice) {
        int choice = -1;
        while (choice < 0 || choice > maxChoice) {
            System.out.print("Ваш выбор (0-" + maxChoice + "): ");
            try {
                choice = scanner.nextInt();
                if (choice < 0 || choice > maxChoice) {
                    System.out.println("Пожалуйста, введите число от 0 до " + maxChoice);
                }
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Пожалуйста, введите число.");
            }
        }
        return choice;
    }

    /**
     * Отображает победителя и итоговую статистику.
     */
    private void displayWinner() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ИГРА ОКОНЧЕНА!");

        Player winner = game.getWinner();
        if (winner != null) {
            System.out.println(" Победитель: " + winner.getName() + "!");
        } else {
            System.out.println("Ничья!");
        }

        System.out.println("\nИтог:");
        for (Player player : game.getPlayers()) {
            System.out.println(player.getName() + ": " + player.getHand() +
                    " (очки: " + player.getHandValue() + ")");
        }
    }

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Console demo = new Console();
        demo.start();
    }
}