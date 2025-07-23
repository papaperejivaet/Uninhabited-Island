package model.properties;


import exceptions.NoSuchAnimalException;
import lombok.Getter;
import model.animals.carnivore.*;
import model.animals.herbivore.*;
import model.Living;
import model.plants.*;

import java.util.*;

public enum Encyclopedia
{
    // Плотоядные
    WOLF("Волк", Wolf.class),
    SNAKE("Змея", Snake.class),
    FOX("Лиса", Fox.class),
    BEAR("Медведь", Bear.class),
    EAGLE("Орел", Eagle.class),

    // Травоядные
    HORSE("Лошадь", Horse.class),
    DEER("Олень", Deer.class),
    RABBIT("Кролик", Rabbit.class),
    MOUSE("Мышь", Mouse.class),
    GOAT("Коза", Goat.class),
    SHEEP("Овца", Sheep.class),
    HOG("Кабан", Hog.class),
    BUFFALO("Буйвол", Buffalo.class),
    DUCK("Утка", Duck.class),
    CATERPILLAR("Гусеница", Caterpillar.class),

    // Растения
    GRASS("Трава", Grass.class),
    FLOWER("Цветок", Flower.class),
    MUSHROOM("Гриб", Mushroom.class);



    private final String translation;
    @Getter
    private final Class<? extends Living> type;

    Encyclopedia(String translation, Class<? extends Living> type)
    {
        this.translation = translation;
        this.type = type;
    }


    @Override
    public String toString()
    {
        return super.toString();
    }

    public String toRuString()
    {
        return translation;
    }

    //============================= Быстрый доступ =============================

    private static final Map<String, Encyclopedia> names;
    private static final Map<Class<? extends Living>, Encyclopedia> classes;



    static
    {
        names = new HashMap<>();
        classes = new HashMap<>();

        for (Encyclopedia livingBeing : values())
        {
            names.put(livingBeing.name(), livingBeing);
            classes.put(livingBeing.type, livingBeing);
        }
    }

    public static Encyclopedia getLivingBeing(String name)
    {
            Encyclopedia livingBeing = names.get(name.toUpperCase());
            if (livingBeing == null)
            {
                throw new NoSuchAnimalException("Такого животного нет в Энциклопедии!");
            }
            return livingBeing;
    }

    public static Encyclopedia getLivingBeing(Class<? extends Living> type)
    {
        Encyclopedia livingBeing = classes.get(type);

        if (livingBeing == null)
        {
            throw new NoSuchAnimalException("Такого животного нет в Энциклопедии!");
        }
        return livingBeing;
    }

    static Set<Encyclopedia> getByType(LivingBeingType lbType)
    {
        Class<? extends Living> type = lbType.getType();
        Set<Encyclopedia> set = new HashSet<>();
        for (Map.Entry<Class<? extends Living>, Encyclopedia> entry : classes.entrySet())
        {
            if (entry.getKey().getSuperclass() == type)
            {
                set.add(entry.getValue());
            }
        }
        return set;
    }

}
