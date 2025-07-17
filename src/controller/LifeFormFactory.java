package controller;

import exceptions.LifeFormCreatingException;
import model.Cell;
import model.animals.utility.Living;
import model.properties.Encyclopedia;
import model.properties.LifeFormRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;


public class LifeFormFactory
{
    ThreadLocalRandom random = ThreadLocalRandom.current();
    public static Living create(Encyclopedia livingBeing, Cell cell, double age, double saturationLevel)
    {
        try
        {
            Constructor<?> constructor = livingBeing.getType().getConstructor(Cell.class, double.class, double.class);
            return (Living) constructor.newInstance(cell, age, saturationLevel);
        }
        catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            throw new LifeFormCreatingException("Произошла ошибка при создании формы жизни" + Arrays.toString(e.getStackTrace()));
        }
    }

    public static Living createNewborn(Encyclopedia livingBeing, Cell cell)
    {
        return create(livingBeing, cell, 0.0, LifeFormRegistry.getMaxSaturationLevel(livingBeing));
    }
}
