package model.properties;


import util.GeneralConstants;
import util.JsonHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Хранилище конфигурационной информации обо всех формах жизни.
 * Загружает данные из JSON один раз при старте и предоставляет методы доступа.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Registry
{
    private static final Map<Encyclopedia, InfoDTO> livingBeingInfo = JsonHandler.parseLifeFormInfo(GeneralConstants.INFO_PATH);


    /**
     * Возвращает вес существа.
     * @param type вид существа
     * @return вес
     */
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

    /**
     * Возвращает шанс поедания одной формы жизни другой.
     *
     * @param predator хищник
     * @param prey потенциальная жертва
     * @return шанс (0–100)
     */
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


    /**
     * Вычисляет стартовое количество особей на острове, на основе площади острова и лимита на клетку.
     *
     * @param type тип существа
     * @return стартовое количество
     */
    public static Integer getStartAmount(Encyclopedia type)
    {
        double startAmount = ((double) (GeneralConstants.HEIGHT + GeneralConstants.LENGTH) / 100) * getMaxCellAmount(type) * 5;
        return (int) Math.round(startAmount);
    }

}
