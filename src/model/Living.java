package model;

import model.properties.DeathCause;

import java.util.concurrent.ThreadLocalRandom;

public interface Living
{
    boolean consume ();

    void reproduce(Living livingBeing);

    void die(DeathCause cause);

    boolean increaseSaturationLevel(Consumable food);

    void grow();
}
