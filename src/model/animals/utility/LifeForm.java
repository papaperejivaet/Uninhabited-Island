package model.animals.utility;

import lombok.EqualsAndHashCode;
import model.main.Cell;
import model.properties.DeathCause;
import model.properties.Encyclopedia;
import model.properties.GeneralConstants;
import model.properties.LifeFormRegistry;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

@EqualsAndHashCode
public abstract class LifeForm implements Living, Consumable
{
    protected ReentrantLock lock = new ReentrantLock();

    protected Encyclopedia livinBeingType = Encyclopedia.getLivingBeing(this.getClass());

    double age;
    double saturationLevel;
    double maxAge = LifeFormRegistry.getMaxAge(livinBeingType);
    double maxSaturationLevel = LifeFormRegistry.getMaxSaturationLevel(livinBeingType);

    boolean isBred = false; // Размножалось ли данное существо в этом цикле?
    boolean isDead = false;

    protected Map<Encyclopedia, Integer> currentPosition;
    int x;
    int y;
    Cell currentCell;


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
        isBred = false;
        currentCell.removeLivingBeing(this);
        LifeFormRegistry.registerDeath(livinBeingType, cause);
    }

    @Override
    public double beConsumed()
    {
        if (isDead)
        {
            return 0.0;
        }
        die(DeathCause.EATEN);
        return LifeFormRegistry.getWeight(livinBeingType);
    }

    @Override
    public void reproduce(Living livingBeing)
    {
        if (livingBeing instanceof LifeForm lifeForm &&
                !isDead && !lifeForm.isDead &&
                !isBred && !lifeForm.isBred)
        {
            isBred = true;
            lifeForm.isBred = true;
        }
    }

    @Override
    public boolean consume(Consumable food, ThreadLocalRandom random)
    {
        if (saturationLevel == maxSaturationLevel || isDead)
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

    @Override
    public boolean increaseSaturationLevel(Consumable food)
    {
        double weight = food.beConsumed();

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

        if (age == LifeFormRegistry.getMaxAge(livinBeingType))
        {
            die(DeathCause.NATURAL);
        }
            isBred = false;
            age += GeneralConstants.CYCLE_TIME * 0.01;

    }

    protected Integer getCurrentEatingChances(Consumable food)
    {
        if (food instanceof Animal animal)
        {
            Encyclopedia prey = Encyclopedia.getLivingBeing(animal.getClass());
            return LifeFormRegistry.getEatingChances(livinBeingType, prey);
        }
        else
        {
            return 100;
        }
    }


}
