package model.main;


import lombok.Getter;
import model.main.tasks.MoveTask;
import model.main.tasks.PopulationTask;
import model.main.tasks.LiveTask;
import model.properties.Encyclopedia;
import util.GeneralConstants;
import model.properties.LivingBeingType;
import view.Drawer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Island
{
    /**
     * Двумерная карта острова.
     */
    @Getter
    private static final Cell[][] islandMap = new Cell[GeneralConstants.HEIGHT][GeneralConstants.LENGTH];

    private static final ExecutorService executor = Executors.newFixedThreadPool(GeneralConstants.MAX_PROCESSING_THREADS);
    private static final Phaser phaser = new Phaser(1);



    public static void main(String[] args)
    {
        createMap();
        populateRandomly();
        startSimulation();
        shutdown();
    }

    /**
     * Запускает бесконечный цикл симуляции, пока не будут нарушены условия продолжения,
     * проверяемые в {@link Statistics#checkConditions()}.
     * Внутри каждого цикла:
     * - выполняется симуляция одного шага (движение, жизнь, размножение);
     * - отрисовывается текущее состояние поля.
     */
    private static void startSimulation()
    {
        do
        {
            simulate();
            Drawer.drawField();
        }
        while (Statistics.checkConditions());

    }

    /**
     * Создаёт сетку клеток (остров) и связывает каждую клетку с её соседями.
     * Клетки создаются по размеру, заданному в {@link GeneralConstants}, и после этого каждая
     * клетка определяет и сохраняет список соседних клеток.
     */
    private static void createMap()
    {
        for (int y = 0; y < islandMap.length; y++)
        {
            for (int x = 0; x < islandMap[y].length; x++)
            {
                islandMap[y][x] = new Cell(x, y);
            }
        }

        for (Cell[] cells : islandMap)
        {
            for (Cell cell : cells)
            {
                cell.findNeighboringCells();
            }
        }
    }

    /**
     * Заселяет карту существами из справочника {@link Encyclopedia}.
     * Для каждого вида создается задача {@link PopulationTask}, которая выполняется в пуле потоков.
     * После заселения основной поток ожидает завершения всех задач с помощью {@link Phaser}.
     */
    private static void populateRandomly()
    {
        int livingBeingCounter = Encyclopedia.values().length;
        for (int i = 0; i < livingBeingCounter; i++)
        {
            executor.submit(new PopulationTask(i, phaser));
        }

        phaser.arriveAndAwaitAdvance();
    }

    /**
     * Возвращает клетку по координатам.
     *
     * @param x координата X
     * @param y координата Y
     * @return объект клетки
     */
    static Cell getCell(int x, int y)
    {
        return islandMap[y][x];
    }

    /**
     * Выполняет один шаг симуляции, состоящий из следующих этапов:
     * 1. Передвижение подвижных существ в каждой клетке через {@link MoveTask}.
     * 2. Жизненные действия (рост, питание, размножение) через {@link LiveTask}.
     * 3. Сбор статистики и визуализация.
     * Все задачи запускаются параллельно, синхронизируются с помощью {@link Phaser}.
     */
    private static void simulate()
    {
        phaser.bulkRegister(GeneralConstants.CELLS_AMOUNT);
        for (Cell[] cells : islandMap)
        {
            for (Cell cell : cells)
            {
                executor.submit(new MoveTask(cell, phaser));
            }
        }

        phaser.arriveAndAwaitAdvance();

        phaser.bulkRegister(GeneralConstants.CELLS_AMOUNT);
        for (Cell[] cells : islandMap)
        {
            for (Cell cell : cells)
            {
                executor.submit(new LiveTask(cell, phaser));
            }
        }


        phaser.arriveAndAwaitAdvance();
        sendEveryCellChar();
        Statistics.nextCycle();

    }

    /**
     * Сканирует все клетки острова и определяет, какие растения и животные наиболее многочисленны в каждой из них.
     * Собранные символы передаются в визуализатор {@link Drawer} для отображения на экране.
     */
    private static void sendEveryCellChar()
    {
        List<String> mostPopularAnimalChars = new ArrayList<>();
        List<String> mostPopularPlantChars = new ArrayList<>();

        for (Cell[] cells : islandMap)
        {
            for (Cell cell : cells)
            {
                mostPopularAnimalChars.add(cell.getCharOfMaxAmount(LivingBeingType.ANIMAL));
                mostPopularPlantChars.add(cell.getCharOfMaxAmount(LivingBeingType.PLANT));
            }
        }

        Drawer.receiveMostPopularChars(mostPopularAnimalChars, mostPopularPlantChars);
    }

    /**
     * Завершает работу пула потоков.
     * Пытается корректно завершить задачи в течение 3 секунд. Если не удаётся — форсирует завершение всех потоков.
     */
    private static void shutdown()
    {
        executor.shutdown();

        try
        {
            boolean isTerminated = executor.awaitTermination(3, TimeUnit.SECONDS);
            if (!isTerminated)
            {
                executor.shutdownNow();
            }
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

    }


}
