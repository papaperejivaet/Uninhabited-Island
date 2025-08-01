package view;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.main.Cell;
import model.main.Island;
import model.main.Statistics;
import model.properties.DeathCause;
import model.properties.EndReason;
import util.GeneralConstants;

import java.util.List;

/**
 * Утилитный класс, отвечающий за визуализацию текущего состояния острова в консоль.
 * <p>
 * Отображает сетку острова, статистику смертей, спаривания, количество съеденных животных и другие данные.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Drawer
{
    private static final int ROWS = GeneralConstants.LENGTH;
    private static final int COLS = GeneralConstants.HEIGHT;
    private static final String BORDER = "=".repeat(15);

    private static List<String> mostPopularAnimalChars;
    private static List<String> mostPopularPlantChars;

    /**
     * Устанавливает символы самых популярных животных и растений для дальнейшей отрисовки поля.
     *
     * @param mostPopularAnimalChars список символов животных для каждой клетки
     * @param mostPopularPlantChars  список символов растений для каждой клетки
     */
    public static void receiveMostPopularChars(List<String> mostPopularAnimalChars,
                                               List<String> mostPopularPlantChars)
    {
        Drawer.mostPopularAnimalChars = mostPopularAnimalChars;
        Drawer.mostPopularPlantChars = mostPopularPlantChars;
    }

    /**
     * Отрисовывает текущее состояние игрового поля и статистику за текущий цикл.
     */
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

    /**
     * Отображает статистику за текущий день, включая количество спариваний, смертей и поеданий.
     */
    private static void drawStatistics()
    {
        System.out.println(BORDER + "DAY " + Statistics.getCurrentCycleNumber() + BORDER);
        System.out.println("Животных спарено: " + Statistics.getBreedingCount());
        System.out.println("Животных съедено: " + Statistics.getDeathCount(DeathCause.EATEN));
        System.out.println("Животных умерло от голода: " + Statistics.getDeathCount(DeathCause.HUNGER));
        System.out.println("Животных умерло от старости: " + Statistics.getDeathCount(DeathCause.NATURAL));
        System.out.println("Животные, которые поели: " + Statistics.getAteCount());
    }

    /**
     * Отрисовывает верхнюю границу таблицы.
     *
     * @param sb строковый буфер, в который добавляется разметка
     */
    private static void drawTopBorder(StringBuilder sb) {
        sb.append("╔");
        sb.append("════╦".repeat(Math.max(0, GeneralConstants.HEIGHT - 1)));
        sb.append("════╗\n");
    }

    /**
     * Отрисовывает содержимое строки поля: животных и растения.
     *
     * @param sb  строковый буфер
     * @param row индекс строки
     */
    private static void drawCellContent(StringBuilder sb, int row) {
        // Верхняя строка с животными и растениями
        int index;
        sb.append("║");
        for (int col = 0; col < COLS; col++) {
            index = row * COLS + col;
            String animalChar = mostPopularAnimalChars.get(index);
            String plantChar = mostPopularPlantChars.get(index);
            sb.append(animalChar).append(plantChar).append("║");
        }
        sb.append("\n");


        sb.append("║");
        for (int col = 0; col < COLS; col++) {
            sb.append("    ").append("║");
        }
        sb.append("\n");
    }

    /**
     * Отрисовывает разделительную строку между строками поля.
     *
     * @param sb строковый буфер
     */
    private static void drawRowSeparator(StringBuilder sb) {
        sb.append("╠");
        sb.append("════╬".repeat(Math.max(0, GeneralConstants.HEIGHT - 1)));
        sb.append("════╣\n");
    }

    /**
     * Отрисовывает нижнюю границу таблицы.
     *
     * @param sb строковый буфер
     */
    private static void drawBottomBorder(StringBuilder sb) {
        sb.append("╚");
        sb.append("════╩".repeat(Math.max(0, GeneralConstants.HEIGHT - 1)));
        sb.append("════╝\n");
    }

    /**
     * Отображает финальное сообщение при завершении симуляции по определённой причине.
     *
     * @param reason причина завершения симуляции
     */
    public static void drawEnd(EndReason reason)
    {
        System.out.println(BORDER.repeat(7));
        switch (reason)
        {
            case ALL_DEAD -> System.out.println("                                        Все животные погибли");
            case ONLY_CARNIVORE_LEFT -> System.out.println("                                        Остались только хищники");
            case ONLY_HERBIVORE_LEFT -> System.out.println("                                        Остались только травоядные");
            case TIMEOUT -> System.out.println("                                        Закончилось время!");
            case NO_PLANTS_LEFT -> System.out.println("                                        Все растения вымерли!");
        }
        System.out.println(BORDER.repeat(7));
    }



}
