package model.main;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.properties.DeathCause;
import model.properties.Encyclopedia;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Statistics
{
    private static final Map<Encyclopedia, Integer> naturalDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> eatenDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> hungerDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> accidentDeaths = new ConcurrentHashMap<>();

    private static final Map<Encyclopedia, Integer> bredCount = new ConcurrentHashMap<>();

    private static final Map<Encyclopedia, Integer> ateCount = new ConcurrentHashMap<>();



    public static void registerDeath(Encyclopedia type, DeathCause deathCause)
    {
        switch (deathCause)
        {
            case NATURAL -> register(type, naturalDeaths);
            case EATEN -> register(type, eatenDeaths);
            case HUNGER -> register(type, hungerDeaths);
            case ACCIDENT -> register(type, accidentDeaths);
        }
    }

    public static void registerBreeding(Encyclopedia type)
    {
        register(type, bredCount);
    }

    public static void registerConsumption(Encyclopedia type)
    {
        register(type, ateCount);
    }

    private static void register(Encyclopedia type, Map<Encyclopedia, Integer> statisticMap)
    {
        if (!statisticMap.containsKey(type))
        {
            statisticMap.put(type, 1);
            return;
        }
        Integer count = statisticMap.get(type);
        statisticMap.put(type, ++count);
    }
}
