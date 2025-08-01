package model.plants;

import model.main.Cell;
import model.Consumable;
import model.LifeForm;
import model.Sun;


/**
 * Класс, представляющий растение на острове.
 * <p>
 * Является подклассом {@link LifeForm}, реализует базовую модель фотосинтеза:
 * растение не поедает других существ, а "питается" солнечным светом.
 */
public class Plant extends LifeForm
{

    /**
     * Создает новое растение с заданной клеткой, возрастом и уровнем насыщения.
     *
     * @param cell            клетка, в которой находится растение
     * @param age             возраст растения
     * @param saturationLevel текущий уровень насыщения (например, от света)
     */
    protected Plant(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
    }

    /**
     * Возвращает источник питания растения — объект {@link Sun}, представляющий солнечную энергию.
     * <p>
     * Метод всегда возвращает singleton-экземпляр {@link Sun}, так как растения не поедают других существ.
     *
     * @return объект {@link Sun}, как источник "пищи"
     */
    @Override
    protected Consumable findFood()
    {
        return Sun.getInstance();
    }
}
