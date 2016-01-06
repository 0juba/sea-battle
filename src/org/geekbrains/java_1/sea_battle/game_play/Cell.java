package org.geekbrains.java_1.sea_battle.game_play;

/**
 * Created by Guba Andrei on 22.12.15.
 */
public class Cell {
    private int x;
    private int y;

    // Ячейка занята, означает что ячейка не содержит сектор корабля, но является смежной с другим кораблем, находящимся в соседней ячейке
    private boolean neighboring;

    // Ячейка подбита, т/е по ней уже нанесли удар
    private boolean hit;

    // Сектор корабля который находится в этой ячейке
    private Ship ship;

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
        return !this.isNeighboring() && !this.hasSheep();
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

    public Ship getSheep() {
        return ship;
    }

    public void setSheep(Ship ship) {
        if (!this.hasSheep()) {
            this.ship = ship;
        }
    }

    /**
     * Ячейка содержит сектор корабля
     * @return boolean
     */
    public boolean hasSheep() {
        return this.ship == null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
