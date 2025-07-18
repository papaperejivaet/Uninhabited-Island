package model.properties;


import controller.JsonHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LifeFormRegistry
{
    private static final Map<Encyclopedia, LifeFormInfo> livingBeingInfo = JsonHandler.parseLifeFormInfo("lifeform_info.json");

    private static final Map<Encyclopedia, Map<Encyclopedia, Integer>> eatingChances =
            JsonHandler.parseCodependentData("eating_chances.json", Integer.class);

    private static final Map<Encyclopedia, Integer> naturalDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> eatenDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> hungerDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> accidentDeaths = new ConcurrentHashMap<>();

    public static Double getWeight(Encyclopedia type)
    {
        LifeFormInfo lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getWeight();
    }

    public static Integer getMaxCellAmount(Encyclopedia type)
    {
        LifeFormInfo lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getMaxCellAmount();
    }

    public static Integer getMaxSpeed(Encyclopedia type)
    {
        LifeFormInfo lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getMaxSpeed();
    }

    public static Double getMaxSaturationLevel(Encyclopedia type)
    {
        LifeFormInfo lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getMaxSaturation();
    }

    public static Double getMaxAge(Encyclopedia type)
    {
        LifeFormInfo lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getMaxAge();
    }

    public static Integer getEatingChances(Encyclopedia predator, Encyclopedia prey)
    {
        Map<Encyclopedia, Integer> predatorEatingChances = eatingChances.get(predator);
        return predatorEatingChances.get(prey);
    }

    public static void registerDeath(Encyclopedia type, DeathCause deathCause)
    {
        switch (deathCause)
        {
            case NATURAL -> registerDeath(type, naturalDeaths);
            case EATEN -> registerDeath(type, eatenDeaths);
            case HUNGER -> registerDeath(type, hungerDeaths);
            case ACCIDENT -> registerDeath(type, accidentDeaths);
        }
    }

    private static void registerDeath(Encyclopedia type, Map<Encyclopedia, Integer> deathRegistry)
    {
        if (!deathRegistry.containsKey(type))
        {
            deathRegistry.put(type, 1);
            return;
        }
        Integer deathCount = deathRegistry.get(type);
        deathRegistry.put(type, ++deathCount);
    }

    public static Integer getStartAmount(Encyclopedia type)
    {
        double startAmount = ((double) (GeneralConstants.HEIGHT + GeneralConstants.LENGTH) / 1000) * getMaxCellAmount(type);
        return (int) Math.round(startAmount);
    }

}
