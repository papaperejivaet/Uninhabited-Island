package model.animals.utility;

import lombok.EqualsAndHashCode;
import model.properties.DeathCause;

@EqualsAndHashCode
public abstract class LifeForm implements Living, Consumable
{

    int age;
    long saturationLevel;

    @Override
    public void consume(Consumable food)
    {

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
}
