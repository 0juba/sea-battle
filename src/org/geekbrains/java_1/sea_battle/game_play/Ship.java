package org.geekbrains.java_1.sea_battle.game_play;

import java.util.HashMap;

/**
 * Created by Guba Andrei on 22.12.15.
 */
public class Ship {
    public static final int ORIENTATION_VERTICAL = 1;
    public static final int ORIENTATION_HORIZONTAL = 2;

    public static final int SHEEP_SIZE_1 = 1;
    public static final int SHEEP_SIZE_2 = 2;
    public static final int SHEEP_SIZE_3 = 3;
    public static final int SHEEP_SIZE_4 = 4;

    private int size;
    private boolean destroyed;
    private int orientation;

    // Ключ - это координата сектора корабля, значение - подбит (true) или целый (false)
    private HashMap<Cell, Boolean> position;

    public Ship(int shipSize) {
        this.size = shipSize;
        this.destroyed = false;

        this.position = new HashMap<>();
    }

    public int getSize() {
        return size;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public int getOrientation() {
        return orientation;
    }

    public Ship setOrientation(int orientation) {
        this.orientation = orientation;

        return this;
    }

    /**
     * Метод сохраняет позицию сектора корабля
     *
     * @param position Cell
     * @return Ship
     */
    public Ship placeSheep(Cell position) {
        this.position.put(position, false);

        return this;
    }

    public boolean isDamaged(Cell position) {
        Boolean damaged = this.position.get(position);

        return damaged == null ? false : damaged;
    }

    /**
     * Метод фиксирует урон по сектору корабля
     *
     * @param position Cell
     * @return Ship
     */
    public Ship setDamage(Cell position) {
        if (null != this.position.get(position) && !this.position.get(position)) {
            this.position.put(position, true);

            int damagedSectors = 0;
            for (Boolean f : this.position.values()) {
                if (f) {
                    damagedSectors++;
                }
            }

            if (damagedSectors == this.size) {
                this.destroyed = true;
            }
        }

        return this;
    }
}
