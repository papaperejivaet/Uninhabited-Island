package model.main;


import model.main.tasks.MoveTask;
import model.main.tasks.PopulationTask;
import model.main.tasks.LiveTask;
import model.properties.Encyclopedia;
import model.properties.Registry;
import util.GeneralConstants;
import model.properties.LivingBeingType;
import view.Drawer;

import java.util.ArrayList;
import java.util.Arrays;
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
        startSimulation();
    }

    private static void startSimulation()
    {
        do
        {
            simulate();
            Drawer.drawField();
        }
        while (Statistics.checkConditions());
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
        for (int i = 0; i < livingBeingCounter; i++)
        {
            executor.submit(new PopulationTask(i, phaser));
        }
        phaser.awaitAdvance(phaser.arriveAndDeregister());
    }


    static Cell getCell(int x, int y)
    {
        return islandMap[y][x];
    }

    private static void simulate()
    {

        phaser.register();
        for (Cell[] cells : islandMap)
        {
            for (Cell cell : cells)
            {
                executor.submit(new MoveTask(cell, phaser));
            }
        }
        System.out.println("first: " + phaser.getRegisteredParties());
        phaser.awaitAdvance(phaser.arriveAndDeregister());
        System.out.println("first end: " + phaser.getRegisteredParties());

        phaser.register();
        for (Cell[] cells : islandMap)
        {
            for (Cell cell : cells)
            {
                executor.submit(new LiveTask(cell, phaser));
            }
        }

        System.out.println("second: " + phaser.getRegisteredParties());
        phaser.awaitAdvance(phaser.arriveAndDeregister());
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


}
