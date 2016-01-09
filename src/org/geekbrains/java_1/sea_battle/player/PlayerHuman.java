package org.geekbrains.java_1.sea_battle.player;

import org.geekbrains.java_1.sea_battle.exceptions.AlreadyFallenException;
import org.geekbrains.java_1.sea_battle.game_play.GameField;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Guba Andrei on 24.12.15.
 */
public class PlayerHuman extends Player {
    @Override
    public boolean makeTurn(GameField enemyField) {
        int attemptsCount = super.ATTEMPTS_PER_TURN, x = 0, y = 0;
        Scanner scanner = new Scanner(System.in);

        // Запросим координаты у пользователя
        do {
            attemptsCount--;

            System.out.println(String.format("Введите координаты x[1..%d], y[1..%d]:", GameField.X_MAX, GameField.Y_MAX));
            System.out.println();

            try {
                x = scanner.nextInt();
                y = scanner.nextInt();

                if ((x >= 1 && x <= GameField.X_MAX) && (y >= 1 && y <= GameField.Y_MAX)) {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Можно ввести только целое число!");
                // В буфере входного потока осталось неверное значение, заберем его
                scanner.nextLine();
            }

            System.out.println("Вы указали неверные координаты, повторите заново! Осталось попыток: " + attemptsCount);
        } while(attemptsCount > 0);

        if (attemptsCount == 0) {
            System.out.println("Вы исчерпали допустимое количество попыток! Ход передается опоненту!");
            return false;
        }

        // Успешное попадание
        boolean successHit = false;

        try {
            // Попробуем ударить по вражескому кораблю
            successHit = enemyField.fire(
                    GameField.convertXCoordinateToIndex(x),
                    GameField.convertYCoordinateToIndex(y)
            );
        } catch (AlreadyFallenException e) {
            System.out.println(e.getMessage());
        }

        // Запомним ход пользователя
        super.addHistoryRow(enemyField.getCell(x, y), successHit);

        return successHit;
    }
}
