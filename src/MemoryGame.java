import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MemoryGame {
    private int boardSize;
    private String[][] board;
    private boolean[][] flipped;
    private String[] players;
    private int[] scores;
    private final Scanner scanner;

    public MemoryGame() {
        scanner = new Scanner(System.in);
    }


    public void displayMenu() {
        while (true) {
            System.out.println("\nMENU:");
            System.out.println("1. INICIAR");
            System.out.println("2. PONTUAÇÃO PARTICIPANTES");
            System.out.println("3. REGRAS DO JOGO");
            System.out.println("4. SAIR");
            System.out.print("Escolha uma opção: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    setupGame();
                    playGame();
                    break;
                case "2":
                    System.out.println("Você ainda não jogou, por isso não tem um resultado a ser exibido.");
                    break;
                case "3":
                    displayRules();
                    break;
                case "4":
                    System.out.println("Saindo do jogo...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public void displayRules() {
        System.out.println("\nREGRAS DO JOGO:");
        System.out.println("1. O jogo consiste em um tabuleiro com pares de cartas escondidas.");
        System.out.println("2. Os jogadores alternam as jogadas tentando encontrar pares iguais.");
        System.out.println("3. Cada cor tem uma pontuação diferente.");
        System.out.println("4. O jogador com maior pontuação ao final vence.");
    }

    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    public void setupGame() {
        System.out.println("QUAL O TAMANHO DE TABULEIRO DESEJA JOGAR?");
        System.out.println("a. 4 x 4");
        System.out.println("b. 6 x 6");
        System.out.println("c. 8 x 8");
        System.out.println("d. 10 x 10");
        String option = scanner.nextLine().toLowerCase();

        switch (option) {
            case "a":
                boardSize = 4;
                break;
            case "b":
                boardSize = 6;
                break;
            case "c":
                boardSize = 8;
                break;
            case "d":
                boardSize = 10;
                break;
            default:
                System.out.println("Por favor, escolha uma das opções de tamanho de tabuleiro disponíveis.");
                setupGame();
                return;
        }

        createBoard();
        setupPlayers();
    }

    private void createBoard() {
        int totalCards = boardSize * boardSize;
        int totalPairs = totalCards / 2;

        List<String> cards = new ArrayList<>();

        cards.add("branco");

        int remainingPairs = totalPairs - 1;

        int redBluePairs = remainingPairs / 2;
        int yellowPairs = remainingPairs - (redBluePairs * 2);

        for (int i = 0; i < redBluePairs; i++) {
            cards.add("vermelho");
            cards.add("azul");
        }

        for (int i = 0; i < yellowPairs; i++) {
            cards.add("amarelo");
        }

        cards.addAll(new ArrayList<>(cards));

        Collections.shuffle(cards);

        board = new String[boardSize][boardSize];
        flipped = new boolean[boardSize][boardSize];

        int index = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = cards.get(index);
                index++;
            }
        }
    }

    private void setupPlayers() {
        players = new String[2];
        scores = new int[2];
        String player1 = "PARTICIPANTE01";
        String player2 = "PARTICIPANTE02";

        System.out.print("QUAL O APELIDO DA(O) PARTICIPANTE 1? ");
        String input1 = scanner.nextLine();
        players[0] = input1.isEmpty() ? player1 : input1;
        System.out.print("QUAL O APELIDO DA(O) PARTICIPANTE 2? ");
        String input2 = scanner.nextLine();
        players[1] = input2.isEmpty() ? player2 : input2;
    }

    private void printColored(String position) {
        switch (position) {
            case "azul":
                System.out.print(ANSI_BLUE + "O" + "\t" + ANSI_RESET);
                break;
            case "amarelo":
                System.out.print(ANSI_YELLOW + "O" + "\t" + ANSI_RESET);
                break;
            case "vermelho":
                System.out.print(ANSI_RED + "O" + "\t" + ANSI_RESET);
                break;
            case "branco":
                System.out.print(ANSI_WHITE + "O" + "\t" + ANSI_RESET);
                break;
            default:
                System.out.print(position + "\t");
        }
    }

    private void displayBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (flipped[i][j]) {
                    printColored(board[i][j]);
                } else {
                    System.out.print("X\t");
                }
            }
            System.out.println();
        }
    }

    private int[] getCardPosition(String cardNumber) {
        while (true) {
            try {
                System.out.print("DIGITE A POSIÇÃO DA " + cardNumber + " QUE DESEJA REVELAR: LINHA: ");
                int row = Integer.parseInt(scanner.nextLine()) - 1;
                System.out.print("COLUNA: ");
                int col = Integer.parseInt(scanner.nextLine()) - 1;

                if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
                    if (!flipped[row][col]) {
                        return new int[]{row, col};
                    } else {
                        System.out.println("A carta da posição informada já está virada, por favor, escolha outra posição.");
                    }
                } else {
                    System.out.println("Posição da carta inválida, por favor, insira uma posição válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira números válidos.");
            }
        }
    }

    private void updateScore(int playerIndex, String card) {
        switch (card) {
            case "amarelo":
                scores[playerIndex] += 1;
                break;
            case "vermelho":
            case "azul":
                scores[playerIndex] += 5;
                break;
            case "branco":
                scores[playerIndex] += 50;
                break;
        }
    }

    private boolean checkGameOver() {
        for (boolean[] row : flipped) {
            for (boolean card : row) {
                if (!card) {
                    return false;
                }
            }
        }
        return true;
    }

    private void displayScores() {
        System.out.println("PONTUAÇÃO FINAL:");
        for (int i = 0; i < players.length; i++) {
            System.out.println(players[i] + ": " + scores[i] + " pontos");
        }
    }

    public void playGame() {
        int currentPlayer = 0;
        boolean gameOver = false;
        while (!gameOver) {
            displayBoard();
            System.out.println("PARTICIPANTE: " + players[currentPlayer]);

            int[] pos1 = getCardPosition("PRIMEIRA CARTA");
            int[] pos2 = getCardPosition("SEGUNDA CARTA");

            String card1 = board[pos1[0]][pos1[1]];
            String card2 = board[pos2[0]][pos2[1]];

            flipped[pos1[0]][pos1[1]] = true;
            flipped[pos2[0]][pos2[1]] = true;

            displayBoard();

            if (card1.equals(card2)) {
                System.out.println("ACERTOU! GANHOU PONTOS.");
                updateScore(currentPlayer, card1);
            } else {
                System.out.println("ERROU! PERDEU PONTOS.");
                flipped[pos1[0]][pos1[1]] = false;
                flipped[pos2[0]][pos2[1]] = false;
                currentPlayer = 1 - currentPlayer;
            }

            if (checkGameOver()) {
                gameOver = true;
            }
        }

        displayScores();
    }

    public static void main(String[] args) {
        MemoryGame game = new MemoryGame();
        game.displayMenu();
    }
}