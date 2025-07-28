package model.animals.herbivore;

import model.Consumable;
import model.Living;
import model.main.Cell;
import model.properties.Encyclopedia;
import model.properties.LivingBeingType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Duck extends Herbivore
{

    public Duck(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }

    @Override
    public Set<Encyclopedia> additionalFood()
    {
        return new HashSet<>(List.of(Encyclopedia.CATERPILLAR));
    }
}
