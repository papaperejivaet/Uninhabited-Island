package model.animals.utility;

import lombok.EqualsAndHashCode;
import model.plants.Plant;
import model.properties.DeathCause;
import model.properties.Encyclopedia;
import model.properties.LifeFormRegistry;

@EqualsAndHashCode
public abstract class LifeForm implements Living, Consumable
{

    double age;
    double saturationLevel;
    boolean isBred = false; // Размножалось ли данное существо в этом цикле?


    protected LifeForm(int age, long saturationLevel)
    {
        this.age = age;
        this.saturationLevel = saturationLevel;
    }

    @Override
    public void consume(Consumable food)
    {
            Integer eatingChances = getCurrentEatingChances(food);
    }

    @Override
    public void die(DeathCause cause)
    {

    }

    @Override
    public void beConsumed()
    {

    }

    @Override
    public void reproduce(Living livingBeing)
    {

    }

    private Integer getCurrentEatingChances(Consumable food)
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
