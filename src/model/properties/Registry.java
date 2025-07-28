package model.properties;


import util.GeneralConstants;
import util.JsonHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Registry
{
    private static final Map<Encyclopedia, InfoDTO> livingBeingInfo = JsonHandler.parseLifeFormInfo("C:\\Users\\ndpee\\IdeaProjects\\Uninhabited Island\\src\\info.json");



    public static Double getWeight(Encyclopedia type)
    {
        InfoDTO lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getWeight();
    }

    public static Integer getMaxCellAmount(Encyclopedia type)
    {
        InfoDTO lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getMaxCellAmount();
    }

    public static Integer getMaxSpeed(Encyclopedia type)
    {
        InfoDTO lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getMaxSpeed();
    }

    public static Double getMaxSaturationLevel(Encyclopedia type)
    {
        InfoDTO lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getMaxSaturation();
    }

    public static Double getMaxAge(Encyclopedia type)
    {
        InfoDTO lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getMaxAge();
    }

    public static Integer getEatingChances(Encyclopedia predator, Encyclopedia prey)
    {
        InfoDTO lifeFormInfo = livingBeingInfo.get(predator);
        Map<String, Integer> diet = lifeFormInfo.getDiet();

        return diet.get(prey.toString());
    }

    public static String getDisplay(Encyclopedia type)
    {
        InfoDTO lifeFormInfo = livingBeingInfo.get(type);
        return lifeFormInfo.getDisplay();
    }



    public static Integer getStartAmount(Encyclopedia type)
    {
        double startAmount = ((double) (GeneralConstants.HEIGHT + GeneralConstants.LENGTH) / 100) * getMaxCellAmount(type) * 5;
        return (int) Math.round(startAmount);
    }

}
