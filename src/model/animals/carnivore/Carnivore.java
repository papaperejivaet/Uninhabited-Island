package model.animals.carnivore;
//Плотоядное

import model.Consumable;
import model.main.Cell;
import model.animals.Animal;
import model.properties.LivingBeingType;

/**
 * Абстрактный класс для плотоядных животных.
 * Наследуется от {@link Animal}, реализует логику поиска животной пищи.
 */
public abstract class Carnivore extends Animal
{

    /**
     * Создает плотоядное животное.
     *
     * @param cell            клетка, в которой находится существо
     * @param age             возраст
     * @param saturationLevel уровень насыщения
     */
    protected Carnivore(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }

    /**
     * Ищет жертву для питания среди других животных в текущей клетке.
     *
     * @return случайное {@link Consumable} животное, либо {@code null}, если пищи нет
     */
    @Override
    protected Consumable findFood()
    {
        //Т.к. Animal реализует Consumable
        return (Consumable) currentCell.getRandomLiving(LivingBeingType.ANIMAL.getMembers(), this);
    }
}
