package model.animals.herbivore;

import model.Consumable;
import model.main.Cell;
import model.animals.Animal;
import model.properties.Encyclopedia;
import model.properties.LivingBeingType;

import java.util.Set;

//Травоядное
public abstract class Herbivore extends Animal
{
    protected static Set<Encyclopedia> foodTypes = LivingBeingType.PLANT.getMembers();

    protected Herbivore(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }

    @Override
    protected Consumable findFood()
    {
        //Т.к. Plant реализует Consumable
        return (Consumable) currentCell.getRandomLiving(foodTypes);
    }
}
