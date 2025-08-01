package model;

import model.properties.DeathCause;

/**
 * Интерфейс, описывающий основные жизненные функции живого существа.
 * Используется для всех объектов, обладающих жизненным циклом.
 */
public interface Living
{
    boolean consume ();

    boolean reproduce(Living livingBeing);

    void die(DeathCause cause);

    boolean increaseSaturationLevel(Consumable food);
    /**
     * Выполняет действия, связанные со старением: снижение насыщения, увеличение возраста, возможная смерть.
     */
    void grow();
}
