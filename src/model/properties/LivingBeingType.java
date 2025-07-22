package model.properties;

import lombok.AccessLevel;
import lombok.Getter;
import model.Living;
import model.animals.Animal;
import model.animals.carnivore.Carnivore;
import model.animals.herbivore.Herbivore;
import model.plants.Plant;

import java.util.Collections;
import java.util.Set;

public enum LivingBeingType
{
    ANIMAL(Animal.class),
    CARNIVORE(Carnivore.class),
    HERBIVORE(Herbivore.class),
    PLANT(Plant.class);

    @Getter(AccessLevel.PACKAGE)
    Class<? extends Living> type;

    LivingBeingType(Class<? extends Living> type)
    {
        this.type = type;
    }

    @Getter
    private final Set<Encyclopedia> members = Collections.unmodifiableSet(Encyclopedia.getByType(this));


}
