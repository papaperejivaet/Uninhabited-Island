package model.properties;


import controller.JsonHandler;

import java.util.Map;


public final class LifeFormRegistry
{
    private static final Map<Encyclopedia, LifeFormInfo> livingBeingInfo = JsonHandler.parseLifeFormInfo("lifeform_info.json");

    private static final Map<Encyclopedia, Map<Encyclopedia, Integer>> eatingChances =
            JsonHandler.parseCodependentData("eating_chances.json", Integer.class);


    private LifeFormRegistry(){}

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

    public static Double getMaxSaturationAmount(Encyclopedia type)
    {
        LifeFormInfo lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getSaturation();
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

}
