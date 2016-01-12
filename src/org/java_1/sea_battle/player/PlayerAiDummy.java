package org.java_1.sea_battle.player;

import org.java_1.sea_battle.exceptions.AlreadyFallenException;
import org.java_1.sea_battle.game_play.GameField;

import java.util.Random;

/**
 * Простая реализация искуственного "опонента", бьет постоянно по случайным координатам
 *
 * Created by Guba Andrei on 25.12.15.
 */
public class PlayerAiDummy extends Player {
    public PlayerAiDummy() {
        super.setName("Skynet");
    }

    @Override
    public boolean makeTurn(GameField enemyField) {
        Random random = new Random();
        int x, y, attempts = 10;

        do {
            x = GameField.convertXCoordinateToIndex(enemyField.getRandPosition(random, GameField.X_MAX));
            y = GameField.convertYCoordinateToIndex(enemyField.getRandPosition(random, GameField.Y_MAX));
        } while (enemyField.getCell(x, y).isHit() || --attempts > 0);

        // Успешное попадание
        boolean successHit = false;

        try {
            // Попробуем ударить по вражескому кораблю
            successHit = enemyField.fire(x, y);
        } catch (AlreadyFallenException e) {
            // Ничего не делаем
        }

        // Запомним ход компьютера
        super.addHistoryRow(enemyField.getCell(x, y), successHit);

        return successHit;
    }
}
