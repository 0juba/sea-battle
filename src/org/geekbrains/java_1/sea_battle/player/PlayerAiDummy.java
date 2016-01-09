package org.geekbrains.java_1.sea_battle.player;

import org.geekbrains.java_1.sea_battle.exceptions.AlreadyFallenException;
import org.geekbrains.java_1.sea_battle.game_play.GameField;

import java.util.Random;

/**
 * Проста реализация искуственного "опонента", бьет постоянно по случайным координатам
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

        int x = enemyField.getRandPosition(random, GameField.X_MAX);
        int y = enemyField.getRandPosition(random, GameField.Y_MAX);

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
