package model.animals.herbivore;


import model.main.Cell;
import model.properties.Encyclopedia;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hog extends Herbivore
{
    public Hog(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }


    @Override
    public Set<Encyclopedia> additionalFood()
    {
        return new HashSet<>(List.of(Encyclopedia.CATERPILLAR, Encyclopedia.MOUSE));
    }
}
