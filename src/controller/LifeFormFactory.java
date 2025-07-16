package controller;

import exceptions.LifeFormCreatingException;
import model.animals.utility.Living;
import model.properties.Encyclopedia;
import model.properties.LifeFormRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


public class LifeFormFactory
{

    public static Living create(Encyclopedia livingBeing, double age, double saturationLevel)
    {
        try
        {
            Constructor<?> constructor = livingBeing.getType().getConstructor(Double.class, Double.class);
            return (Living) constructor.newInstance(age, saturationLevel);
        }
        catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            throw new LifeFormCreatingException("Произошла ошибка при создании формы жизни" + Arrays.toString(e.getStackTrace()));
        }
    }

    public static Living createNewborn(Encyclopedia livingBeing)
    {
        return create(livingBeing, 0.0, LifeFormRegistry.getMaxSaturationAmount(livingBeing));
    }
}
