package view;

import util.GeneralConstants;

import java.util.List;


public class Drawer
{
    private static final int ROWS = GeneralConstants.LENGTH;
    private static final int COLS = GeneralConstants.HEIGHT;

    private static List<String> mostPopularAnimalChars;
    private static List<String> mostPopularPlantChars;
//
//    public static void draw()
//    {
//        System.out.println("╔═══════╗");
//        System.out.printf("║%s  %s  ║\n", content[0][0], content[0][1]);
//        System.out.printf("║%s  %s  ║\n", content[1][0], content[1][1]);
//        System.out.println("╚═══════╝");
//    }

    public static void receiveMostPopularChars(List<String> mostPopularAnimalChars,
                                               List<String> mostPopularPlantChars)
    {
        Drawer.mostPopularAnimalChars = mostPopularAnimalChars;
        Drawer.mostPopularPlantChars = mostPopularPlantChars;
    }

    public static void drawField() {
        StringBuilder sb = new StringBuilder();

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
            sb.append(animalChar).append("  ").append("║");
        }
        sb.append("\n");

        // Нижняя строка с растениями
        sb.append("║");
        for (int col = 0; col < COLS; col++) {
            index = row * COLS + col;
            String plantChar = mostPopularPlantChars.get(index);
            sb.append(plantChar).append("  ").append("║");
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


}
