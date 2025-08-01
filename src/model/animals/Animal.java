package model.animals;


import lombok.EqualsAndHashCode;
import model.LifeForm;
import model.Mobile;
import model.main.Cell;
import model.main.Statistics;
import model.properties.DeathCause;
import model.properties.Registry;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Абстрактный класс, представляющий животных на острове.
 * Расширяет {@link LifeForm} и реализует интерфейс {@link Mobile}.
 * Добавляет логику передвижения и отслеживания факта перемещения.
 * Поддерживает потокобезопасное перемещение между клетками с помощью {@link ReentrantLock}.
 */
@EqualsAndHashCode(callSuper = true)
public abstract class Animal extends LifeForm implements Mobile
{
    boolean hasMoved;
    /**
     * Создает животное с заданными параметрами.
     *
     * @param cell            клетка, в которой находится животное
     * @param age             возраст животного
     * @param saturationLevel текущий уровень насыщения
     */
    protected Animal(Cell cell, double age, double saturationLevel)
    {
        super(cell, age, saturationLevel);
        hasMoved = false;
    }

    /**
     * Выполняет попытку перемещения животного на новую клетку.
     * Использует неблокирующую синхронизацию с ограничением по времени, чтобы избежать дедлоков.
     * Обновляет положение, координаты и уменьшает уровень насыщения.
     */
    @Override
    public void move() {
        if (hasMoved) return;

        Cell from = currentCell;
        Cell to = getNewCell();

        // Порядок захвата локов
        Cell first = System.identityHashCode(from) < System.identityHashCode(to) ? from : to;
        Cell second = (from == first) ? to : from;

        ReentrantLock firstLock = first.getLock();
        ReentrantLock secondLock = second.getLock();

        boolean firstAcquired = false;
        boolean secondAcquired = false;

        try {
            // Попробовать захватить первый лок
            firstAcquired = firstLock.tryLock(10, TimeUnit.MILLISECONDS);
            if (!firstAcquired) return;

            // Попробовать захватить второй лок
            secondAcquired = secondLock.tryLock(10, TimeUnit.MILLISECONDS);
            if (!secondAcquired) return;

            // Переместиться
            changeHabitat(to);

        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        } finally {
            if (secondAcquired) secondLock.unlock();
            if (firstAcquired) firstLock.unlock();
        }
    }

    private void changeHabitat(Cell destination)
    {
        currentCell.removeLivingBeing(this);
        currentCell = destination;
        currentCell.addLivingBeing(this);
        x = currentCell.getX();
        y = currentCell.getY();
        hasMoved = true;
        decreaseSaturationLevel();
    }

    /**
     * Удаляет животное с карты и регистрирует смерть в статистике.
     *
     * @param cause причина смерти
     */
    @Override
    public void die(DeathCause cause)
    {
        super.die(cause);
        Statistics.registerDeath(livingBeingType, cause);
    }
    /**
     * Обновляет состояние животного при наступлении нового цикла:
     * - вызывает {@link LifeForm#grow()};
     * - сбрасывает флаги hasMoved и hasConsumed.
     */
    @Override
    public void grow()
    {
        super.grow();
        hasMoved = false;
        hasConsumed = false;
    }

    /**
     * Пытается найти новую клетку для перемещения.
     * Новая клетка выбирается случайным образом, количество шагов зависит от скорости.
     *
     * @return клетка, в которую животное переместится
     */
    private Cell getNewCell()
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int maxSpeed = Registry.getMaxSpeed(livingBeingType) + 1;
        int stepsAmount = random.nextInt(maxSpeed);
        List<Cell> neighboringCells;
        int cellNumber;
        Cell newCell = currentCell;

        for (int i = 0; i < stepsAmount; i++)
        {
            neighboringCells = newCell.getNeighboringCells();
            cellNumber = random.nextInt(neighboringCells.size());
            newCell = neighboringCells.get(cellNumber);
        }

        return newCell;
    }
    /**
     * Пытается найти и съесть доступную пищу.
     * Если потребление успешно, обновляется статистика.
     *
     * @return {@code true}, если пища была найдена и потреблена
     */
    @Override
    public boolean consume()
    {
        boolean hasConsumed = super.consume();
        if (hasConsumed)
        {
            Statistics.registerConsumption(livingBeingType);
        }
        return hasConsumed;
    }

}
