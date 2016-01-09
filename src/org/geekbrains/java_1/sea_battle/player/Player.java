package org.geekbrains.java_1.sea_battle.player;

import org.geekbrains.java_1.sea_battle.game_play.Cell;
import org.geekbrains.java_1.sea_battle.game_play.GameField;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Guba Andrei on 24.12.15.
 */
abstract public class Player implements PlayerInterface {
    final public int ATTEMPTS_PER_TURN = 3;

    private String name;
    private GameField gameField;

    protected HashMap<Cell, Boolean> history = new HashMap<>();

    public HashMap<Cell, Boolean> getHistory() {
        return history;
    }

    public void addHistoryRow(Cell cell, boolean successHit) {
        this.history.put(cell, successHit);
    }

    public int countTurns() {
        return this.history.size();
    }

    /**
     *
     * @param phrase String
     */
    public void tell(String phrase) {
        System.out.println();
        System.out.println("-> " + this.name + ": " + phrase);
        System.out.println("---------------------");
    }

    public void printHistory() {
        for(Map.Entry<Cell, Boolean> entry : this.history.entrySet()) {
            Cell cell = entry.getKey();
            Boolean successHit = entry.getValue();

            System.out.println(String.format(
                    "-> x: %d, y: %d <- %s",
                    cell.getX(),
                    cell.getY(),
                    successHit ? "попадание" : "промах"
            ));
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameField getGameField() {
        return gameField;
    }

    public void setGameField(GameField gameField) {
        this.gameField = gameField;
    }

    abstract public boolean makeTurn(GameField enemyField);
}
