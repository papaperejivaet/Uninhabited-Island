package controller;

import exceptions.LifeFormCreatingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.main.Cell;
import model.animals.utility.Living;
import model.properties.Encyclopedia;
import model.properties.LifeFormRegistry;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LifeFormFactory
{

//    public static Living create(Encyclopedia livingBeing, Cell cell, double age, double saturationLevel)
//    {
//        try
//        {
//            Constructor<?> constructor = livingBeing.getType().getConstructor(Cell.class, double.class, double.class);
//            return (Living) constructor.newInstance(cell, age, saturationLevel);
//        }
//        catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
//        {
//            throw new LifeFormCreatingException("Произошла ошибка при создании формы жизни" + Arrays.toString(e.getStackTrace()));
//        }
//    }

    private static final Map<Encyclopedia, MethodHandle> constructorCache = new ConcurrentHashMap<>();

    public static Living create(Encyclopedia livingBeing, Cell cell, double age, double saturationLevel)
    {
        try
        {
            //Если нет конструктора в кэше, то найти конструктор и присвоить в constructorHandle
            MethodHandle constructorHandle = constructorCache.computeIfAbsent(livingBeing, lb -> {

                try
                {
                    // Поисковик методов
                    MethodHandles.Lookup lookup = MethodHandles.publicLookup();

                    // Поисковик ищет конструктор ->
                    return lookup.findConstructor(
                            // Класс конструктора,
                            lb.getType(),
                            // Возвращаемое значение и аргументы конструктора
                            MethodType.methodType(void.class, Cell.class, double.class, double.class)
                    );
                }
                catch (NoSuchMethodException | IllegalAccessException e)
                {
                    throw new LifeFormCreatingException("Не удалось найти конструктор для " + lb.name() + "\n" + Arrays.toString(e.getStackTrace()));
                }

            });

            Living createdLivingBeing = (Living) constructorHandle.invoke(cell, age, saturationLevel);
            cell.addLivingBeing(createdLivingBeing);

            return createdLivingBeing;
        }
        catch (Throwable e) // invoke может выбросить Throwable
        {
            throw new LifeFormCreatingException("Ошибка при создании формы жизни: " + Arrays.toString(e.getStackTrace()));
        }
    }


    public static Living createNewborn(Living mom, Living dad, Cell cell)
    {
        Encyclopedia livingBeing = Encyclopedia.getLivingBeing(mom.getClass());
        mom.reproduce(dad);
        return create(livingBeing, cell, 0.0, LifeFormRegistry.getMaxSaturationLevel(livingBeing));
    }
}
