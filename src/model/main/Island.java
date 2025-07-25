package model.main;


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
    private static final Cell[][] islandMap = new Cell[GeneralConstants.HEIGHT][GeneralConstants.LENGTH];

    private static final ExecutorService executor = Executors.newFixedThreadPool(GeneralConstants.MAX_PROCESSING_THREADS);
    private static final Phaser phaser = new Phaser(1);



    public static void main(String[] args)
    {
        createMap();
        populateRandomly();
        simulate();
    }

    private static void simulate()
    {
        int i = 0;
        do
        {
            i++;
            startSimulation();
            Drawer.drawField();
            System.out.println("Cycle number: " + i);
        }
        while (Statistics.checkConditions());
        shutdown();
    }

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

    private static void populateRandomly()
    {
        int livingBeingCounter = Encyclopedia.values().length;
        phaser.bulkRegister(livingBeingCounter);
        for (int i = 0; i < livingBeingCounter; i++)
        {
            executor.submit(new PopulationTask(i, phaser));
        }
        System.out.println("Populate Randomly: before await");
        phaser.arriveAndAwaitAdvance();
        System.out.println("Populate Randomly: after await, registered: " + phaser.getRegisteredParties());
    }


    static Cell getCell(int x, int y)
    {
        return islandMap[y][x];
    }

    private static void startSimulation()
    {
        System.out.println("startSimulation");
        phaser.bulkRegister(GeneralConstants.CELLS_AMOUNT);

        for (Cell[] cells : islandMap)
        {
            for (Cell cell : cells)
            {
                executor.submit(new MoveTask(cell, phaser));
            }
        }

        System.out.println("startSimulation: before 1 await");
        phaser.arriveAndAwaitAdvance();
        System.out.println("startSimulation: after 1 await, unarrived: " + phaser.getUnarrivedParties());


        phaser.bulkRegister(GeneralConstants.CELLS_AMOUNT);

        for (Cell[] cells : islandMap)
        {
            for (Cell cell : cells)
            {
                executor.submit(new LiveTask(cell, phaser));
            }
        }
        System.out.println("startSimulation: before 2 await");
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        System.out.println("Unarrived: " + phaser.getUnarrivedParties());
        phaser.arriveAndAwaitAdvance();
        System.out.println("startSimulation: after 2 await, unarrived: " + phaser.getUnarrivedParties());
        sendEveryCellChar();
        Statistics.nextCycle();

    }

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
