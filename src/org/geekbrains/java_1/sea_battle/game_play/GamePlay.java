package org.geekbrains.java_1.sea_battle.game_play;

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
            userChoice = scanner.nextInt();

            if (userChoice == 1) {
                System.out.println("Да начнется бой!");

                // Подготовим игровые поля и расставим корабли
                gameEnv.refresh();

                int choice = 0;
                // Игра
                do {
                    this.displayGameMenu();
                    choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            if (gameEnv.getPlayerHuman().makeTurn(gameEnv.getAiGameField())) {
                                // Игрок попал, продолжаем делать ходы
                                System.out.println("Есть! Попадание!");

                                // Проверим, вдруг все убиты, пора закругляться
                                if (gameEnv.getAiGameField().isSheepsDestroyed()) {
                                    System.out.println("Поздравляем! Вы победили!");
                                    choice = 5;
                                }

                            } else {
                                System.out.println("Промах! Ход опонента!");
                                // Ход компьютера
                                do {
                                    if (gameEnv.getPlayerAi().makeTurn(gameEnv.getUserGameField())) {
                                        System.out.println("Ваш корабль подбит!");

                                        // Победа компьютера
                                        if (gameEnv.getUserGameField().isSheepsDestroyed()) {
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
                            }

                            break;
                        case 2:
                            // Вывод поля для опонента
                            gameEnv.getAiGameField().printEnemyField();

                            break;
                        case 3:
                            // Смотрим свое поле
                            gameEnv.getUserGameField().printOwnField();

                            break;
                        case 4:
                            // Показать историю ходов
                            gameEnv.getPlayerHuman().printHistory();

                            break;
                        case 5:
                            // Конец игры
                            break;
                    }
                } while (choice != 5);
            } else {
                System.out.println("Неверный выбор. Допустимые значения: 1, 2.");
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
}
