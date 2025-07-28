package view;

import model.main.Cell;
import model.main.Island;
import model.main.Statistics;
import model.properties.DeathCause;
import model.properties.EndReason;
import util.GeneralConstants;

import java.util.List;


public class Drawer
{
    private static final int ROWS = GeneralConstants.LENGTH;
    private static final int COLS = GeneralConstants.HEIGHT;
    private static final String BORDER = "=".repeat(15);

    private static List<String> mostPopularAnimalChars;
    private static List<String> mostPopularPlantChars;


    public static void receiveMostPopularChars(List<String> mostPopularAnimalChars,
                                               List<String> mostPopularPlantChars)
    {
        Drawer.mostPopularAnimalChars = mostPopularAnimalChars;
        Drawer.mostPopularPlantChars = mostPopularPlantChars;
    }

    public static void drawField() {
        StringBuilder sb = new StringBuilder();

        drawStatistics();
        drawTopBorder(sb);

        for (int row = 0; row < ROWS; row++) {
            drawCellContent(sb, row);

            if (row < ROWS - 1) {
                drawRowSeparator(sb);
            } else {
                drawBottomBorder(sb);
            }
        }
        System.out.print(sb);
    }

    private static void drawStatistics()
    {
        System.out.println(BORDER + "DAY " + Statistics.getCurrentCycleNumber() + BORDER);
        System.out.println("Животных спарено: " + Statistics.getBreedingCount());
        System.out.println("Животных съедено: " + Statistics.getDeathCount(DeathCause.EATEN));
        System.out.println("Животных умерло от голода: " + Statistics.getDeathCount(DeathCause.HUNGER));
        System.out.println("Животных умерло от старости: " + Statistics.getDeathCount(DeathCause.NATURAL));
        System.out.println("Животные, которые поели: " + Statistics.getAteCount());
    }

    private static void drawTopBorder(StringBuilder sb) {
        sb.append("╔");
        sb.append("════╦".repeat(Math.max(0, GeneralConstants.HEIGHT - 1)));
        sb.append("════╗\n");
    }

    private static void drawCellContent(StringBuilder sb, int row) {
        // Верхняя строка с животными
        int index;
        sb.append("║");
        for (int col = 0; col < COLS; col++) {
            index = row * COLS + col;
            String animalChar = mostPopularAnimalChars.get(index);
            String plantChar = mostPopularPlantChars.get(index);
            sb.append(animalChar).append(plantChar).append("║");
        }
        sb.append("\n");

        // Нижняя строка, v
        sb.append("║");
        for (int col = 0; col < COLS; col++) {
            index = row * COLS + col;

            sb.append("    ").append("║");
        }
        sb.append("\n");
    }

    private static void drawRowSeparator(StringBuilder sb) {
        sb.append("╠");
        sb.append("════╬".repeat(Math.max(0, GeneralConstants.HEIGHT - 1)));
        sb.append("════╣\n");
    }

    private static void drawBottomBorder(StringBuilder sb) {
        sb.append("╚");
        sb.append("════╩".repeat(Math.max(0, GeneralConstants.HEIGHT - 1)));
        sb.append("════╝\n");
    }

    public static void drawEnd(EndReason reason)
    {
        switch (reason)
        {
            case ALL_DEAD -> System.out.println("Все животные погибли");
            case ONLY_CARNIVORE_LEFT -> System.out.println("Остались только хищники");
            case ONLY_HERBIVORE_LEFT -> System.out.println("Остались только травоядные");
            case TIMEOUT -> System.out.println("Закончилось время!");
            case NO_PLANTS_LEFT -> System.out.println("Все растения вымерли!");
        }
    }

    public static void drawEnd()
    {
        System.out.println("Остались на поле: ");
        for (Cell[] cells : Island.getIslandMap())
        {
            for (Cell cell : cells)
            {

            }
        }
    }

}
