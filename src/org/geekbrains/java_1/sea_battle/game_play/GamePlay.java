package org.geekbrains.java_1.sea_battle.game_play;

import org.geekbrains.java_1.sea_battle.player.PlayerAiDummy;
import org.geekbrains.java_1.sea_battle.player.PlayerHuman;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Класс отвечает за взаимодействие с пользователем - игровой процесс
 *
 * Created by Guba Andrei on 25.12.15.
 */
public class GamePlay {
    final public int EXIT = 2;
    final public String DEFAULT_NAME = "Player 1";

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

            if (userChoice == 1) {
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
                        case 1:
                            // Пользователь делает ход
                            if (playerHuman.makeTurn(AIGameField)) {
                                // Игрок попал, продолжаем делать ходы
                                System.out.println("Есть! Попадание!");

                                // Проверим, вдруг все убиты, пора закругляться
                                if (AIGameField.isShipsDestroyed()) {
                                    System.out.println("Поздравляем! Вы победили!");
                                    choice = 5;
                                } else {
                                    break;
                                }
                            } else {
                                System.out.println("Промах! Ход опонента!");
                                // Ход компьютера
                                do {
                                    if (playerAiDummy.makeTurn(userGameField)) {
                                        System.out.println("Ваш корабль подбит!");

                                        // Победа компьютера
                                        if (userGameField.isShipsDestroyed()) {
                                            System.out.println("Вы проиграли! Попытайте счастье в другой раз");
                                            choice = 5;
                                            break;
                                        }
                                    } else {
                                        // Передаем управление пользователю
                                        System.out.println("Враг промазал!");
                                        break;
                                    }
                                } while (true);

                                if (choice == 1) {
                                    break;
                                }
                            }
                        case 2:
                            // Вывод поля для опонента
                            AIGameField.printEnemyField();

                            break;
                        case 3:
                            // Смотрим свое поле
                            userGameField.printOwnField();

                            break;
                        case 4:
                            // Показать историю ходов
                            playerHuman.printHistory();

                            break;
                        case 5:
                            // Конец игры
                            break;
                    }
                } while (choice != 5);
            }
        } while (userChoice != this.EXIT);
    }

    public void displayMainMenu() {
        System.out.println("1. Начать бой");
        System.out.println("2. Выход");
        System.out.println();
    }

    public void displayGameMenu() {
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
}
