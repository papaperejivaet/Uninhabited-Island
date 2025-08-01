package model;

import lombok.EqualsAndHashCode;
import model.animals.Animal;
import model.main.Cell;
import model.main.LifeFormFactory;
import model.main.Statistics;
import model.properties.DeathCause;
import model.properties.Encyclopedia;
import util.GeneralConstants;
import model.properties.Registry;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Абстрактный базовый класс, представляющий живое существо (животное, растение и т.п.) в симуляции острова.
 * Содержит реализацию основных биологических функций:
 * - рост (старение и потеря насыщения),
 * - потребление пищи,
 * - размножение,
 * - смерть (естественная, от голода или поедания),
 * Хранит координаты текущего положения, а также взаимодействует с клеткой {@link Cell}, в которой находится.
 * Каждое существо имеет уникальные характеристики, определяемые через {@link Registry} и {@link Encyclopedia}.
 */
@EqualsAndHashCode()
public abstract class LifeForm implements Living, Consumable
{
    protected ReentrantLock lock = new ReentrantLock();

    protected Encyclopedia livingBeingType = Encyclopedia.getLivingBeing(this.getClass());

    protected double age;
    protected double saturationLevel;


    protected boolean hasBred; // Размножалось ли данное существо в этом цикле?
    protected boolean hasConsumed;
    protected boolean isDead;


    protected int x;
    protected int y;
    protected Cell currentCell;

    /**
     * Конструктор базовой формы жизни.
     *
     * @param cell клетка, в которой размещается существо
     * @param age начальный возраст
     * @param saturationLevel начальный уровень насыщения
     */
    protected LifeForm(Cell cell, double age, double saturationLevel)
    {
        currentCell = cell;
        this.x = cell.getX();
        this.y = cell.getY();
        this.age = age;
        this.saturationLevel = saturationLevel;
    }

    /**
     * Убивает существо, удаляя его из клетки и прерывая дальнейшее участие в симуляции.
     * Также блокирует повторные попытки потребления или размножения.
     *
     * @param cause причина смерти (естественная, от голода, съедено и т.п.)
     */
    @Override
    public void die(DeathCause cause)
    {
        isDead = true;
        hasBred = true;
        hasConsumed = true;
        currentCell.removeLivingBeing(this);
    }
    /**
     * Метод вызывается, когда существо поедается.
     * Убивает существо и возвращает его массу как питательную ценность.
     *
     * @return масса существа (вес), либо 0.0, если оно уже мертво
     */
    @Override
    public double beConsumed()
    {
        if (isDead)
        {
            return 0.0;
        }
        die(DeathCause.EATEN);
        return Registry.getWeight(livingBeingType);
    }

    /**
     * Пытается размножиться с другим существом такого же типа.
     * Условия для успешного размножения:
     * - оба существа живы,
     * - оба не размножались в текущем цикле.
     * Если успешно, то создаёт нового потомка и помещает его в ту же клетку.
     * У животных также регистрирует событие в статистике.
     *
     * @param livingBeing потенциальный партнёр для размножения
     * @return true, если размножение произошло
     */
    @Override
    public boolean reproduce(Living livingBeing)
    {
        if (!(livingBeing instanceof LifeForm partner &&
                !isDead && !partner.isDead &&
                !hasBred && !partner.hasBred))
        {
            return false;
        }

            hasBred = true;
            partner.hasBred = true;


            LifeForm newborn = (LifeForm) LifeFormFactory.createNewborn(livingBeingType, x, y);
            newborn.hasBred = true;
            currentCell.addLivingBeing(newborn);
            decreaseSaturationLevel();
            if (this instanceof Animal)
            {
                Statistics.registerBreeding(livingBeingType);
            }
        return hasBred;
    }


    /**
     * Пытается найти и съесть пищу.
     * Пища ищется через {@link #findFood()}, затем оценивается шанс поедания.
     * Успешное поедание увеличивает уровень насыщения.
     *
     * @return true, если пища была успешно потреблена
     */
    @Override
    public boolean consume()
    {
        if (isDead || saturationLevel >= Registry.getMaxSaturationLevel(livingBeingType))
        {
            return false;
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Consumable food = findFood();

        if (food == null)
        {
            return false;
        }

        Integer chanceObj = getCurrentEatingChances(food);
        int eatingChance = (chanceObj == null ? 0 : chanceObj);

        if (random.nextInt(100) < eatingChance)
        {
            return increaseSaturationLevel(food);
        }

        return false;
    }

    /**
     * Абстрактный метод поиска пищи.
     * Должен быть реализован в конкретных классах (травоядные, хищники и т.п.).
     *
     * @return объект пищи или null, если еда не найдена
     */
    protected abstract Consumable findFood();

    /**
     * Увеличивает уровень насыщения в зависимости от массы съеденной пищи.
     *
     * @param food объект пищи
     * @return всегда true (успех при съедании)
     */
    @Override
    public boolean increaseSaturationLevel(Consumable food)
    {
        double weight = food.beConsumed();
        saturationLevel += weight;
        return true;
    }

    /**
     * Реализует процесс роста:
     * - возраст увеличивается,
     * - насыщение уменьшается,
     * - проверяется наступление смерти по старости или голоду.
     * Сбрасывает флаги размножения и потребления на новый цикл.
     */
    @Override
    public void grow()
    {
        if (isDead)
        {
            return;
        }

        hasBred = false;
        hasConsumed = false;
        decreaseSaturationLevel();
        age += GeneralConstants.CYCLE_TIME * 0.01;

        if (age >= Registry.getMaxAge(livingBeingType))
        {
            die(DeathCause.NATURAL);
            return;
        }
        if (saturationLevel <= 0)
        {
            die(DeathCause.HUNGER);
        }

    }

    /**
     * Возвращает шанс съесть конкретную пищу.
     * Для животных — считывает шанс из {@link Registry}.
     * Для растительной пищи — возвращает 100% по умолчанию.
     *
     * @param food объект пищи
     * @return шанс в процентах (0–100)
     */
    protected Integer getCurrentEatingChances(Consumable food)
    {
        if (food instanceof Animal animal)
        {
            Encyclopedia prey = Encyclopedia.getLivingBeing(animal.getClass());
            return Registry.getEatingChances(livingBeingType, prey);
        }
        else
        {
            return 100;
        }
    }

    /**
     * Уменьшает насыщение существа на 2% от максимального.
     * Если насыщение упало ниже 0, вызывает смерть от голода.
     */
    protected void decreaseSaturationLevel()
    {
        saturationLevel -= Registry.getMaxSaturationLevel(livingBeingType) * 0.02; // -2% от макс
        if (saturationLevel < 0)
        {
            saturationLevel = 0;
            die(DeathCause.HUNGER);
        }

    }

}
