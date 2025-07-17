package model;



public class Island
{
    private static final int WIDTH = 100;
    private static final int HEIGHT = 20;
    private static final Cell[][] islandMap = new Cell[HEIGHT][WIDTH];

    public static void main(String[] args)
    {
        createMap();
    }

    public static void createMap()
    {
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                islandMap[y][x] = new Cell(x, y);
            }
        }
    }

}
