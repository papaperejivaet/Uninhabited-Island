package model.animals.herbivore;

import model.Consumable;
import model.main.Cell;

public class Caterpillar extends Herbivore
{

    public Caterpillar(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }

    @Override
    public boolean increaseSaturationLevel(Consumable food)
    {
        return super.increaseSaturationLevel(food);
    }
}
