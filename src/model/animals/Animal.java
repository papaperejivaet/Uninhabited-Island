package model.animals;


import lombok.Getter;
import model.LifeForm;
import model.Mobile;
import model.main.Cell;
import model.properties.DeathCause;
import model.properties.Registry;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Getter
public abstract class Animal extends LifeForm implements Mobile
{
    boolean hasMoved;

    protected Animal(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
        hasMoved = false;
    }


    @Override
    public void move()
    {
        Cell newCell = getNewCell();

        if (newCell.equals(currentCell))
        {
            hasMoved = true;
            return;
        }

        currentCell.removeLivingBeing(this);
        currentCell = newCell;
        currentCell.addLivingBeing(this);
        x = currentCell.getX();
        y = currentCell.getY();

        hasMoved = true;
    }

    @Override
    public void die(DeathCause cause)
    {
        super.die(cause);
        Registry.registerDeath(livingBeingType, cause);
    }

    @Override
    public void grow()
    {
        super.grow();
        hasMoved = false;
        hasConsumed = false;
    }


    private Cell getNewCell()
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int stepsAmount = random.nextInt(maxSpeed);
        List<Cell> neighboringCells;
        int cellNumber;
        Cell newCell = currentCell;

        for (int i = 0; i < stepsAmount; i++)
        {
            neighboringCells = currentCell.getNeighboringCells();
            cellNumber = random.nextInt(neighboringCells.size());
            newCell = neighboringCells.get(cellNumber);
        }

        return newCell;
    }


}
