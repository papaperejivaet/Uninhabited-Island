package model.main;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.animals.utility.Living;
import model.properties.Encyclopedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Cell
{
    private final Map<Encyclopedia, List<Living>> biota = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    @Getter
    private final int x;
    @Getter
    private final int y;



    public void addLivingBeing(Living living)
    {
        lock.lock();

        Encyclopedia livingBeing = Encyclopedia.getLivingBeing(living.getClass());

        biota.computeIfAbsent(livingBeing, _ -> new ArrayList<>());

        List<Living> livingBeings = biota.get(livingBeing);
        livingBeings.add(living);

        lock.unlock();
    }

    public void removeLivingBeing(Living living)
    {
        lock.lock();

        Encyclopedia livingBeing = Encyclopedia.getLivingBeing(living.getClass());
        List<Living> livingBeings = biota.get(livingBeing);
        livingBeings.remove(living);

        lock.unlock();
    }

    public boolean isEmpty()
    {
        return biota.isEmpty();
    }
}
