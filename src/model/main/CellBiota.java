package model.main;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.Living;
import model.properties.Encyclopedia;
import model.properties.Registry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class CellBiota
{
    private final Map<Encyclopedia, List<Living>> biota = new ConcurrentHashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    public void addLivingBeing(Living living)
    {

        Encyclopedia livingBeing = Encyclopedia.getLivingBeing(living.getClass());

        lock.lock();

        biota.computeIfAbsent(livingBeing, _ -> new ArrayList<>());
        putInMap(living, biota);

        lock.unlock();
    }

    private void putInMap(Living living, Map<Encyclopedia, List<Living>> map)
    {
        List<Living> livings = map.get(Encyclopedia.getLivingBeing(living.getClass()));
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
        lock.lock();

        if (biota.get(livingBeing).isEmpty())
        {
            return null;
        }
        ArrayList<Living> livings = new ArrayList<>(biota.get(livingBeing));

        lock.unlock();

        return livings;
    }

    protected Set<Encyclopedia> getAllLivingBeingTypes()
    {
        return biota.keySet();
    }

    //Список Encyclopedia, из которых выбирать
    protected Living getRandomLiving(List<Encyclopedia> typeList)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int typeNumber;
        Encyclopedia currentType;
        List<Living> livings;

        do
        {
            typeNumber = random.nextInt(typeList.size());
            currentType = typeList.get(typeNumber);
        }
        while((livings = biota.get(currentType)) == null);

        int livingNumber = random.nextInt(livings.size());
        return livings.get(livingNumber);

    }

    char getMaxAmount()
    {
        Encyclopedia type = Collections.max(
                biota.entrySet(),
                Map.Entry.comparingByValue(
                        Comparator.comparingInt(List::size)
                ))
                .getKey();
        return Registry.getDisplay(type);
    }

    char getMaxAmountOfType(Class<? extends Living> type)
    {
        Optional<Map.Entry<Encyclopedia, List<Living>>> maxOptional = biota.entrySet().stream()
                .filter(entry -> type.isInstance(entry.getKey().getType()))
                .max(Comparator.comparingInt(entry -> entry.getValue().size()));

        if (maxOptional.isPresent())
        {
            Encyclopedia maxType = maxOptional.get().getKey();
            return Registry.getDisplay(maxType);
        }

        return '?';

    }
}
