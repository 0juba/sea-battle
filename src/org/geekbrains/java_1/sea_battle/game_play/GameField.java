package org.geekbrains.java_1.sea_battle.game_play;

import org.geekbrains.java_1.sea_battle.exceptions.AlreadyFallenException;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Guba Andrei on 24.12.15.
 */
public class GameField {
    // Размер игрового поля
    public final static int X_MAX = 10;
    public final static int Y_MAX = 10;
    
    // Максимальное количество попыток разместить корабль случайным образом на игровом поле
    public final static int MAX_ATTEMPTS = 10;

    // Ячейки игрового поля
    private Cell[][] cells;
    private ArrayList<Sheep> sheeps = new ArrayList<>();

    public GameField() {
        this.cells = new Cell[GameField.X_MAX][GameField.Y_MAX];
        
        // Заполним объектами Cell ячейки игрового поля
        for (int i = 0; i < GameField.X_MAX; i++) {
            for (int j = 0; j < GameField.Y_MAX; j++) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
    }

    public ArrayList<Sheep> getSheeps() {
        return sheeps;
    }

    public boolean isSheepsDestroyed() {
        for (Sheep sheep : this.sheeps) {
            if (!sheep.isDestroyed()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Метод размещает коллекцию кораблей случайным образом на игровом поле
     * 
     * @param sheeps ArrayList<Sheep>
     */
    public void putSheeps(ArrayList<Sheep> sheeps) {
        Random random = new Random();
        this.sheeps = sheeps;
        
        for (Sheep sheep : sheeps) {
            int x, y, attempts = 0;

            do {                
                // TODO: требуется повторное размещения кораблей
                // TODO: требуется проверка что корабли указанного размера поместятся на поле
                if (attempts > GameField.MAX_ATTEMPTS) {
                    // Слишком много попыток сделано для одного корабля, оставляем попытки его разместить
                    break;
                }

                x = this.getRandPosition(random, GameField.X_MAX);
                y = this.getRandPosition(random, GameField.Y_MAX);

                // Пытаемся разместить корабль по горизонтали слева - направо
                if (this.checkAndMarkPosition(x, y, sheep.setOrientation(Sheep.ORIENTATION_HORIZONTAL))) {
                    break;
                }

                // Пытаемся разместить корабль по вертикали снизу - вверх
                if (this.checkAndMarkPosition(x, y, sheep.setOrientation(Sheep.ORIENTATION_VERTICAL))) {
                    break;
                }

                // Пытаемся разместить корабль по вертикали сверху - вниз
                if (this.checkAndMarkPosition(x, y - sheep.getSize(), sheep.setOrientation(Sheep.ORIENTATION_VERTICAL))) {
                    break;
                }

                // Пытаемся разместить корабль по горизонтали справа - налево
                if (this.checkAndMarkPosition(x - sheep.getSize(), y, sheep.setOrientation(Sheep.ORIENTATION_HORIZONTAL))) {
                    break;
                }

                attempts++;
            } while(true);
        }
    }

    /**
     * Возвращает ячейку по координатам - не индексу массива
     *
     * @param x int
     * @param y int
     * @return Cell
     */
    public Cell getCell(int x, int y) {
        //TODO: проверить границы массива
        return this.cells[x][y];
    }

    public boolean fire(int x, int y) throws AlreadyFallenException {
        Cell cell = this.cells[x-1][y-1];
        Sheep sheep = cell.getSheep();

        if (cell.isHit()) {
            throw new AlreadyFallenException("Вы уже нанесли удар по этим координатам.");
        }

        // Помечаем ячейку, что по ней стрельнули
        cell.setHit(true);

        // Если есть корабль - нужно зафиксировать урон
        if (cell.hasSheep()) {
            sheep.setDamage(cell);
            return true;
        }

        return false;
    }

    /**
     * Метод получает случайную координату из интервала от 1 до MaxN
     * 
     * @param random Random
     * @param maxN int
     * @return int
     */
    public int getRandPosition(Random random, int maxN) {
        int pos = (int)(maxN * random.nextDouble());

        if (pos == 0) {
            pos = this.getRandPosition(random, maxN);
        }

        return pos;
    }

    /**
     * Метод выводит на экран собственное игровое поле
     */
    public void printOwnField() {
        for (int i = 0; i < GameField.X_MAX; i++) {
            System.out.print(" " + Integer.toString(i) + (i + 1 < 10 ? " " : ""));
        }

        System.out.println("");

        for (int i = 0; i < GameField.X_MAX; i++) {
            System.out.println(Integer.toString(i) + " | ");

            for (int j = 0; j < GameField.Y_MAX; j++) {
                Cell cell = this.cells[i][j];
                Sheep sheep = cell.getSheep();

                if (cell.hasSheep()) {
                    if (sheep.isDamaged(cell)) {
                        System.out.print(" x ");
                    } else {
                        System.out.print(" * ");
                    }
                } else {
                    System.out.print(" . ");
                }
            }

            System.out.println("");
        }
    }

    /**
     * Метод выводит на экран чужое игровое поле
     */
    public void printEnemyField() {
        for (int i = 0; i < GameField.X_MAX; i++) {
            System.out.print(" " + Integer.toString(i) + (i + 1 < 10 ? " " : ""));
        }

        System.out.println("");

        for (int i = 0; i < GameField.X_MAX; i++) {
            System.out.println(Integer.toString(i) + " | ");

            for (int j = 0; j < GameField.Y_MAX; j++) {
                Cell cell = this.cells[i][j];
                Sheep sheep = cell.getSheep();

                if (cell.hasSheep() && sheep.isDamaged(cell)) {
                    System.out.print(" x ");
                } else {
                    System.out.print(" . ");
                }
            }

            System.out.println("");
        }
    }

    /**
     * Метод пытается случайным образом разместить корабль на игровом поле. Возвращает true - в случае успеха, false - если это невозможно
     * 
     * @param x int
     * @param y int
     * @param sheep Sheep
     * @return boolean
     */
    private boolean checkAndMarkPosition(int x, int y, Sheep sheep) {
        // Индексы массива начинаютс с 0
        int xI = x - 1;
        int yI = y - 1;

        int size = sheep.getSize();

        switch (sheep.getOrientation()) {
            case Sheep.ORIENTATION_HORIZONTAL:
                // Проверяем, что корабль помещается
                if (x + size > GameField.X_MAX || x < 1) {
                    return false;
                }

                // Проверяем клетку после корабля
                if (x > 1 && cells[xI - 1][yI].hasSheep()) {
                    return false;
                }

                // Проверяем клетку перед кораблем
                if (x + size <= GameField.X_MAX && cells[xI + size][yI].hasSheep()) {
                    return false;
                }

                // Сперва проанализируем клетки
                for (int i = xI; i < size + xI; i++) {
                    // Проверим позицию корабля - ячейка не должна содержать корабль или быть смежной
                    if (!cells[i][yI].isFree()) {
                        return false;
                    }

                    // Проверяем клетки сверху
                    if (y < GameField.Y_MAX && cells[i][yI + 1].hasSheep()) {
                        return false;
                    }

                    // Проверяем клетки снизу
                    if (y > 1 && cells[i][yI - 1].hasSheep()) {
                        return false;
                    }
                }

                // Помещаем корабль на игровое поле
                for (int i = xI; i < size + xI; i++) {
                    // Помечаем клетку занятой кораблем
                    cells[i][yI].setSheep(sheep);

                    // Клетку сверху помеяаем недоступной
                    if (y < GameField.Y_MAX) {
                        cells[i][yI + 1].setNeighboring();
                    }

                    // Клетку снизу помечаем недоступной
                    if (y > 1) {
                        cells[i][yI - 1].setNeighboring();
                    }
                }

                // Клетку перед кораблем помечаем недоступной
                if (x > 1) {
                    cells[xI - 1][yI].setNeighboring();
                }

                // Клетку после корабля помечаем недоступной
                if (x + size <= GameField.X_MAX) {
                    cells[xI + size][yI].setNeighboring();
                }

                break;
            case Sheep.ORIENTATION_VERTICAL:
                // Проверяем что корабль помещается
                if (y + size > GameField.Y_MAX || y < 1) {
                    return false;
                }

                // Проверяем что конец корабля не попадает в другой
                if (y > 1 && cells[xI][yI - 1].hasSheep()) {
                    return false;
                }

                // Проверяем что нос корабля не попадает в другой
                if (y + size < GameField.Y_MAX && cells[xI][yI + size].hasSheep()) {
                    return false;
                }

                // Проверим размещение всего корабля
                for (int i = yI; i < size + yI; i++) {
                    // Проверим что ячейка не занята кораблем и не является смежной
                    if (!cells[xI][i].isFree()) {
                        return false;
                    }

                    // Ячейки справа не должны содержать кораблей
                    if (x < GameField.X_MAX && cells[xI + 1][i].hasSheep()) {
                        return false;
                    }

                    // Ячейки слева не должны содержать кораблей
                    if (x > 1 && cells[xI - 1][i].hasSheep()) {
                        return false;
                    }
                }

                // Размещаем корабль на игровом поле
                for (int i = yI; i < size + yI; i++) {
                    // Помечаем саму ячейка, что она содержит корабль
                    cells[xI][i].setSheep(sheep);

                    // Помечаем смежную ячейку справа
                    if (x < GameField.X_MAX) {
                        cells[xI + 1][i].setNeighboring();
                    }

                    // Помечаем смежную ячейку слева
                    if (x > 1) {
                        cells[xI - 1][i].setNeighboring();
                    }
                }

                // Помечаем смежную ячейку после конца корабля
                if (y > 1) {
                    cells[xI][yI - 1].setNeighboring();
                }

                // Помечаем смежную ячейку перед началом корабля
                if (y + size <= GameField.Y_MAX) {
                    cells[xI][yI + size].setNeighboring();
                }

                break;
        }

        return true;
    }
}