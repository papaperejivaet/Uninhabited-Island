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

    public static void confirmAvailability(Cell cell)
    {
        boolean containsAny;

        for (LivingBeingType livingBeingType : LivingBeingType.values())
        {
            containsAny = cell.containsAny(livingBeingType.getMembers());
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
        return animalContainment.containsValue(false) && carnivoreContainment.containsValue(false) &&
                herbivoreContainment.containsValue(false) && plantContainment.containsValue(false) &&
                currentCycleNumber < GeneralConstants.MAX_CYCLES;
    }

    protected static void nextCycle()
    {
        currentCycleNumber++;
    }


}
