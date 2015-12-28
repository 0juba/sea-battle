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
        int i = 0, x = 0, y = 0;
        Scanner scanner = new Scanner(System.in);

        // Запросим координаты у пользователя
        do {
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
                continue;
            }

            System.out.println("Вы указали неверные координаты, повторите заново!");
            i++;
        } while(i < super.ATTEMPTS_PER_TURN);

        // Успешное попадание
        boolean successHit = false;

        try {
            // Попробуем ударить по вражескому кораблю
            successHit = enemyField.fire(x, y);
        } catch (AlreadyFallenException e) {
            System.out.println(e.getMessage());
        }

        // Запомним ход пользователя
        super.addHistoryRow(enemyField.getCell(x, y), successHit);

        return successHit;
    }
}
