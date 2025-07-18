package model.animals.utility;


import model.main.Cell;


public abstract class Animal extends LifeForm implements Mobile
{
    protected Animal(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }

    @Override
    public void move(Cell cell)
    {
        currentCell.removeLivingBeing(this);
        currentCell = cell;
        currentCell.addLivingBeing(this);
        x = cell.getX();
        y = cell.getY();
    }

}
