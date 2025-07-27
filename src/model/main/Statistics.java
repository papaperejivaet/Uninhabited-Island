package model.main;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import model.properties.DeathCause;
import model.properties.Encyclopedia;
import model.properties.LivingBeingType;
import util.GeneralConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Statistics
{
    @Getter
    private static int currentCycleNumber = 0;

    private static final Map<Encyclopedia, Integer> naturalDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> eatenDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> hungerDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> accidentDeaths = new ConcurrentHashMap<>();

    private static final Map<Encyclopedia, Integer> bredCount = new ConcurrentHashMap<>();

    private static final Map<Encyclopedia, Integer> ateCount = new ConcurrentHashMap<>();

    private static final Map<Cell, Boolean> animalContainment = new ConcurrentHashMap<>();
    private static final Map<Cell, Boolean> carnivoreContainment = new ConcurrentHashMap<>();
    private static final Map<Cell, Boolean> herbivoreContainment = new ConcurrentHashMap<>();
    private static final Map<Cell, Boolean> plantContainment = new ConcurrentHashMap<>();





    public static void registerDeath(Encyclopedia type, DeathCause deathCause)
    {
        Map<Encyclopedia, Integer> deathMap = getDeathMap(deathCause);
        register(type, deathMap);
    }

    public static void registerBreeding(Encyclopedia type)
    {
        register(type, bredCount);
    }

    public static void registerConsumption(Encyclopedia type)
    {
        register(type, ateCount);
    }

    public static void confirmAvailability(Cell cell)
    {
        boolean containsAny;

        for (LivingBeingType livingBeingType : LivingBeingType.values())
        {
            containsAny = cell.containsAny(livingBeingType);
            addInContainment(cell, livingBeingType, containsAny);
        }
    }

    private static void addInContainment(Cell cell, LivingBeingType type, Boolean containsAny)
    {
        switch (type)
        {
            case ANIMAL -> animalContainment.put(cell, containsAny);
            case CARNIVORE -> carnivoreContainment.put(cell, containsAny);
            case HERBIVORE -> herbivoreContainment.put(cell, containsAny);
            case PLANT -> plantContainment.put(cell, containsAny);
        }
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

    protected static boolean checkConditions()
    {
        return  animalContainment.containsValue(true) && carnivoreContainment.containsValue(true) &&
                herbivoreContainment.containsValue(true) && plantContainment.containsValue(true) &&
                currentCycleNumber < GeneralConstants.MAX_CYCLES;
    }

    protected static void nextCycle()
    {
        currentCycleNumber++;
    }

    public static int getBreedingCount()
    {
        int counter = 0;
        for (Map.Entry<Encyclopedia, Integer> entry : bredCount.entrySet())
        {
            counter += entry.getValue();
        }
        return counter;
    }
    public static int getAteCount()
    {
        int counter = 0;
        for (Map.Entry<Encyclopedia, Integer> entry : ateCount.entrySet())
        {
            counter += entry.getValue();
        }
        return counter;
    }

    public static int getDeathCount(DeathCause cause)
    {
        int counter = 0;

        Map<Encyclopedia, Integer> deathMap = getDeathMap(cause);

        for (Map.Entry<Encyclopedia, Integer> entry : deathMap.entrySet())
        {
            counter += entry.getValue();
        }

        return counter;
    }

    private static Map<Encyclopedia, Integer> getDeathMap(DeathCause cause)
    {
        return switch (cause)
        {
            case NATURAL -> naturalDeaths;
            case EATEN -> eatenDeaths;
            case HUNGER -> hungerDeaths;
            case ACCIDENT -> accidentDeaths;
        };
    }

}
