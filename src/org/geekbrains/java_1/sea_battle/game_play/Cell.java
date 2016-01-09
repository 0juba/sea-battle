package org.geekbrains.java_1.sea_battle.game_play;

import java.util.ArrayList;

/**
 * Created by Guba Andrei on 22.12.15.
 */
public class Cell {
    private int x;
    private int y;

    // Ячейка занята, означает что ячейка не содержит сектор корабля, но является смежной с другим кораблем, находящимся в соседней ячейке
    private boolean neighboring;

    /**
     * Список ячеек смежнных текущей - заполняется если текущая ячейка содержит сектор корабля
     */
    private ArrayList<Cell> neighboringCells = new ArrayList<>();

    // Ячейка подбита, т/е по ней уже нанесли удар
    private boolean hit;

    // Сектор корабля который находится в этой ячейке
    private Ship ship = null;

    public ArrayList<Cell> getNeighboringCells() {
        return neighboringCells;
    }

    public void addNeighboringCell(Cell cell) {
        this.neighboringCells.add(cell);
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * По ячейке нанесен удар
     * @return boolean
     */
    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    /**
     * Ячейка свободна - т/е не смежная и не содержит сектор корабля
     * @return boolean
     */
    public boolean isFree() {
        return !this.isNeighboring() && !this.hasShip();
    }

    /**
     * Ячейка смежная - т/е находится по бокам корабля, либо перед ним, либо после
     * @return boolean
     */
    public boolean isNeighboring() {
        return neighboring;
    }

    public void setNeighboring() {
        this.neighboring = true;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        if (!this.hasShip()) {
            this.ship = ship;
        }
    }

    /**
     * Ячейка содержит сектор корабля
     * @return boolean
     */
    public boolean hasShip() {
        return this.ship != null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
