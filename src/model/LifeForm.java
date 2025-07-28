package model;

import lombok.EqualsAndHashCode;
import model.animals.Animal;
import model.main.Cell;
import model.main.LifeFormFactory;
import model.main.Statistics;
import model.properties.DeathCause;
import model.properties.Encyclopedia;
import util.GeneralConstants;
import model.properties.Registry;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

@EqualsAndHashCode()
public abstract class LifeForm implements Living, Consumable
{
    protected ReentrantLock lock = new ReentrantLock();

    protected Encyclopedia livingBeingType = Encyclopedia.getLivingBeing(this.getClass());

    protected double age;
    protected double saturationLevel;


    protected boolean hasBred; // Размножалось ли данное существо в этом цикле?
    protected boolean hasConsumed;
    protected boolean isDead;

    protected Map<Encyclopedia, Integer> currentPosition;
    protected int x;
    protected int y;
    protected Cell currentCell;


    protected LifeForm(Cell cell, double age, double saturationLevel)
    {
        currentCell = cell;
        this.x = cell.getX();
        this.y = cell.getY();
        this.age = age;
        this.saturationLevel = saturationLevel;
    }


    @Override
    public void die(DeathCause cause)
    {
        lock.lock();
        isDead = true;
        hasBred = true;
        hasConsumed = true;
        lock.unlock();
        currentCell.removeLivingBeing(this);
    }

    @Override
    public double beConsumed()
    {
        if (isDead)
        {
            return 0.0;
        }
        die(DeathCause.EATEN);
        return Registry.getWeight(livingBeingType);
    }

    @Override
    public boolean reproduce(Living livingBeing)
    {
        if (!(livingBeing instanceof LifeForm partner &&
                !isDead && !partner.isDead &&
                !hasBred && !partner.hasBred))
        {
            return false;
        }

            hasBred = true;
            partner.hasBred = true;


            LifeForm newborn = (LifeForm) LifeFormFactory.createNewborn(livingBeingType, x, y);
            newborn.hasBred = true;
            currentCell.addLivingBeing(newborn);
            decreaseSaturation();
            if (this instanceof Animal)
            {
                Statistics.registerBreeding(livingBeingType);
            }
        return hasBred;
    }



    @Override
    public boolean consume()
    {
        if (isDead || saturationLevel >= Registry.getMaxSaturationLevel(livingBeingType))
        {
            return false;
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Consumable food = findFood();

        if (food == null)
        {
            return false;
        }

        Integer chanceObj = getCurrentEatingChances(food);
        int eatingChance = (chanceObj == null ? 0 : chanceObj);

        if (random.nextInt(100) < eatingChance)
        {
            return increaseSaturationLevel(food);
        }

        return false;
    }

    protected abstract Consumable findFood();

    @Override
    public boolean increaseSaturationLevel(Consumable food)
    {
        double weight = food.beConsumed();
        saturationLevel += weight;


        return true;
    }

    @Override
    public void grow()
    {
        if (isDead)
        {
            return;
        }

        hasBred = false;
        hasConsumed = false;
        saturationLevel -= (Registry.getMaxSaturationLevel(livingBeingType) / 100) * 5;
        age += GeneralConstants.CYCLE_TIME * 0.01;

        if (age >= Registry.getMaxAge(livingBeingType))
        {
            die(DeathCause.NATURAL);
            return;
        }
        if (saturationLevel <= 0)
        {
            die(DeathCause.HUNGER);
        }

    }

    protected Integer getCurrentEatingChances(Consumable food)
    {
        if (food instanceof Animal animal)
        {
            Encyclopedia prey = Encyclopedia.getLivingBeing(animal.getClass());
            return Registry.getEatingChances(livingBeingType, prey);
        }
        else
        {
            return 100;
        }
    }

    protected void decreaseSaturation()
    {
        saturationLevel -= Registry.getMaxSaturationLevel(livingBeingType) * 0.02; // например, -2% от макс
        if (saturationLevel < 0)
        {
            saturationLevel = 0;
            die(DeathCause.HUNGER);
        }

    }

}
