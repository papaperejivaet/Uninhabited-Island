package model.animals.utility;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import model.Cell;
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

    double age;
    double saturationLevel;
    double maxSaturationLevel = LifeFormRegistry.getMaxSaturationLevel(Encyclopedia.getLivingBeing(this.getClass()));

    boolean isBred = false; // Размножалось ли данное существо в этом цикле?
    @Getter
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
    }

    @Override
    public double beConsumed()
    {
        if (isDead)
        {
            return 0.0;
        }
        die(DeathCause.EATEN);
        return LifeFormRegistry.getWeight(Encyclopedia.getLivingBeing(this.getClass()));
    }

    @Override
    public void reproduce(Living livingBeing)
    {

        if (livingBeing instanceof LifeForm lifeForm && !isDead)
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
        if (!isDead)
        {
            isBred = false;
            age += GeneralConstants.CYCLE_TIME * 0.01;
        }
    }

    protected Integer getCurrentEatingChances(Consumable food)
    {
        if (food instanceof Animal animal)
        {
            return LifeFormRegistry.getEatingChances(Encyclopedia.getLivingBeing(this.getClass()),
                    Encyclopedia.getLivingBeing(animal.getClass()));
        }
        else
        {
            return 100;
        }
    }


}
