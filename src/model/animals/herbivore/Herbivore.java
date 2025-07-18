package model.animals.herbivore;

import model.main.Cell;
import model.animals.utility.Animal;

//Травоядное
public abstract class Herbivore extends Animal
{
    protected Herbivore(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }
}
