package model.animals.carnivore;
//Плотоядное

import model.main.Cell;
import model.animals.utility.Animal;

public abstract class Carnivore extends Animal
{
    protected Carnivore(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }
}
