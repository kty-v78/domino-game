package domino.gui;

import domino.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DominoGui {
    private DominoGame game;
    private JFrame frame;
    private JLabel statusLabel;
    private JPanel boardPanel;
    private JPanel playerHandPanel;
    private JTextArea gameInfoArea;
    private JButton[] dominoButtons;
    private Player currentPlayer;

    // Цвета для оформления
    private static final Color DOMINO_BG = new Color(255, 250, 240);
    private static final Color DOMINO_DOT = new Color(40, 40, 40);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color BOARD_COLOR = new Color(240, 248, 255);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DominoGui().createAndShowGUI());
    }

    private void createAndShowGUI() {
        // 1. Сначала создать интерфейс
        createGUIComponents();

        // 2. Затем инициализируем игру
        initializeNewGame();

        // 3. Показывать окно
        frame.setVisible(true);
    }

    private void createGUIComponents() {
        // Создаем главное окно
        frame = new JFrame("ДОМИНО");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Верхняя панель - заголовок и статус
        JPanel topPanel = createTopPanel();

        // Центральная панель - игровая доска
        boardPanel = createBoardPanel();
        JScrollPane boardScroll = new JScrollPane(boardPanel);
        boardScroll.setBorder(BorderFactory.createTitledBorder("Игровая доска"));
        boardScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        boardScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Панель руки игрока (с костяшками)
        playerHandPanel = createPlayerHandPanel();
        JScrollPane handScroll = new JScrollPane(playerHandPanel);
        handScroll.setBorder(BorderFactory.createTitledBorder("Ваши костяшки"));

        // Панель информации
        gameInfoArea = new JTextArea(8, 30);
        gameInfoArea.setEditable(false);
        gameInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane infoScroll = new JScrollPane(gameInfoArea);
        infoScroll.setBorder(BorderFactory.createTitledBorder("Информация об игре"));

        // Панель управления
        JPanel controlPanel = createControlPanel();

        // Компоновка окна
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(boardScroll, BorderLayout.CENTER);
        centerPanel.add(infoScroll, BorderLayout.EAST);

        frame.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.add(handScroll, BorderLayout.CENTER);
        southPanel.add(controlPanel, BorderLayout.SOUTH);

        frame.add(southPanel, BorderLayout.SOUTH);

        // Настройки окна
        frame.pack();
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("ДОМИНО", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(25, 25, 112));

        statusLabel = new JLabel("Нажмите 'Новая игра' чтобы начать", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(new Color(0, 100, 0));

        panel.add(title, BorderLayout.NORTH);
        panel.add(statusLabel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return panel;
    }

    private JPanel createBoardPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(BOARD_COLOR);
        panel.setPreferredSize(new Dimension(1250, 200));

        JLabel placeholder = new JLabel("Доска пуста. Начните новую игру!");
        placeholder.setFont(new Font("Arial", Font.ITALIC, 14));
        panel.add(placeholder);

        return panel;
    }

    private JPanel createPlayerHandPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(new Color(245, 245, 245));

        JLabel placeholder = new JLabel("Нет активной игры");
        placeholder.setFont(new Font("Arial", Font.ITALIC, 14));
        panel.add(placeholder);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 0));

        JButton playBtn = createControlButton("Сыграть", e -> playSelectedDomino());
        JButton takeFromBankBtn = createControlButton("Взять", e -> takeFromBank());
        JButton passBtn = createControlButton("Пас", e -> passTurn());
        JButton newGameBtn = createControlButton("Новая", e -> initializeNewGame());
        JButton rulesBtn = createControlButton("Правила", e -> showRules());

        panel.add(playBtn);
        panel.add(takeFromBankBtn);
        panel.add(passBtn);
        panel.add(newGameBtn);
        panel.add(rulesBtn);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JButton createControlButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.addActionListener(action);
        return button;
    }
    private void initializeNewGame() {
        // Всегда 2 игрока
        List<String> playerNames = Arrays.asList("Игрок 1", "Игрок 2");

        try {
            game = new DominoGame(playerNames);
            game.startGame();
            currentPlayer = game.getCurrentPlayer();

            updateStatus("Игра началась! Ходит: " + currentPlayer.getName());
            updateGameDisplay();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Ошибка при создании игры: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            updateStatus("Ошибка: " + e.getMessage());
        }
    }
    private void updateGameDisplay() {
        // Обновляем доску
        updateBoardDisplay();

        // Обновляем набор текущего игрока
        updatePlayerHandDisplay();

        // Обновляем информацию
        updateGameInfo();
    }

    private void updateBoardDisplay() {
        boardPanel.removeAll();

        if (game == null || game.getBoard() == null) {
            JLabel placeholder = new JLabel("Доска пуста. Начните новую игру!");
            placeholder.setFont(new Font("Arial", Font.ITALIC, 14));
            boardPanel.add(placeholder);
        } else {
            JLabel boardLabel = new JLabel(game.getBoard().toString());
            boardLabel.setFont(new Font("Arial", Font.BOLD, 16));
            boardPanel.add(boardLabel);
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void updatePlayerHandDisplay() {
        playerHandPanel.removeAll();

        if (currentPlayer == null || currentPlayer.getHand() == null) {
            JLabel placeholder = new JLabel("Нет активной игры");
            placeholder.setFont(new Font("Arial", Font.ITALIC, 14));
            playerHandPanel.add(placeholder);
        } else {
            List<Domino> hand = currentPlayer.getHand();

            if (hand.isEmpty()) {
                JLabel emptyLabel = new JLabel("У вас нет костяшек!");
                emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
                playerHandPanel.add(emptyLabel);
            } else {
                dominoButtons = new JButton[hand.size()];

                for (int i = 0; i < hand.size(); i++) {
                    Domino domino = hand.get(i);
                    JButton dominoButton = createDominoButton(domino, i);
                    dominoButtons[i] = dominoButton;
                    playerHandPanel.add(dominoButton);
                }
            }
        }

        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    private JButton createDominoButton(Domino domino, int index) {
        //кнопка-костяшка
        JButton button = new JButton(createDominoText(domino));
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(DOMINO_BG);
        button.setForeground(DOMINO_DOT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setPreferredSize(new Dimension(120, 60));

        // Сохраняем индекс костяшки в кнопке
        button.putClientProperty("dominoIndex", index);
        button.putClientProperty("domino", domino);

        // Добавляем всплывающую подсказку
        button.setToolTipText("Костяшка " + domino + " (клик для выбора)");

        // Выделяем выбранную костяшку
        button.addActionListener(e -> selectDominoButton(button));

        return button;
    }

    private void selectDominoButton(JButton selectedButton) {
        // Снимаем выделение со всех кнопок
        if (dominoButtons != null) {
            for (JButton btn : dominoButtons) {
                if (btn != null) {
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                            BorderFactory.createEmptyBorder(10, 20, 10, 20)
                    ));
                    btn.putClientProperty("selected", false);
                }
            }
        }

        // Выделяем текущую
        selectedButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 3),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        selectedButton.putClientProperty("selected", true);
    }

    private String createDominoText(Domino domino) {
        return String.format("[%d|%d]", domino.getLeft(), domino.getRight());
    }

    private void playSelectedDomino() {
        if (game == null || game.isGameOver()) {
            JOptionPane.showMessageDialog(frame,
                    "Сначала начните новую игру!",
                    "Нет активной игры",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Domino selectedDomino = findSelectedDomino();
        if (selectedDomino == null) {
            JOptionPane.showMessageDialog(frame,
                    "Выберите костяшку для хода!",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Запоминаем кто ходил
        Player playerBefore = currentPlayer;

        // Делаем ход
        boolean moveSuccess = game.makeMove(selectedDomino);

        currentPlayer = game.getCurrentPlayer();

        if (moveSuccess) {
            updateStatus(playerBefore.getName() + " сыграл " + selectedDomino);

            if (game.isGameOver()) {
                Player winner = game.getWinner();
                JOptionPane.showMessageDialog(frame,
                        "ИГРА ОКОНЧЕНА! Победитель: " + winner.getName(),
                        "Конец игры",
                        JOptionPane.INFORMATION_MESSAGE);
                updateStatus("Игра окончена! Победитель: " + winner.getName());
            } else {
                updateStatus("Теперь ходит: " + currentPlayer.getName());
            }
        } else {
            updateStatus("Нельзя сыграть " + selectedDomino);
            JOptionPane.showMessageDialog(frame,
                    "Эту костяшку нельзя сыграть сейчас!\n" +
                            "Попробуйте другую костяшку или возьмите из базара.",
                    "Невозможный ход",
                    JOptionPane.WARNING_MESSAGE);
        }

        updateGameDisplay();
    }
    private Domino findSelectedDomino() {
        if (dominoButtons == null) return null;

        for (JButton button : dominoButtons) {
            if (button != null && Boolean.TRUE.equals(button.getClientProperty("selected"))) {
                return (Domino) button.getClientProperty("domino");
            }
        }
        return null;
    }

    private void takeFromBank() {
        if (game == null) {
            JOptionPane.showMessageDialog(frame,
                    "Сначала начните новую игру!",
                    "Нет активной игры",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Запоминаем, кто брал из базара
        Player playerBefore = currentPlayer;
        String playerNameBefore = playerBefore.getName();

        // 2. Выполняем ход "взять из базара"
        boolean couldPlayDrawnDomino = game.makeMove(null);

        // 3. ВАЖНО: Обновляем currentPlayer из движка
        currentPlayer = game.getCurrentPlayer();

        // 4. Отображаем что произошло
        if (couldPlayDrawnDomino) {
            updateStatus(playerNameBefore + " взял и сразу сыграл костяшкой из базара");
        } else {
            // Если не смог сыграть взятой костяшкой
            updateStatus(playerNameBefore + " взял костяшку, ход перешел к " + currentPlayer.getName());
        }

        updateGameDisplay();
    }
    private void passTurn() {
        if (game == null) {
            JOptionPane.showMessageDialog(frame,
                    "Сначала начните новую игру!",
                    "Нет активной игры",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Player playerBefore = currentPlayer;

        game.makeMove(null); // Пас

        // ВАЖНО: Обновляем currentPlayer из движка
        currentPlayer = game.getCurrentPlayer();

        updateStatus(playerBefore.getName() + " пропустил ход. Теперь ходит: " + currentPlayer.getName());
        updateGameDisplay();
    }

    private void updateGameInfo() {
        if (game == null) {
            gameInfoArea.setText("Нет активной игры");
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append("=== ИНФОРМАЦИЯ ОБ ИГРЕ ===\n\n");

        info.append("\n\n");
        info.append("Текущий игрок: ").append(currentPlayer.getName()).append("\n");
        info.append("Костяшек в базаре: ").append(game.getRemainingDominoes()).append("\n");

        int remaining = game.getRemainingDominoes();
        if (remaining == 0) {
            info.append(" (БАЗАР ПУСТ!)");
        }

        info.append("\n=== ИГРОКИ ===\n");
        for (Player player : game.getPlayers()) {
            info.append(player.getName()).append(":\n");
            info.append("  Костяшек: ").append(player.getHand().size()).append("\n");
            info.append("  Очки: ").append(player.getHandValue()).append("\n");

            if (player == currentPlayer) {
                info.append("  [СЕЙЧАС ХОДИТ]\n");
            }
            info.append("\n");
        }

        if (game.isGameOver() && game.getWinner() != null) {
            info.append("ПОБЕДИТЕЛЬ: ").append(game.getWinner().getName()).append("\n");
        }

        gameInfoArea.setText(info.toString());
        gameInfoArea.setCaretPosition(0);
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    private void showRules() {
        String rules = """
            Правила игры:
            
            1. Нажмите "Новая игра" чтобы начать
            2. Выберите костяшку кликом (выделится красным), чтобы поставить
            3. Если нет подходящей костяшки - "Взять" из базара
            4. Победит тот, кто первым сбросит все костяшки!
            
            """;

        JOptionPane.showMessageDialog(frame, rules, "Правила игры", JOptionPane.INFORMATION_MESSAGE);
    }
}