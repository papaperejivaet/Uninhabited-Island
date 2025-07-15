package model.animals.utility;

import model.properties.DeathCause;

public interface Living
{
    void consume (Consumable food);

    void reproduce(Living livingBeing);

    void die(DeathCause cause);
}
