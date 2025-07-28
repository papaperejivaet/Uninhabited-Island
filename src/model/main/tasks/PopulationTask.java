package model.main.tasks;

import model.Living;
import model.main.LifeFormFactory;
import model.properties.Encyclopedia;
import util.GeneralConstants;
import model.properties.Registry;

import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

public class PopulationTask implements Runnable
{
    private final int livingNumber;
    private final Phaser phaser;


    public PopulationTask(int livingNumber, Phaser phaser)
    {
        this.livingNumber = livingNumber;
        this.phaser = phaser;
        livingBeing = Encyclopedia.values()[livingNumber];
        startAmount = Registry.getStartAmount(livingBeing);
        maxAge = Registry.getMaxAge(livingBeing);
        maxSaturation = Registry.getMaxSaturationLevel(livingBeing);
    }

    private final Encyclopedia livingBeing;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final int startAmount;
    private final double maxAge;
    private final double maxSaturation;

    @Override
    public void run()
    {
        phaser.register();
        int x;
        int y;
        double age;
        double saturation;

        for (int i = 0; i < startAmount; i++)
        {
            x = random.nextInt(GeneralConstants.LENGTH);
            y = random.nextInt(GeneralConstants.HEIGHT);
            age = getRandomDouble(maxAge);
            saturation = getRandomDouble(maxSaturation);
            LifeFormFactory.create(livingBeing, x, y, age, saturation);
        }
        phaser.arriveAndDeregister();
    }

    public double getRandomDouble(double bound)
    {
        double number = random.nextDouble(bound);
        return (double) Math.round(number * 100) / 100;
    }
}
