package model.properties;


import model.animals.carnivore.*;
import model.animals.herbivore.*;
import model.plants.*;
import model.animals.utility.Animal;
import model.animals.utility.Living;

import java.util.Map;
import static java.util.Map.entry;

public final class LifeFormProperties
{
    public static Double getWeight(Class<? extends Living> type)
    {
        return Properties.weight.get(type);
    }

    public static Integer getCellMaxAmount(Class<? extends Living> type)
    {
        return Properties.cellMaxAmount.get(type);
    }

    public static Integer getMaxSpeed(Class<? extends Living> type)
    {
        return Properties.maxSpeed.get(type);
    }

    public static Double getMaxSaturationAmount(Class<? extends Living> type)
    {
        return Properties.saturationAmount.get(type);
    }


    private static class Properties
    {
        private static final Map<Class<? extends Living>, Double> weight = Map.ofEntries(
                entry(Wolf.class, 50.0),
                entry(Snake.class, 15.0),
                entry(Fox.class, 8.0),
                entry(Bear.class, 500.0),
                entry(Eagle.class, 6.0),
                entry(Horse.class, 400.0),
                entry(Deer.class, 300.0),
                entry(Rabbit.class, 2.0),
                entry(Mouse.class, 0.05),
                entry(Goat.class, 60.0),
                entry(Sheep.class, 70.0),
                entry(Hog.class, 400.0),
                entry(Buffalo.class, 700.0),
                entry(Duck.class, 1.0),
                entry(Caterpillar.class, 0.01),
                entry(Grass.class, 1.0),
                entry(Flower.class, 2.0),
                entry(Mushroom.class, 3.0)
        );

        private static final Map<Class<? extends Living>, Integer> cellMaxAmount = Map.ofEntries(
                entry(Wolf.class, 30),
                entry(Snake.class, 30),
                entry(Fox.class, 30),
                entry(Bear.class, 5),
                entry(Eagle.class, 20),
                entry(Horse.class, 20),
                entry(Deer.class, 20),
                entry(Rabbit.class, 150),
                entry(Mouse.class, 500),
                entry(Goat.class, 140),
                entry(Sheep.class, 140),
                entry(Hog.class, 50),
                entry(Buffalo.class, 10),
                entry(Duck.class, 200),
                entry(Caterpillar.class, 1000),
                entry(Grass.class, 100),
                entry(Flower.class, 50),
                entry(Mushroom.class, 30)
        );

        private static final Map<Class<? extends Animal>, Integer> maxSpeed = Map.ofEntries(
                entry(Wolf.class, 3),
                entry(Snake.class, 1),
                entry(Fox.class, 2),
                entry(Bear.class, 2),
                entry(Eagle.class, 3),
                entry(Horse.class, 4),
                entry(Deer.class, 4),
                entry(Rabbit.class, 2),
                entry(Mouse.class, 1),
                entry(Goat.class, 3),
                entry(Sheep.class, 3),
                entry(Hog.class, 2),
                entry(Buffalo.class, 3),
                entry(Duck.class, 4),
                entry(Caterpillar.class, 0)
        );

        private static final Map<Class<? extends Living>, Double> saturationAmount = Map.ofEntries(
                entry(Wolf.class, 8.0),
                entry(Snake.class, 3.0),
                entry(Fox.class, 2.0),
                entry(Bear.class, 80.0),
                entry(Eagle.class, 1.0),
                entry(Horse.class, 60.0),
                entry(Deer.class, 50.0),
                entry(Rabbit.class, 0.45),
                entry(Mouse.class, 0.01),
                entry(Goat.class, 10.0),
                entry(Sheep.class, 15.0),
                entry(Hog.class, 50.0),
                entry(Buffalo.class, 100.0),
                entry(Duck.class, 0.15),
                entry(Caterpillar.class, 0.0),
                entry(Grass.class, 100.0),
                entry(Flower.class, 200.0),
                entry(Mushroom.class, 1.0)
        );

        private static final Map<Class<? extends Living>, Map<Class<? extends Living>, Integer>> eatingChances =
                Map.ofEntries(
                        entry(Wolf.class, Map.ofEntries(
                                
                        ))
                );
    }

}
