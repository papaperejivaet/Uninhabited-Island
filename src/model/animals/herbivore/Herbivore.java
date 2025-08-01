package model.animals.herbivore;

import model.Consumable;
import model.main.Cell;
import model.animals.Animal;
import model.properties.Encyclopedia;
import model.properties.LivingBeingType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Абстрактный класс для травоядных животных.
 * Наследуется от {@link Animal}, реализует логику поиска растительной пищи.
 */
public abstract class Herbivore extends Animal
{
    /**
     * Создает травоядное животное.
     *
     * @param cell            клетка, в которой находится существо
     * @param age             возраст
     * @param saturationLevel уровень насыщения
     */
    protected Herbivore(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }
    /**
     * Ищет доступную пищу в текущей клетке.
     * Основной рацион — члены группы PLANT.
     * Дополнительные типы пищи можно указать через {@link #additionalFood()}.
     *
     * @return случайный объект {@link Consumable}, либо {@code null}, если еды нет
     */
    @Override
    protected Consumable findFood()
    {
        Set<Encyclopedia> foodTypes = new HashSet<>(LivingBeingType.PLANT.getMembers());
        Set<Encyclopedia> additionalSet = additionalFood();
        foodTypes.addAll(additionalSet);
        //Т.к. Plant реализует Consumable
        return (Consumable) currentCell.getRandomLiving(foodTypes, this);
    }

    /**
     * Позволяет переопределить набор дополнительных источников пищи,
     * помимо стандартных растений.
     *
     * @return множество видов {@link Encyclopedia}, допустимых к поеданию
     */
    public Set<Encyclopedia> additionalFood()
    {
        return Collections.emptySet();
    }
}
