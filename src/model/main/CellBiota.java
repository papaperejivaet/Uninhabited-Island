package model.main;

import lombok.AccessLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import model.Living;

import model.properties.DeathCause;
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

    @Getter(AccessLevel.PACKAGE)
    ReentrantLock lock = new ReentrantLock();

    void addLivingBeing(Living living)
    {
        Encyclopedia livingBeing = Encyclopedia.getLivingBeing(living.getClass());

        biota.computeIfAbsent(livingBeing, k -> new CopyOnWriteArrayList<>());
        putInMap(living);
    }

    private void putInMap(Living living)
    {
        List<Living> livings = biota.get(Encyclopedia.getLivingBeing(living.getClass()));

        if (livings.size() > Registry.getMaxCellAmount(Encyclopedia.getLivingBeing(living.getClass())))
        {
            living.die(DeathCause.ACCIDENT);
            return;
        }
        livings.add(living);
    }



    public void removeLivingBeing(Living living)
    {

        Encyclopedia livingBeingType = Encyclopedia.getLivingBeing(living.getClass());
        List<Living> livings = biota.get(livingBeingType);

        if (livings == null)
        {
            return;
        }

        if (!livings.isEmpty())
        {
            livings.remove(living);
        }

        if (livings.isEmpty())
        {
            biota.remove(livingBeingType);
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


    Living getRandomLiving(Set<Encyclopedia> baseSet, Living exception) {
        // 1) Скопировать базовый набор и убрать текущего, если надо
        Set<Encyclopedia> typeSet = new HashSet<>(baseSet);
        if (exception != null) {
            typeSet.remove(Encyclopedia.getLivingBeing(exception.getClass()));
        }

        // 2) Если после исключения нет типов — сразу null
        if (typeSet.isEmpty()) {
            return null;
        }

        // 3) Перемешать список типов
        List<Encyclopedia> typeList = new ArrayList<>(typeSet);
        Collections.shuffle(typeList, ThreadLocalRandom.current());

        // 4) Перебрать каждый тип
        for (Encyclopedia type : typeList) {
            List<Living> livings = biota.get(type);
            // 5) Пропускаем, если нет ни одного
            if (livings == null || livings.isEmpty()) {
                continue;
            }
            // 6) Безопасный выбор случайного индекса
            int idx = ThreadLocalRandom.current().nextInt(livings.size());
            return livings.get(idx);
        }

        // 7) Ничего не нашли
        return null;
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

    int getAmountOf(Encyclopedia type)
    {
        List<Living> livings = biota.get(type);
        if (livings != null)
        {
            return livings.size();
        }
        return 0;
    }

}
