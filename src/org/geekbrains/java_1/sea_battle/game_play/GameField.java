package org.geekbrains.java_1.sea_battle.game_play;

import org.geekbrains.java_1.sea_battle.exceptions.AlreadyFallenException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Guba Andrei on 24.12.15.
 */
public class GameField {
    // Размер игрового поля
    public final static int X_MAX = 10;
    public final static int Y_MAX = 10;
    
    // Максимальное количество попыток разместить корабль случайным образом на игровом поле
    public final static int MAX_ATTEMPTS = 100;

    // Ячейки игрового поля
    private Cell[][] cells;
    // Список кораблей
    private ArrayList<Ship> ships = new ArrayList<>();

    public GameField() {
        this.cells = new Cell[GameField.X_MAX][GameField.Y_MAX];
        
        // Заполним объектами Cell ячейки игрового поля
        for (int i = 0; i < GameField.X_MAX; i++) {
            for (int j = 0; j < GameField.Y_MAX; j++) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public boolean isShipsDestroyed() {
        for (Ship ship : this.ships) {
            if (!ship.isDestroyed()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Метод размещает коллекцию кораблей случайным образом на игровом поле
     * 
     * @param ships ArrayList<Ship>
     */
    public void putShips(ArrayList<Ship> ships) {
        Random random = new Random();
        this.ships = ships;
        
        for (Ship ship : ships) {
            int x, y, attempts = 0;

            do {
                x = this.getRandPosition(random, GameField.X_MAX);
                y = this.getRandPosition(random, GameField.Y_MAX);

                // Пытаемся разместить корабль по горизонтали слева - направо
                if (this.checkAndMarkPositionOnField(x, y, ship.setOrientation(Ship.ORIENTATION_HORIZONTAL))) {
                    break;
                }

                // Пытаемся разместить корабль по вертикали снизу - вверх
                if (this.checkAndMarkPositionOnField(x, y, ship.setOrientation(Ship.ORIENTATION_VERTICAL))) {
                    break;
                }

                // Пытаемся разместить корабль по вертикали сверху - вниз
                if (this.checkAndMarkPositionOnField(x, y - ship.getSize(), ship.setOrientation(Ship.ORIENTATION_VERTICAL))) {
                    break;
                }

                // Пытаемся разместить корабль по горизонтали справа - налево
                if (this.checkAndMarkPositionOnField(x - ship.getSize(), y, ship.setOrientation(Ship.ORIENTATION_HORIZONTAL))) {
                    break;
                }
                // TODO: требуется повторное размещения кораблей
                // TODO: требуется проверка что корабли указанного размера поместятся на поле
            } while(++attempts < GameField.MAX_ATTEMPTS);
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

    public static int convertXCoordinateToIndex(int xCoordinate) {
        return xCoordinate - 1;
    }

    public static int convertYCoordinateToIndex(int yCoordinate) {
        return yCoordinate - 1;
    }

    /**
     * Метод фиксирует попытку нанесту удар по кораблю размещенному в этом поле
     *
     * @param x int Индекс массива !НЕ Координата
     * @param y int Индекс массива !НЕ Координата
     * @return boolean
     * @throws AlreadyFallenException
     */
    public boolean fire(int x, int y) throws AlreadyFallenException {
        Cell cell = this.cells[x][y];
        Ship ship = cell.getShip();

        if (cell.isHit()) {
            throw new AlreadyFallenException("Вы уже нанесли удар по этим координатам.");
        }

        // Помечаем ячейку, что по ней стрельнули
        cell.setHit(true);

        // Если есть корабль - нужно зафиксировать урон
        if (cell.hasShip()) {
            ship.setDamage(cell);

            // Если корабль уничтожен - помечаем все соседние ячейки как подбитые
            if (ship.isDestroyed()) {
                HashMap<Cell, Boolean> shipPosition = ship.getPosition();

                // Пробегаем по всем ячейкам где расположены секции корабля
                for(Cell shipCell : shipPosition.keySet()) {
                    // В каждой ячейке пробегаем по списку смежных ячеек
                    for (Cell neighboringCell : shipCell.getNeighboringCells()) {
                        neighboringCell.setHit(true);
                    }
                }
            }

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
     * Метод выводит в консоль игровое поле
     *
     * @param showShip boolean
     */
    private void printField(boolean showShip) {
        System.out.print("     ");

        // Выведем ось Х
        for (int i = 1; i <= GameField.X_MAX; i++) {
            System.out.print(" " + Integer.toString(i) + (i + 1 < GameField.X_MAX ? " " : ""));
        }

        System.out.println("");

        for (int i = 0; i < GameField.Y_MAX; i++) {
            // Вывод оси У
            System.out.print(String.format("%-3d| ", i + 1));

            // Вывод поля
            for (int j = 0; j < GameField.Y_MAX; j++) {
                Cell cell = this.cells[i][j];
                Ship ship = cell.getShip();

                if (cell.hasShip() && ship.isDamaged(cell)) {
                    // Если в ячейке есть корабль и по нему попали
                    System.out.print(" x ");
                } else if (showShip && cell.hasShip()) {
                    // Если в ячейке есть живой корабль
                    System.out.print(" * ");
                } else if (cell.isHit()) {
                    // Ячейка по которой уже стреляли
                    System.out.print(" _ ");
                } else {
                    // Пустая не тронутая клетка
                    System.out.print(" . ");
                }
            }

            System.out.println("");
        }
    }

    /**
     * Метод выводит на экран собственное игровое поле
     */
    public void printOwnField() {
        System.out.println();
        this.printField(true);
        System.out.println();
    }

    /**
     * Метод выводит на экран чужое игровое поле
     */
    public void printEnemyField() {
        System.out.println("Вражеское поле");
        this.printField(false);
        System.out.println();
    }

    /**
     * Метод пытается случайным образом разместить корабль на игровом поле. Возвращает true - в случае успеха, false - если это невозможно
     * 
     * @param x int
     * @param y int
     * @param ship Ship
     * @return boolean
     */
    private boolean checkAndMarkPositionOnField(int x, int y, Ship ship) {
        // Индексы массива начинаютс с 0
        int xI = convertXCoordinateToIndex(x);
        int yI = convertYCoordinateToIndex(y);

        int size = ship.getSize();

        switch (ship.getOrientation()) {
            case Ship.ORIENTATION_HORIZONTAL:
                // Проверяем, что корабль помещается
                if (x + size > GameField.X_MAX || x < 1) {
                    return false;
                }

                // Проверяем клетку после корабля
                if (x > 1 && cells[xI - 1][yI].hasShip()) {
                    return false;
                }

                // Проверяем клетку перед кораблем
                if (x + size <= GameField.X_MAX && cells[xI + size][yI].hasShip()) {
                    return false;
                }

                // Сперва проанализируем клетки
                for (int i = xI; i < size + xI; i++) {
                    // Проверим позицию корабля - ячейка не должна содержать корабль или быть смежной
                    if (!cells[i][yI].isFree()) {
                        return false;
                    }

                    // Проверяем клетки сверху
                    if (y < GameField.Y_MAX && cells[i][yI + 1].hasShip()) {
                        return false;
                    }

                    // Проверяем клетки снизу
                    if (y > 1 && cells[i][yI - 1].hasShip()) {
                        return false;
                    }
                }

                // Помещаем корабль на игровое поле
                for (int i = xI; i < size + xI; i++) {
                    // Помечаем клетку занятой кораблем
                    cells[i][yI].setShip(ship);
                    // Запомним позицию сектора корабля
                    ship.placeShip(cells[i][yI]);

                    // Клетку сверху помеяаем недоступной
                    if (y < GameField.Y_MAX) {
                        cells[i][yI + 1].setNeighboring();
                        cells[i][yI].addNeighboringCell(cells[i][yI + 1]);
                    }

                    // Клетку снизу помечаем недоступной
                    if (y > 1) {
                        cells[i][yI - 1].setNeighboring();
                        cells[i][yI].addNeighboringCell(cells[i][yI - 1]);
                    }
                }

                // Клетку перед кораблем помечаем недоступной
                if (x > 1) {
                    cells[xI - 1][yI].setNeighboring();
                    cells[xI][yI].addNeighboringCell(cells[xI - 1][yI]);
                }

                // Клетку после корабля помечаем недоступной
                if (x + size <= GameField.X_MAX) {
                    cells[xI + size][yI].setNeighboring();
                    cells[xI + size - 1][yI].addNeighboringCell(cells[xI + size][yI]);
                }

                break;
            case Ship.ORIENTATION_VERTICAL:
                // Проверяем что корабль помещается
                if (y + size > GameField.Y_MAX || y < 1) {
                    return false;
                }

                // Проверяем что конец корабля не попадает в другой
                if (y > 1 && cells[xI][yI - 1].hasShip()) {
                    return false;
                }

                // Проверяем что нос корабля не попадает в другой
                if (y + size < GameField.Y_MAX && cells[xI][yI + size].hasShip()) {
                    return false;
                }

                // Проверим размещение всего корабля
                for (int i = yI; i < size + yI; i++) {
                    // Проверим что ячейка не занята кораблем и не является смежной
                    if (!cells[xI][i].isFree()) {
                        return false;
                    }

                    // Ячейки справа не должны содержать кораблей
                    if (x < GameField.X_MAX && cells[xI + 1][i].hasShip()) {
                        return false;
                    }

                    // Ячейки слева не должны содержать кораблей
                    if (x > 1 && cells[xI - 1][i].hasShip()) {
                        return false;
                    }
                }

                // Размещаем корабль на игровом поле
                for (int i = yI; i < size + yI; i++) {
                    // Помечаем саму ячейка, что она содержит корабль
                    cells[xI][i].setShip(ship);
                    // Пометим, что секция корабля в этой ячейке хранится
                    ship.placeShip(cells[xI][i]);

                    // Помечаем смежную ячейку справа
                    if (x < GameField.X_MAX) {
                        cells[xI + 1][i].setNeighboring();
                        cells[xI][i].addNeighboringCell(cells[xI + 1][i]);
                    }

                    // Помечаем смежную ячейку слева
                    if (x > 1) {
                        cells[xI - 1][i].setNeighboring();
                        cells[xI][i].addNeighboringCell(cells[xI - 1][i]);
                    }
                }

                // Помечаем смежную ячейку после конца корабля
                if (y > 1) {
                    cells[xI][yI - 1].setNeighboring();
                    cells[xI][yI].addNeighboringCell(cells[xI][yI - 1]);
                }

                // Помечаем смежную ячейку перед началом корабля
                if (y + size <= GameField.Y_MAX) {
                    cells[xI][yI + size].setNeighboring();
                    cells[xI][yI + size - 1].addNeighboringCell(cells[xI][yI + size]);
                }

                break;
        }

        return true;
    }
}
