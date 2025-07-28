package model.animals.herbivore;

import model.Consumable;
import model.main.Cell;
import model.animals.Animal;
import model.properties.Encyclopedia;
import model.properties.LivingBeingType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//Травоядное
public abstract class Herbivore extends Animal
{

    protected Herbivore(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }

    @Override
    protected Consumable findFood()
    {
        Set<Encyclopedia> foodTypes = new HashSet<>(LivingBeingType.PLANT.getMembers());
        Set<Encyclopedia> additionalSet = additionalFood();
        foodTypes.addAll(additionalSet);
        //Т.к. Plant реализует Consumable
        return (Consumable) currentCell.getRandomLiving(foodTypes, this);
    }

    public Set<Encyclopedia> additionalFood()
    {
        return Collections.emptySet();
    }
}
