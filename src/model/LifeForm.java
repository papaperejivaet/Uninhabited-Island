package model;

import lombok.EqualsAndHashCode;
import model.animals.Animal;
import model.main.Cell;
import model.main.LifeFormFactory;
import model.main.Statistics;
import model.properties.DeathCause;
import model.properties.Encyclopedia;
import model.properties.GeneralConstants;
import model.properties.Registry;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

@EqualsAndHashCode
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
        isDead = true;
        hasBred = true;
        hasConsumed = true;
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
        if (livingBeing instanceof LifeForm lifeForm &&
                !isDead && !lifeForm.isDead &&
                !hasBred && !lifeForm.hasBred)
        {
            hasBred = true;
            lifeForm.hasBred = true;
            Living newborn = LifeFormFactory.createNewborn(livingBeingType, x, y);

            if (newborn instanceof LifeForm born)
            {
                born.hasBred = true;
            }

            currentCell.addLivingBeing(newborn);
            if (lifeForm instanceof Animal)
            {
                Statistics.registerBreeding(livingBeingType);
            }
        }
        return hasBred;
    }



    @Override
    public boolean consume()
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Consumable food = findFood();
        if (saturationLevel == Registry.getMaxSaturationLevel(livingBeingType) || isDead)
        {
            return false;
        }
        Integer eatingChance = getCurrentEatingChances(food);
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

        double maxSaturationLevel = Registry.getMaxSaturationLevel(livingBeingType);;
        saturationLevel += weight;

        if (saturationLevel > maxSaturationLevel)
        {
            saturationLevel = maxSaturationLevel;
        }

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
        saturationLevel -= (Registry.getMaxSaturationLevel(livingBeingType) / 100) * 5;
        age += GeneralConstants.CYCLE_TIME * 0.01;

        if (age == Registry.getMaxAge(livingBeingType))
        {
            die(DeathCause.NATURAL);
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

    public Encyclopedia getEncyclopediaName()
    {
        return livingBeingType;
    }


}
