package model.main;


import model.properties.Encyclopedia;
import model.properties.GeneralConstants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Island
{

    private static final Cell[][] islandMap = new Cell[GeneralConstants.HEIGHT][GeneralConstants.LENGTH];

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    public static void main(String[] args)
    {
        createIsland();
    }

    private static void createIsland()
    {
        createMap();
        populateRandomly();
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

    private static void populateRandomly()
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

    private static void startSimulation()
    {
        for (Cell[] cells : islandMap)
        {
            for (Cell cell : cells)
            {
                executor.submit(new SimulationTask(cell));
            }
        }
    }


}
