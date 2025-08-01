package model.main;


import lombok.Getter;
import model.Living;
import model.properties.Encyclopedia;
import util.GeneralConstants;
import model.properties.LivingBeingType;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Представляет клетку острова. Содержит всех обитателей в данной координате,
 * знает свои координаты и соседние клетки.
 * Обеспечивает потокобезопасную работу с внутренней флорой и фауной (биотой) через {@link CellBiota}.
 */
public class Cell implements Comparable<Cell>
{
    CellBiota biota = new CellBiota();

    @Getter
    private List<Cell> neighboringCells;

    private boolean isFound = false;

    @Getter
    private final int x;
    @Getter
    private final int y;


    private static final int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
    };


    public Cell(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Добавляет живое существо в текущую клетку.
     * Делегирует добавление объекту {@link CellBiota}.
     *
     * @param living добавляемое существо
     */
    public void addLivingBeing(Living living)
    {
        biota.addLivingBeing(living);
    }

    /**
     * Удаляет живое существо из клетки.
     *
     * @param living удаляемое существо
     */
    public void removeLivingBeing(Living living)
    {
        biota.removeLivingBeing(living);
    }

    /**
     * Возвращает список всех существ определённого вида, находящихся в клетке.
     *
     * @param livingBeing вид из {@link Encyclopedia}
     * @return копия списка живых существ этого вида
     */
    public List<Living> getLivingBeings(Encyclopedia livingBeing)
    {
        return biota.getLivingBeings(livingBeing);
    }

    /**
     * Возвращает множество всех видов живых существ, находящихся в клетке.
     *
     * @return множество типов из {@link Encyclopedia}
     */
    public Set<Encyclopedia> getAllLivingBeingTypes()
    {
        return biota.getAllLivingBeingTypes();
    }

    /**
     * Возвращает случайное живое существо из заданных типов, исключая переданное.
     *
     * @param typeSet допустимые типы (например, только травоядные)
     * @param exception существо, которое нельзя выбрать (может быть null)
     * @return случайное существо или null, если нет подходящих
     */
    public Living getRandomLiving(Set<Encyclopedia> typeSet, Living exception)
    {
        return biota.getRandomLiving(typeSet, exception);
    }

    /**
     * Проверяет, содержит ли клетка хотя бы одно существо из указанной категории (животное, растение).
     *
     * @param livingBeingType категория (ANIMAL, HERBIVORE, CARNIVORE или PLANT)
     * @return true, если в клетке есть хотя бы один представитель
     */
    public boolean containsAny(LivingBeingType livingBeingType)
    {
        Set<Encyclopedia> typeSet = livingBeingType.getMembers();
        return biota.containsAny(typeSet);
    }

    /**
     * Возвращает символ (строку длиной 2) самого многочисленного представителя указанной категории.
     * Используется для отрисовки визуального представления острова.
     *
     * @param livingBeingType категория (ANIMAL или PLANT)
     * @return строка-символ или "  ", если в клетке никого нет
     */
    public String getCharOfMaxAmount(LivingBeingType livingBeingType)
    {
        return biota.getCharOfMaxAmount(livingBeingType);
    }

    /**
     * Ищет и сохраняет ссылки на все соседние клетки.
     * Используется при построении карты. Повторные вызовы игнорируются.
     */
    public void findNeighboringCells()
    {
        if (isFound) return;

        List<Cell> neighbors = new ArrayList<>();
        for (int[] direction : directions)
        {
            int newY = y + direction[0];
            int newX = x + direction[1];
            checkAndAddNeighbors(neighbors, newX, newY);
        }
        neighboringCells = Collections.unmodifiableList(neighbors);
        isFound = true;
    }


    private void checkAndAddNeighbors(List<Cell> neighbors, int x, int y)
    {
        if (y >= 0 && y < GeneralConstants.HEIGHT &&
                x >= 0 && x < GeneralConstants.LENGTH)
        {
            neighbors.add(Island.getCell(x, y));
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Cell o)
    {
        if (this.y != o.y)
        {
            return Integer.compare(o.y, this.y);
        }
        else
        {
            return Integer.compare(o.x, this.x);
        }
    }

    /**
     * Возвращает объект блокировки, связанный с данной клеткой.
     * Используется для потокобезопасного перемещения и изменения состояния.
     *
     * @return {@link ReentrantLock}
     */
    public ReentrantLock getLock()
    {
        return biota.getLock();
    }

    @Override
    public String toString()
    {
        return "Cell: " + x + "x " + y + "y ";
    }
}
