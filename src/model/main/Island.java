package model.main;


import model.main.tasks.MoveTask;
import model.main.tasks.PopulationTask;
import model.main.tasks.LiveTask;
import model.properties.Encyclopedia;
import model.properties.GeneralConstants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class Island
{
    private static final Cell[][] islandMap = new Cell[GeneralConstants.HEIGHT][GeneralConstants.LENGTH];

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);
    private static final Phaser phaser = new Phaser();
    private static int cellsWithPredators;



    public void main(String[] args)
    {
        createIsland();
    }

    private void createIsland()
    {
        createMap();
        populateRandomly();
        while (Statistics.checkConditions())
        {
            simulate();
        }
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
    }

    private void populateRandomly()
    {
        int livingBeingCounter = Encyclopedia.values().length;
        for (int i = 0; i < livingBeingCounter; i++)
        {
            executor.submit(new PopulationTask(i));
        }

    }


    static Cell getCell(int x, int y)
    {
        return islandMap[x][y];
    }

    private void simulate()
    {
        phaser.register();

        for (Cell[] cells : islandMap)
        {
            for (Cell cell : cells)
            {
                executor.submit(new MoveTask(cell, phaser));
            }
        }

        phaser.arriveAndAwaitAdvance();

        phaser.register();

        for (Cell[] cells : islandMap)
        {
            for (Cell cell : cells)
            {
                executor.submit(new LiveTask(cell, phaser));
            }

        }

        phaser.arriveAndAwaitAdvance();

    }




}
