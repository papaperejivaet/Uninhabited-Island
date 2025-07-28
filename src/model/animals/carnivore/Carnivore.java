package model.animals.carnivore;
//Плотоядное

import model.Consumable;
import model.main.Cell;
import model.animals.Animal;
import model.properties.LivingBeingType;


public abstract class Carnivore extends Animal
{


    protected Carnivore(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }

    @Override
    protected Consumable findFood()
    {
        //Т.к. Animal реализует Consumable
        return (Consumable) currentCell.getRandomLiving(LivingBeingType.ANIMAL.getMembers(), this);
    }
}
