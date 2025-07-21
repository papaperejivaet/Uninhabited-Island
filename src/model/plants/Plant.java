package model.plants;

import model.main.Cell;
import model.Consumable;
import model.LifeForm;
import model.Sun;
import java.util.concurrent.ThreadLocalRandom;


public class Plant extends LifeForm
{


    protected Plant(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }


    @Override
    protected Consumable findFood()
    {
        return Sun.getInstance();
    }
}
