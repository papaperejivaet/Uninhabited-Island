package model.animals.carnivore;
//Плотоядное

import model.Consumable;
import model.main.Cell;
import model.animals.Animal;
import model.properties.Encyclopedia;
import model.properties.LivingBeingType;

import java.util.Set;

public abstract class Carnivore extends Animal
{
    protected static Set<Encyclopedia> foodTypes = LivingBeingType.ANIMAL.getMembers();

    protected Carnivore(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }

    @Override
    protected Consumable findFood()
    {
        //Т.к. Animal реализует Consumable
        return (Consumable) currentCell.getRandomLiving(foodTypes);
    }
}
