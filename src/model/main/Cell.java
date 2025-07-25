package model.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import model.Living;
import model.properties.Encyclopedia;
import util.GeneralConstants;
import model.properties.LivingBeingType;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

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

    private final char[][] biggestAmount = new char[2][2];

    public Cell(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void addLivingBeing(Living living)
    {
        biota.addLivingBeing(living);
    }

    public void removeLivingBeing(Living living)
    {
        biota.removeLivingBeing(living);
    }

    public List<Living> getLivingBeings(Encyclopedia livingBeing)
    {
        return biota.getLivingBeings(livingBeing);
    }

    public Set<Encyclopedia> getAllLivingBeingTypes()
    {
        return biota.getAllLivingBeingTypes();
    }

    public Living getRandomLiving(Set<Encyclopedia> typeList)
    {
        return biota.getRandomLiving(typeList);
    }

    public boolean containsAny(LivingBeingType livingBeingType)
    {
        Set<Encyclopedia> typeSet = livingBeingType.getMembers();
        boolean contains = biota.containsAny(typeSet);
        System.out.println(this + ": " + contains);
        return contains;
    }


    public String getCharOfMaxAmount(LivingBeingType livingBeingType)
    {
        return biota.getCharOfMaxAmount(livingBeingType);
    }

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

    @Override
    public String toString()
    {
        return "Cell: " + x + "x " + y + "y ";
    }
}
