package model.main;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.animals.utility.Living;
import model.properties.Encyclopedia;
import model.properties.GeneralConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Cell
{
    private final Map<Encyclopedia, List<Living>> biota = new ConcurrentHashMap<>();

    private final ReentrantLock lock = new ReentrantLock();
    @Getter
    private final int x;
    @Getter
    private final int y;

    private final static int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
    };


    public void addLivingBeing(Living living)
    {
        lock.lock();

        Encyclopedia livingBeing = Encyclopedia.getLivingBeing(living.getClass());
        biota.computeIfAbsent(livingBeing, _ -> new ArrayList<>());
        putInMap(living, biota);

        lock.unlock();
    }

    public void removeLivingBeing(Living living)
    {
        lock.lock();

        removeFromMap(living, biota);

        lock.unlock();
    }

    public boolean isEmpty()
    {
        return biota.isEmpty();
    }

    public List<Living> getLivingBeings(Encyclopedia livingBeing)
    {
        if (biota.get(livingBeing).isEmpty())
        {
            return null;
        }
        return new ArrayList<>(biota.get(livingBeing));
    }

    public Set<Encyclopedia> getAllLivingBeingTypes()
    {
        return biota.keySet();
    }

    public List<Cell> getNeighboringCells()
    {
        lock.lock();
        List<Cell> neighbors = new ArrayList<>();
        for (int[] direction : directions)
        {
            int newY = y + direction[0];
            int newX = x + direction[1];

            if (newY >= 0 && newY < GeneralConstants.HEIGHT &&
                    newX >= 0 && newX < GeneralConstants.LENGTH)
            {
                neighbors.add(Island.getCell(newX, newY));
            }
        }
        lock.unlock();
        return neighbors;
    }


    private void putInMap(Living living, Map<Encyclopedia, List<Living>> map)
    {
        List<? super Living> livings = map.get(Encyclopedia.getLivingBeing(living.getClass()));
        livings.add(living);
    }

    private void removeFromMap(Living living, Map<Encyclopedia, List<Living>> map)
    {
        List<Living> livings = map.get(Encyclopedia.getLivingBeing(living.getClass()));
        livings.remove(living);
    }


}
