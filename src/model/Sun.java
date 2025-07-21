package model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Sun implements Consumable
{
    private static volatile Sun instance;

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

    @Override
    public double beConsumed()
    {
        return 0.01;
    }
}
