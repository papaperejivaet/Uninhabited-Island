package model.animals;


import model.LifeForm;
import model.Mobile;
import model.main.Cell;
import model.main.Statistics;
import model.properties.DeathCause;
import model.properties.Registry;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public abstract class Animal extends LifeForm implements Mobile
{
    boolean hasMoved;

    protected Animal(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
        hasMoved = false;
    }


    @Override
    public void move() {
        if (hasMoved) return;

        Cell from = currentCell;
        Cell to = getNewCell();

        // Порядок захвата локов
        Cell first = System.identityHashCode(from) < System.identityHashCode(to) ? from : to;
        Cell second = (from == first) ? to : from;

        ReentrantLock firstLock = first.getLock();
        ReentrantLock secondLock = second.getLock();

        boolean firstAcquired = false;
        boolean secondAcquired = false;

        try {
            // Попробовать захватить первый лок
            firstAcquired = firstLock.tryLock(10, TimeUnit.MICROSECONDS);
            if (!firstAcquired) return;

            // Попробовать захватить второй лок
            secondAcquired = secondLock.tryLock(10, TimeUnit.MICROSECONDS);
            if (!secondAcquired) return;

            // Переместиться
            if (to.getAmountOf(livingBeingType) == Registry.getMaxCellAmount(livingBeingType))
            {
                die(DeathCause.ACCIDENT);
                return;
            }
            currentCell.removeLivingBeing(this);
            currentCell = to;
            currentCell.addLivingBeing(this);
            x = currentCell.getX();
            y = currentCell.getY();
            hasMoved = true;
            decreaseSaturation();

        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
        finally
        {
            if (firstAcquired) firstLock.unlock();
            if (secondAcquired) secondLock.unlock();
        }
    }

    @Override
    public void die(DeathCause cause)
    {
        super.die(cause);
        Statistics.registerDeath(livingBeingType, cause);
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
        int maxSpeed = Registry.getMaxSpeed(livingBeingType) + 1;
        int stepsAmount = random.nextInt(maxSpeed);
        List<Cell> neighboringCells;
        int cellNumber;
        Cell newCell = currentCell;

        for (int i = 0; i < stepsAmount; i++)
        {
            neighboringCells = newCell.getNeighboringCells();
            cellNumber = random.nextInt(neighboringCells.size());
            newCell = neighboringCells.get(cellNumber);
        }

        return newCell;
    }

    @Override
    public boolean consume()
    {
        boolean hasConsumed = super.consume();
        if (hasConsumed)
        {
            Statistics.registerConsumption(livingBeingType);
        }
        return hasConsumed;
    }

    public boolean hasMoved()
    {
        return hasMoved;
    }
}
