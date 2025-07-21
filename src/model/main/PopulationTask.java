package model.main;

import controller.LifeFormFactory;
import model.properties.Encyclopedia;
import model.properties.GeneralConstants;
import model.properties.Registry;

import java.util.concurrent.ThreadLocalRandom;

public class PopulationTask implements Runnable
{
    private int livingNumber;

    public PopulationTask(int livingNumber)
    {
        this.livingNumber = livingNumber;
    }

    Encyclopedia livingBeing = Encyclopedia.values()[livingNumber];
    ThreadLocalRandom random = ThreadLocalRandom.current();
    int startAmount = Registry.getStartAmount(livingBeing);
    double maxAge = Registry.getMaxAge(livingBeing);
    double maxSaturation = Registry.getMaxSaturationLevel(livingBeing);

    @Override
    public void run()
    {

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

            LifeFormFactory.create(livingBeing, Island.getCell(x,y), age, saturation);
        }
    }

    public double getRandomDouble(double bound)
    {
        double number = random.nextDouble(bound);
        return (double) Math.round(number * 100) / 100;
    }
}
