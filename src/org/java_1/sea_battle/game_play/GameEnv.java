package org.java_1.sea_battle.game_play;

import org.java_1.sea_battle.player.PlayerAiDummy;
import org.java_1.sea_battle.player.PlayerHuman;

import java.util.ArrayList;

/**
 * Created by Guba Andrei on 24.12.15.
 */
public class GameEnv {
    private GameField userGameField;
    private GameField aiGameField;
    private PlayerHuman playerHuman;
    private PlayerAiDummy playerAi;

    public GameEnv() {
        this.playerHuman = new PlayerHuman();
        this.playerAi = new PlayerAiDummy();
    }

    /**
     * Обновляет игровое окружение
     */
    public void refresh() {
        // Создаем игровые поля
        this.userGameField = new GameField();
        this.aiGameField = new GameField();

        // Заполняем их короблями
        this.userGameField.putShips(this.createShips());
        this.aiGameField.putShips(this.createShips());
    }

    public PlayerHuman getPlayerHuman() {
        return playerHuman;
    }

    public PlayerAiDummy getPlayerAi() {
        return playerAi;
    }

    public GameField getUserGameField() {
        return userGameField;
    }

    public GameField getAiGameField() {
        return aiGameField;
    }

    private ArrayList<Ship> createShips() {
        ArrayList<Ship> ships = new ArrayList<>();

        // Size 1, allow 4
        ships.add(new Ship(Ship.SHEEP_SIZE_1));
        ships.add(new Ship(Ship.SHEEP_SIZE_1));
        ships.add(new Ship(Ship.SHEEP_SIZE_1));
        ships.add(new Ship(Ship.SHEEP_SIZE_1));

        // Size 2, allow 3
        ships.add(new Ship(Ship.SHEEP_SIZE_2));
        ships.add(new Ship(Ship.SHEEP_SIZE_2));
        ships.add(new Ship(Ship.SHEEP_SIZE_2));

        // Size 3, allow 2
        ships.add(new Ship(Ship.SHEEP_SIZE_3));
        ships.add(new Ship(Ship.SHEEP_SIZE_3));

        // Size 4, allow 1
        ships.add(new Ship(Ship.SHEEP_SIZE_4));

        return ships;
    }
}
