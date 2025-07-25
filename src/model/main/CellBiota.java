package model.main;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.Living;
import model.properties.Encyclopedia;
import model.properties.LivingBeingType;
import model.properties.Registry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class CellBiota
{
    private final Map<Encyclopedia, List<Living>> biota = new ConcurrentHashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    void addLivingBeing(Living living)
    {
        Encyclopedia livingBeing = Encyclopedia.getLivingBeing(living.getClass());

        biota.computeIfAbsent(livingBeing, _ -> new CopyOnWriteArrayList<>());
        putInMap(living, biota);
    }

    private void putInMap(Living living, Map<Encyclopedia, List<Living>> map)
    {
        List<Living> livings = map.get(Encyclopedia.getLivingBeing(living.getClass()));
        livings.add(living);
    }

    void removeLivingBeing(Living living)
    {
        removeFromMap(living, biota);
    }

    private void removeFromMap(Living living, Map<Encyclopedia, List<Living>> map)
    {
        Encyclopedia livingBeingType = Encyclopedia.getLivingBeing(living.getClass());
        List<Living> livings = map.get(livingBeingType);
        livings.remove(living);
        if (livings.isEmpty())
        {
            map.remove(livingBeingType);
        }
    }

    public List<Living> getLivingBeings(Encyclopedia livingBeing)
    {
        List<Living> list = biota.get(livingBeing);
        if (list == null || list.isEmpty())
        {
            return Collections.emptyList();
        }
        return new CopyOnWriteArrayList<>(list);
    }

    Set<Encyclopedia> getAllLivingBeingTypes()
    {
        return biota.keySet();
    }

    //Список Encyclopedia, из которых выбирать
    Living getRandomLiving(Set<Encyclopedia> typeSet)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int typeNumber;
        Encyclopedia currentType;
        List<Encyclopedia> typeList = new ArrayList<>(typeSet);
        List<Living> livings = null;

        for (int i = 0; i < typeList.size(); i++)
        {
            typeNumber = random.nextInt(typeList.size());
            currentType = typeList.get(typeNumber);

            if ((livings = biota.get(currentType)) != null && !livings.isEmpty())
            {
                break;
            }
        }
        if (livings == null || livings.isEmpty())
        {
            return null;
        }

        int livingNumber = random.nextInt(livings.size());
        return livings.get(livingNumber);

    }

    String getMaxAmount()
    {
        Encyclopedia type = Collections.max(
                biota.entrySet(),
                Map.Entry.comparingByValue(
                        Comparator.comparingInt(List::size)
                ))
                .getKey();
        return Registry.getDisplay(type);
    }

    //Для Drawer
    String getCharOfMaxAmount(LivingBeingType livingBeingType)
    {
        Class<? extends Living> type = livingBeingType.getType();
        Optional<Map.Entry<Encyclopedia, List<Living>>> maxOptional = biota.entrySet().stream()
                .filter(entry -> type.isAssignableFrom(entry.getKey().getType()))
                .max(Comparator.comparingInt(entry -> entry.getValue().size()));

        if (maxOptional.isPresent())
        {
            Encyclopedia maxType = maxOptional.get().getKey();
            return Registry.getDisplay(maxType);
        }

        return "  ";
    }

    boolean containsAny(Set<Encyclopedia> typeSet)
    {
        for (Encyclopedia type : typeSet)
        {
            if (biota.containsKey(type))
            {
                return true;
            }
        }

        return false;
    }



}
