package model;

import model.properties.DeathCause;


public interface Living
{
    boolean consume ();

    boolean reproduce(Living livingBeing);

    void die(DeathCause cause);

    boolean increaseSaturationLevel(Consumable food);

    void grow();
}
