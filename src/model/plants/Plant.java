package model.plants;

import model.Cell;
import model.animals.utility.Consumable;
import model.animals.utility.LifeForm;
import model.animals.utility.Sun;
import java.util.concurrent.ThreadLocalRandom;


public class Plant extends LifeForm
{


    protected Plant(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }

    @Override
    public boolean consume(Consumable food, ThreadLocalRandom random)
    {
        lock.lock();

        if (food instanceof Sun sun)
        {
            return increaseSaturationLevel(sun);
        }
        lock.unlock();
        return false;
    }
}
