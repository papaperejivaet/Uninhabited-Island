package view;

import model.properties.GeneralConstants;

public class Drawer
{

//
//    public static void draw()
//    {
//        System.out.println("╔═══════╗");
//        System.out.printf("║%s  %s  ║\n", content[0][0], content[0][1]);
//        System.out.printf("║%s  %s  ║\n", content[1][0], content[1][1]);
//        System.out.println("╚═══════╝");
//    }

    private static void drawFirstQuarter()
    {

    }

    public static void drawField() {
        StringBuilder sb = new StringBuilder();

        drawTopBorder(sb);

        for (int row = 0; row < GeneralConstants.LENGTH; row++) {
            drawCellContent(sb);

            if (row < GeneralConstants.LENGTH - 1) {
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

    private static void drawCellContent(StringBuilder sb) {
        for (int inner = 0; inner < 2; inner++) {
            sb.append("║");
            for (int col = 0; col < GeneralConstants.HEIGHT; col++) {
                sb.append("    ");
                sb.append("║");
            }
            sb.append("\n");
        }
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
