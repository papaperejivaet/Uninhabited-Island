package model.animals.herbivore;

import model.Consumable;
import model.Living;
import model.main.Cell;
import model.animals.Animal;
import model.plants.Plant;
import model.properties.Encyclopedia;

import java.util.List;

//Травоядное
public abstract class Herbivore extends Animal
{
    protected static List<Encyclopedia> foodTypes = Encyclopedia.getByType(Plant.class);

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
