package model.main;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.Living;
import model.properties.Encyclopedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CellBiota
{
    private final Map<Encyclopedia, List<Living>> biota = new ConcurrentHashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    public void addLivingBeing(Living living)
    {
        lock.lock();

        Encyclopedia livingBeing = Encyclopedia.getLivingBeing(living.getClass());
        biota.computeIfAbsent(livingBeing, _ -> new ArrayList<>());
        putInMap(living, biota);

        lock.unlock();
    }

    private void putInMap(Living living, Map<Encyclopedia, List<Living>> map)
    {
        List<? super Living> livings = map.get(Encyclopedia.getLivingBeing(living.getClass()));
        livings.add(living);
    }

    protected void removeLivingBeing(Living living)
    {
        lock.lock();

        removeFromMap(living, biota);

        lock.unlock();
    }

    private void removeFromMap(Living living, Map<Encyclopedia, List<Living>> map)
    {
        List<Living> livings = map.get(Encyclopedia.getLivingBeing(living.getClass()));
        livings.remove(living);
    }

    protected List<Living> getLivingBeings(Encyclopedia livingBeing)
    {
        if (biota.get(livingBeing).isEmpty())
        {
            return null;
        }
        return new ArrayList<>(biota.get(livingBeing));
    }

    protected Set<Encyclopedia> getAllLivingBeingTypes()
    {
        return biota.keySet();
    }

    protected Living getRandomLiving(Class<? extends Living> type)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int encyclopediaNumber;
        // дописать

    }
}
