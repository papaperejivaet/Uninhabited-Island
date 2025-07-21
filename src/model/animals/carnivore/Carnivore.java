package model.animals.carnivore;
//Плотоядное

import model.Consumable;
import model.main.Cell;
import model.animals.Animal;
import model.properties.Encyclopedia;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Carnivore extends Animal
{
    protected static List<Encyclopedia> foodTypes = Encyclopedia.getByType(Animal.class);

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
