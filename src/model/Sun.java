package model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Синглтон, представляющий Солнце как источник энергии.
 * Используется растениями в качестве пищи.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Sun implements Consumable
{
    private static volatile Sun instance;
    /**
     * Возвращает единственный экземпляр Солнца.
     *
     * @return экземпляр Sun
     */
    public static Sun getInstance()
    {
        if (instance == null)
        {
            synchronized (Sun.class)
            {
                if (instance == null)
                {
                    instance = new Sun();
                }
            }
        }
        return instance;
    }
    /**
     * Возвращает количество энергии, получаемое при потреблении Солнца.
     * Это постоянная малая величина.
     *
     * @return 0.01 условных единиц энергии
     */
    @Override
    public double beConsumed()
    {
        return 0.01;
    }
}
