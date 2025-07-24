package model.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import model.Living;
import model.properties.Encyclopedia;
import util.GeneralConstants;
import model.properties.LivingBeingType;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@EqualsAndHashCode
public class Cell implements Comparable<Cell>
{
    CellBiota biota = new CellBiota();

    private final ReentrantLock lock = new ReentrantLock();
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

    public boolean containsAny(Set<Encyclopedia> typeSet)
    {
        return biota.containsAny(typeSet);
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
}
