package org.java_1.sea_battle.game_play;

import org.java_1.sea_battle.player.PlayerAiDummy;
import org.java_1.sea_battle.player.PlayerHuman;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Класс отвечает за взаимодействие с пользователем - игровой процесс
 *
 * Created by Guba Andrei on 25.12.15.
 */
public class GamePlay {
    final public String DEFAULT_NAME = "Player 1";

    private static GamePlay instance;

    private GamePlay() {}

    public static GamePlay getInstance() {
        if (GamePlay.instance == null) {
            GamePlay.instance = new GamePlay();
        }

        return GamePlay.instance;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String playerName;
        GameEnv gameEnv = new GameEnv();
        int userChoice;

        // Приветствуем пользователя
        this.displayGreeting();

        // Узнаем его имя
        System.out.println("Введите ваше имя: ");
        playerName = scanner.nextLine();

        if (playerName.length() == 0) {
            playerName = this.DEFAULT_NAME;
        }

        // Запомним имя игрока
        gameEnv.getPlayerHuman().setName(playerName);

        do {
            // Показываем основное меню
            this.displayMainMenu();
            userChoice = this.readIntFromConsole(scanner);

            if (userChoice == MainMenu.START_GAME) {
                System.out.println("\nДа начнется бой!\n");

                // Подготовим игровые поля и расставим корабли
                gameEnv.refresh();

                int choice = 0;
                // Игра
                do {
                    this.displayGameMenu();
                    choice = this.readIntFromConsole(scanner);

                    PlayerHuman playerHuman = gameEnv.getPlayerHuman();
                    PlayerAiDummy playerAiDummy = gameEnv.getPlayerAi();
                    GameField AIGameField = gameEnv.getAiGameField();
                    GameField userGameField = gameEnv.getUserGameField();

                    switch (choice) {
                        case PlayMenu.DO_TURN:
                            // Пользователь делает ход
                            AIGameField.printEnemyField();

                            if (playerHuman.makeTurn(AIGameField)) {
                                // Игрок попал, продолжаем делать ходы
                                playerHuman.tell("Ура! Попал!");

                                // Проверим, вдруг все убиты, пора закругляться
                                if (AIGameField.isShipsDestroyed()) {
                                    playerHuman.tell("Победа!");
                                    choice = PlayMenu.STOP_GAME;
                                } else {
                                    break;
                                }
                            } else {
                                playerAiDummy.tell("Промах! Мой ход! Тебе конец! Бгааааа!!!");

                                // Ход компьютера
                                do {
                                    if (playerAiDummy.makeTurn(userGameField)) {
                                        playerAiDummy.tell("Попадание! Один готов! Следующий!");

                                        // Победа компьютера
                                        if (userGameField.isShipsDestroyed()) {
                                            playerAiDummy.tell("Ты проиграл! Может в следующий раз повезет?");
                                            choice = 5;

                                            break;
                                        }
                                    } else {
                                        // Передаем управление пользователю
                                        playerAiDummy.tell("О-о-у.... Промазал???");

                                        break;
                                    }
                                } while (true);

                                if (choice == 1) {
                                    break;
                                }
                            }
                        case PlayMenu.SHOW_ENEMY_FIELD:
                            // Вывод поля для опонента
                            AIGameField.printEnemyField();

                            break;
                        case PlayMenu.SHOW_MY_FIELD:
                            // Смотрим свое поле
                            userGameField.printOwnField();

                            break;
                        case PlayMenu.SHOW_HISTORY:
                            // Показать историю ходов
                            playerHuman.printHistory();

                            break;
                        case PlayMenu.STOP_GAME:
                            // Конец игры
                            break;
                    }
                } while (choice != PlayMenu.STOP_GAME);
            }
        } while (userChoice != MainMenu.EXIT_GAME);
    }

    public void displayMainMenu() {
        System.out.println();
        System.out.println("1. Начать бой");
        System.out.println("2. Выход");
        System.out.println();
    }

    public void displayGameMenu() {
        System.out.println();
        System.out.println("1. Сделать ход");
        System.out.println("2. Показать поле противника");
        System.out.println("3. Показать мое поле");
        System.out.println("4. Моя история ходов");
        System.out.println("5. Завершить текущую игру");
        System.out.println();
    }

    public void displayGreeting() {
        System.out.println("Добро пожаловать в игру Морской бой (классический)!");
    }

    private int readIntFromConsole(Scanner scanner) {
        int inputInt = 0;

        try {
            inputInt = scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Можно вводить только целые числа - номера пунктов меню!");
        }

        return inputInt;
    }

    public static class MainMenu {
        public final static int START_GAME = 1;
        public final static int EXIT_GAME = 2;
    }

    public static class PlayMenu {
        public final static int DO_TURN = 1;
        public final static int SHOW_ENEMY_FIELD = 2;
        public final static int SHOW_MY_FIELD = 3;
        public final static int SHOW_HISTORY = 4;
        public final static int STOP_GAME = 5;
    }
}
