package model.animals.utility;

import model.properties.DeathCause;

import java.util.concurrent.ThreadLocalRandom;

public interface Living
{
    boolean consume (Consumable food, ThreadLocalRandom random);

    void reproduce(Living livingBeing);

    void die(DeathCause cause);

    boolean increaseSaturationLevel(Consumable food);

    void grow();
}
