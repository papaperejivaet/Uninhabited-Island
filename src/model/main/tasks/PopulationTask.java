package model.main.tasks;

import model.Living;
import model.main.LifeFormFactory;
import model.properties.Encyclopedia;
import util.GeneralConstants;
import model.properties.Registry;

import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Задача для первоначального заселения острова существами одного вида.
 * Используется на старте симуляции. Каждый экземпляр заселяет остров существами каждого вида,
 * выбранного по индексу из {@link Encyclopedia}.
 */
public class PopulationTask implements Runnable
{
    private final Phaser phaser;

    /**
     * Создаёт задачу для заселения определённого вида существ.
     *
     * @param livingNumber порядковый номер существа в {@link Encyclopedia}
     * @param phaser синхронизатор для ожидания завершения всех задач
     */
    public PopulationTask(int livingNumber, Phaser phaser)
    {
        this.phaser = phaser;
        livingBeing = Encyclopedia.values()[livingNumber];
        startAmount = Registry.getStartAmount(livingBeing);
        maxAge = Registry.getMaxAge(livingBeing);
        maxSaturation = Registry.getMaxSaturationLevel(livingBeing);
    }

    private final Encyclopedia livingBeing;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final int startAmount;
    private final double maxAge;
    private final double maxSaturation;

    /**
     * Основной метод выполнения задачи.
     * Регистрирует себя в {@link Phaser}, затем создаёт определённое количество особей
     * указанного вида, размещая их в случайных клетках острова.
     * Возраст и уровень насыщения у каждого существа также выбираются случайным образом.
     * После завершения — сообщает синхронизатору о завершении работы.
     */
    @Override
    public void run()
    {
        phaser.register();
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
            LifeFormFactory.create(livingBeing, x, y, age, saturation);
        }
        phaser.arriveAndDeregister();
    }

    /**
     * Генерирует случайное число от 0 до указанной границы и округляет его до двух знаков.
     *
     * Используется для генерации возраста и уровня насыщения создаваемых существ.
     *
     * @param bound верхняя граница значения
     * @return округлённое значение от 0 до bound
     */
    public double getRandomDouble(double bound)
    {
        double number = random.nextDouble(bound);
        return (double) Math.round(number * 100) / 100;
    }
}
