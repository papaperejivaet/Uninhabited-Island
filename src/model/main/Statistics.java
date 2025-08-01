package model.main;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import model.properties.DeathCause;
import model.properties.Encyclopedia;
import model.properties.EndReason;
import model.properties.LivingBeingType;
import util.GeneralConstants;
import view.Drawer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Сервис для сбора и анализа статистики симуляции.
 * Отслеживает:
 * - смерти по причинам;
 * - размножения;
 * - случаи потребления пищи;
 * - наличие различных типов существ на клетках.
 * Также отвечает за определение условий завершения симуляции.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Statistics
{
    @Getter
    private static int currentCycleNumber = 0;

    private static final Map<Encyclopedia, Integer> naturalDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> eatenDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> hungerDeaths = new ConcurrentHashMap<>();
    private static final Map<Encyclopedia, Integer> accidentDeaths = new ConcurrentHashMap<>();

    private static final Map<Encyclopedia, Integer> bredCount = new ConcurrentHashMap<>();

    private static final Map<Encyclopedia, Integer> ateCount = new ConcurrentHashMap<>();

    private static final Map<Cell, Boolean> animalContainment = new ConcurrentHashMap<>();
    private static final Map<Cell, Boolean> carnivoreContainment = new ConcurrentHashMap<>();
    private static final Map<Cell, Boolean> herbivoreContainment = new ConcurrentHashMap<>();
    private static final Map<Cell, Boolean> plantContainment = new ConcurrentHashMap<>();


    /**
     * Регистрирует смерть существа и причину.
     *
     * @param type тип существа
     * @param deathCause причина смерти
     */
    public static void registerDeath(Encyclopedia type, DeathCause deathCause)
    {
        Map<Encyclopedia, Integer> deathMap = getDeathMap(deathCause);
        register(type, deathMap);
    }

    public static void registerBreeding(Encyclopedia type)
    {
        register(type, bredCount);
    }

    public static void registerConsumption(Encyclopedia type)
    {
        register(type, ateCount);
    }
    /**
     * Проверяет, какие типы существ присутствуют в клетке,
     * и обновляет глобальные карты присутствия.
     *
     * @param cell клетка острова
     */
    public static void confirmAvailability(Cell cell)
    {
        boolean containsAny;

        for (LivingBeingType livingBeingType : LivingBeingType.values())
        {
            containsAny = cell.containsAny(livingBeingType);
            addInContainment(cell, livingBeingType, containsAny);
        }
    }

    private static void addInContainment(Cell cell, LivingBeingType type, Boolean containsAny)
    {
        switch (type)
        {
            case ANIMAL -> animalContainment.put(cell, containsAny);
            case CARNIVORE -> carnivoreContainment.put(cell, containsAny);
            case HERBIVORE -> herbivoreContainment.put(cell, containsAny);
            case PLANT -> plantContainment.put(cell, containsAny);
        }
    }

    private static void register(Encyclopedia type, Map<Encyclopedia, Integer> statisticMap)
    {
        if (!statisticMap.containsKey(type))
        {
            statisticMap.put(type, 1);
            return;
        }
        Integer count = statisticMap.get(type);
        statisticMap.put(type, ++count);
    }
    /**
     * Проверяет, выполнены ли условия завершения симуляции.
     *
     * @return true — продолжить симуляцию, false — завершить
     */
    protected static boolean checkConditions()
    {
        if (!animalContainment.containsValue(true))
        {
            Drawer.drawEnd(EndReason.ALL_DEAD);
            return false;
        }
        if (!carnivoreContainment.containsValue(true))
        {
            Drawer.drawEnd(EndReason.ONLY_HERBIVORE_LEFT);
            return false;
        }
        if (!herbivoreContainment.containsValue(true))
        {
            Drawer.drawEnd(EndReason.ONLY_CARNIVORE_LEFT);
            return false;
        }
        if (!plantContainment.containsValue(true))
        {
            Drawer.drawEnd(EndReason.NO_PLANTS_LEFT);
        }
        if (currentCycleNumber >= GeneralConstants.MAX_CYCLES)
        {
            Drawer.drawEnd(EndReason.TIMEOUT);
            return false;
        }

        return true;
    }
    /**
     * Увеличивает счётчик симуляционных циклов.
     */
    protected static void nextCycle()
    {
        currentCycleNumber++;
    }

    public static int getBreedingCount()
    {
        int counter = 0;
        for (Map.Entry<Encyclopedia, Integer> entry : bredCount.entrySet())
        {
            counter += entry.getValue();
        }
        return counter;
    }
    public static int getAteCount()
    {
        int counter = 0;
        for (Map.Entry<Encyclopedia, Integer> entry : ateCount.entrySet())
        {
            counter += entry.getValue();
        }
        return counter;
    }

    public static int getDeathCount(DeathCause cause)
    {
        int counter = 0;

        Map<Encyclopedia, Integer> deathMap = getDeathMap(cause);

        for (Map.Entry<Encyclopedia, Integer> entry : deathMap.entrySet())
        {
            counter += entry.getValue();
        }

        return counter;
    }

    private static Map<Encyclopedia, Integer> getDeathMap(DeathCause cause)
    {
        return switch (cause)
        {
            case NATURAL -> naturalDeaths;
            case EATEN -> eatenDeaths;
            case HUNGER -> hungerDeaths;
            case ACCIDENT -> accidentDeaths;
        };
    }

}
